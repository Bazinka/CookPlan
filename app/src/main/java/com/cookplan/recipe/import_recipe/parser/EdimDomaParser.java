package com.cookplan.recipe.import_recipe.parser;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ShopListStatus;
import com.cookplan.utils.Utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public class EdimDomaParser extends BaseParser {

    public EdimDomaParser(String url) {
        super(url);
    }

    @Override
    protected String parseDescriptionFromDoc(Document doc) {
        String description = "";
        Elements descriptionList = doc.select("div.content-box");
        for (Element desc : descriptionList) {
            if (desc.select("div.recipe-step-title").size() != 0) {
                description = description + desc.select("div.plain-text").text() + "\n";
            }
        }
        return description;
    }

    @Override
    protected List<String> parseImageUrlsFromDoc(Document doc) {
        List<String> imageUrls = new ArrayList<>();
        Elements imagesElements = doc.select("div.thumb-slider__slide");
        for (Element element : imagesElements) {
            if (element.children().size() == 1) {
                String url = element.child(0).attr("src");
                if (!url.isEmpty()) {
                    imageUrls.add("https:" + url);
                }
            }
        }
        return imageUrls;
    }

    private String getProductNameTag() {
        return "div.checkbox-info__name";
    }

    private String getIngredientItemTag() {
        return "tr.definition-list-table__tr";
    }

    private String getAmountTag() {
        return "td.definition-list-table__td_value";
    }

    @Override
    protected String parseRecipeTitleFromDoc(Document doc) {
        return doc.select("h1.recipe-header__name").text();
    }

    @Override
    protected List<String> getProductsNames(Document doc) {
        List<String> names = new ArrayList<>();

        Elements products = doc.select(getIngredientItemTag());
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

    @Override
    protected Map<String, List<Ingredient>> parceDocumentToIngredientList(Document doc, Map<String, List<Product>> namesToProducts) {
        Map<String, List<Ingredient>> ingredientMap = new HashMap<>();
        Elements ingredElements = doc.select(getIngredientItemTag());
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