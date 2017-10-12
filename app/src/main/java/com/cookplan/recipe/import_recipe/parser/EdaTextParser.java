package com.cookplan.recipe.import_recipe.parser;

import android.util.Log;

import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.impl.ProductProviderImpl;
import com.cookplan.utils.Utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public class EdaTextParser extends BaseParser {

    private final static String INGRIDIENTS_LIST_PATTERN = "Ингредиенты" +
            "(\\s[\\p{InCyrillic}\\d]+)+(\\s\\d+)(\\s[\\p{InCyrillic}\\d]+)";

    private final static String INGRIDIENT_PATTERN = "(\\p{Lu}\\p{InCyrillic}+)(\\s\\p{InCyrillic}+)?(\\s\\d+)(\\s[\\p{InCyrillic}\\d]+)";

    private final static String INGRIDIENT_NAME_PATTERN = "(\\p{Lu}\\p{InCyrillic}+)(\\s\\p{InCyrillic}+)?";

    private final static String RECIPE_AMOUNT_PATTERN = "([\\d½¾¼]+)";

    private final static String RECIPE_MEASURE_PATTERN = "(\\s[\\p{InCyrillic}.]+)+$";

    private final static String RECIPE_DESCRIPTION_PATTERN = "(\\s\\d+[.]((\\s[\\p{InCyrillic},]+)+[.])+)+";

    private final static String RECIPE_NAME_PATTERN = "^\\p{InCyrillic}+(\\s[\\p{InCyrillic},]+)+";

    private ProductProvider productDataProvider;
    private CompositeDisposable disposables;

    public EdaTextParser(String url) {
        super(url);
        productDataProvider = new ProductProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    protected void parseDocument(Document doc) {
        String recipeText = doc.text();

        List<String> names = new ArrayList<>();
        List<String> ingredientsStringList = new ArrayList<>();
        Pattern ingredListPattern = Pattern.compile(INGRIDIENTS_LIST_PATTERN);
        Matcher ingredListMatcher = ingredListPattern.matcher(recipeText);
        if (ingredListMatcher.find()) {
            String ingredientsListString = ingredListMatcher.group().toString();

            Pattern ingredPattern = Pattern.compile(INGRIDIENT_PATTERN);
            Matcher ingredMatcher = ingredPattern.matcher(ingredientsListString);
            int start = 0;
            while (ingredMatcher.find(start)) {
                String ingredient = ingredMatcher.group().toString();
                Log.d("PARSING", "ingredient: " + ingredient);
                ingredientsStringList.add(ingredient);

                names.add(parseStringProductName(ingredient));

                start = ingredMatcher.start() + 1;
            }
        } else {
            Log.d("PARSING", "didn't found ingredients.");
        }

        if (!names.isEmpty()) {
            disposables.add(
                    productDataProvider.getTheClosestProductsToStrings(names)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<Map<String, List<Product>>>() {
                                @Override
                                public void onNext(Map<String, List<Product>> namesToProducts) {
                                    disposables.clear();
                                    Map<String, List<Ingredient>> ingredients = parceStringToIngredientList(ingredientsStringList, namesToProducts);
                                    Recipe recipe = getRecipeObject(recipeText);
                                    List<String> imageUrls = parseImageUrlsFromDoc(doc);
                                    recipe.setImageUrls(imageUrls);
                                    onImportSuccess(recipe, ingredients);
                                }


                                @Override
                                public void onError(Throwable e) {
                                    onImportError(e);
                                }

                                @Override
                                public void onComplete() {

                                }
                            }));
        }
    }

    private Recipe getRecipeObject(String allRecipeText) {
        Recipe recipe = new Recipe();
        Pattern recipeNamePattern = Pattern.compile(RECIPE_NAME_PATTERN);
        Matcher recipeNameMatcher = recipeNamePattern.matcher(allRecipeText);
        if (recipeNameMatcher.find()) {
            recipe.setName(recipeNameMatcher.group().toString());
        }

        Pattern recipeDescPattern = Pattern.compile(RECIPE_DESCRIPTION_PATTERN);
        Matcher recipeDescMatcher = recipeDescPattern.matcher(allRecipeText);
        String recipeDescription = "";
        int start = 0;
        while (recipeDescMatcher.find(start)) {
            if (!recipeDescription.contains(recipeDescMatcher.group().toString())) {
                recipeDescription += recipeDescMatcher.group().toString();
            }
            Log.d("PARSING", "recipe desc: " + recipeDescMatcher.group().toString());
            start = recipeDescMatcher.start() + 1;
        }
        recipe.setDesc(recipeDescription);


        return recipe;
    }

    private List<String> parseImageUrlsFromDoc(Document doc) {
        List<String> imageUrls = new ArrayList<>();

        Elements imagesElements = doc.select("img");
        imagesElements = imagesElements.isEmpty() ? doc.select("amp-img") : imagesElements;
        for (Element imageElement : imagesElements) {
            String absoluteUrl = imageElement.absUrl("src");  //absolute URL on src
            if (absoluteUrl.contains("eda.ru")) {
                imageUrls.add(absoluteUrl);
            }
        }
        return imageUrls;
    }

    private String parseStringProductName(String ingredientString) {
        Pattern ingredNamePattern = Pattern.compile(INGRIDIENT_NAME_PATTERN);
        Matcher ingredNameMatcher = ingredNamePattern.matcher(ingredientString);
        String name = "";
        if (ingredNameMatcher.find()) {
            name = ingredNameMatcher.group().toString();
            Log.d("PARSING", "ingredient name: " + name);
        }
        return name;
    }

    private Map<String, List<Ingredient>> parceStringToIngredientList(List<String> ingredientListString, Map<String, List<Product>> namesToProducts) {
        Map<String, List<Ingredient>> ingredientMap = new HashMap<>();
        for (String element : ingredientListString) {
            String name = parseStringProductName(element);
            if (!name.isEmpty()) {
                List<Ingredient> ingredients = new ArrayList<>();
                for (Product product : namesToProducts.get(name)) {
                    ingredients.add(parseIngredient(product, element));
                }
                ingredientMap.put(element, ingredients);
            }
        }
        return ingredientMap;
    }

    private Ingredient parseIngredient(Product product, String ingredientAllText) {
        Pattern amountPattern = Pattern.compile(RECIPE_AMOUNT_PATTERN);
        Matcher amountMatcher = amountPattern.matcher(ingredientAllText);
        Double amount = 0.;
        if (amountMatcher.find()) {
            amount = Utils.getDoubleFromString(amountMatcher.group().toString());
            Log.d("PARSING", "amount: " + amount);
        }

        Pattern measurePattern = Pattern.compile(RECIPE_MEASURE_PATTERN);
        Matcher measureMatcher = measurePattern.matcher(ingredientAllText);
        MeasureUnit unit = MeasureUnit.UNITS;
        if (measureMatcher.find()) {
            unit = MeasureUnit.parseUnit(measureMatcher.group().toString());
            Log.d("PARSING", "amount: " + amount);
        }

        return new Ingredient(null,
                              product.toStringName(),
                              product,
                              null,
                              unit,
                              amount,
                              ShopListStatus.NONE);
    }

}