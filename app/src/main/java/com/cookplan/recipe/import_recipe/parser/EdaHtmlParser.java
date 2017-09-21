package com.cookplan.recipe.import_recipe.parser;

import com.cookplan.R;
import com.cookplan.RApplication;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public class EdaHtmlParser extends BaseParser {


    private ProductProvider productDataProvider;
    private CompositeDisposable disposables;

    public EdaHtmlParser(String url) {
        super(url);
        productDataProvider = new ProductProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    protected void parseDocument(Document doc) {
        List<String> names = getProductsNames(doc);
        disposables.add(
                productDataProvider.getTheClosestProductsToStrings(names)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Map<String, List<Product>>>() {
                            @Override
                            public void onNext(Map<String, List<Product>> namesToProducts) {
                                disposables.clear();
                                Map<String, List<Ingredient>> ingredients = parceDocumentToIngredientList(doc, namesToProducts);
                                Recipe recipe = parceDocumentToRecipe(doc);
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

    private Recipe parceDocumentToRecipe(Document doc) {
        Recipe recipe = new Recipe();
        recipe.setName(parseRecipeTitleFromDoc(doc));

        String description = parseDescriptionFromDoc(doc);

        recipe.setDesc(description);

        List<String> imageUrls = parseImageUrlsFromDoc(doc);

        recipe.setImageUrls(imageUrls);
        return recipe;
    }

    private String parseDescriptionFromDoc(Document doc) {
        String description = "";
        Elements descriptionList = doc.select("div.step-description");
        for (Element desc : descriptionList) {
            description = description + desc.text() + "\n";
        }
        return description;
    }

    private String parseRecipeTitleFromDoc(Document doc) {
        return doc.title();
    }

    private List<String> parseImageUrlsFromDoc(Document doc) {
        List<String> imageUrls = new ArrayList<>();
        Elements imagesElements = doc.select("div.g-first-page-block").select("div.s-photo-gall__trigger");
        String imageUrlsString = imagesElements.attr("data-gall-photos-urls");
        if (imageUrlsString != null) {
            String[] splits = imageUrlsString.split(",");
            for (String url : splits) {
                if (!url.isEmpty()) {
                    imageUrls.add("https:" + url.replace("'", ""));
                }
            }
        }
        return imageUrls;
    }

    private String getProductNameTag() {
        return "span.content-item__name";
    }

    private String getIngredientItemTag() {
        return "p.ingredients-list__content-item";
    }

    private Elements getIngredientsElementList(Document doc) {
        Element ingredients = doc.select("div.ingredients-list").first();
        Elements products = null;
        if (ingredients != null) {
            products = ingredients.select(getIngredientItemTag());
        }
        return products;
    }

    private String getAmountTag() {
        return "span.content-item__measure";
    }


    private List<String> getProductsNames(Document doc) {
        List<String> names = new ArrayList<>();
        Elements products = getIngredientsElementList(doc);
        for (Element product : products) {
            String name = parseStringProductName(product);
            if (!name.isEmpty()) {
                names.add(name);
            }
        }
        return names;
    }

    private String parseStringProductName(Element element) {
        Elements nameElem = element.select(getProductNameTag());
        if (nameElem == null || nameElem.size() == 0) {
            nameElem = element.select("span.name");//if product doesn't have category on the website.
        }
        String name = nameElem != null ? nameElem.text() : "";
        return name;
    }


    private String getMeasureUnitString(Double amount, Element element) {
        Elements amountElem = element.select(getAmountTag());
        if (amountElem.size() == 1) {
            String amountUnitString = amountElem.text();
            amountUnitString = amountUnitString.replace("½ ", "");
            amountUnitString = amountUnitString.replace("¾ ", "");
            String amountString;
            double value = Math.abs(amount - Math.round(amount));
            if (value < 1e-8 && value > -1e-8) {
                amountString = Integer.valueOf(amount.intValue()).toString();
            } else {
                amountString = amount.toString();
            }
            amountUnitString = amountUnitString.replace(amountString + " ", "");
            return amountUnitString;
        }
        return null;
    }

    private Map<String, List<Ingredient>> parceDocumentToIngredientList(Document doc, Map<String, List<Product>> namesToProducts) {
        Map<String, List<Ingredient>> ingredientMap = new HashMap<>();
        Elements ingredElements = getIngredientsElementList(doc);
        for (Element element : ingredElements) {
            String name = parseStringProductName(element);
            if (!name.isEmpty()) {
                List<Ingredient> ingredients = new ArrayList<>();
                for (Product product : namesToProducts.get(name)) {
                    ingredients.add(parseIngredient(product, element));
                }
                ingredientMap.put(name + ": " + element.select(getAmountTag()).text(), ingredients);
            }
        }
        return ingredientMap;
    }

    private Ingredient parseIngredient(Product product, Element element) {
        Double amount = parseAmount(element);
        MeasureUnit unit = parseMeasureUnit(amount, element);
        return new Ingredient(null,
                              product.toStringName(),
                              product,
                              null,
                              unit,
                              amount,
                              ShopListStatus.NONE);
    }

    private MeasureUnit parseMeasureUnit(Double amount, Element element) {
        MeasureUnit unit = MeasureUnit.UNITS;
        String unitString = getMeasureUnitString(amount, element);
        if (unitString != null) {
            unit = MeasureUnit.parseUnit(unitString);
        }
        return unit;
    }

    private Double parseAmount(Element element) {
        Double amount = 0.;
        Elements amountElem = element.select(getAmountTag());
        if (amountElem.size() == 1) {
            String amountString = amountElem.text();
            if (!amountString.contains(RApplication.getAppContext().getString(R.string.by_the_taste))) {
                String[] splited = amountString.split("\\s+");
                amount = Utils.getDoubleFromString(splited[0]);
            }
        }
        return amount;
    }
}