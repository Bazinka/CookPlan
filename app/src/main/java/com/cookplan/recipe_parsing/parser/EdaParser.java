package com.cookplan.recipe_parsing.parser;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public class EdaParser extends BaseParser {

    public EdaParser(String url) {
        super(url);
    }

    @Override
    protected String getDescTagName() {
        return "div.step-description";
    }

    private String getProductNameTag() {
        return "span.ingredient";
    }

    private String getIngredientItemTag() {
        return "li.ingredient-item";
    }

    private String getAmountTag() {
        return "span.amount";
    }

    @Override
    protected List<String> getProductsNames(Document doc) {
        List<String> names = new ArrayList<>();

        Elements products = doc.select(getIngredientItemTag());
        for (Element product : products) {
            Elements nameElem = product.select(getProductNameTag());
            String name = nameElem != null ? nameElem.text() : "";
            if (!name.isEmpty()) {
                names.add(name);
            }
        }

        return names;
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
    protected Map<String, List<Ingredient>> parceDocumentToIngredientList(Document doc, List<Product> products) {
        Map<String, List<Ingredient>> ingredientMap = new HashMap<>();
        Elements ingredElements = doc.select(getIngredientItemTag());
        for (Element element : ingredElements) {
            Elements nameElem = element.select(getProductNameTag());
            if (nameElem == null || nameElem.size() == 0) {
                nameElem = element.select("span.name");//if product doesn't have category on the website.
            }
            String name = nameElem != null ? nameElem.text() : "";
            if (!name.isEmpty()) {
                List<Ingredient> ingredients = new ArrayList<>();
                for (Product product : products) {
                    Pattern p = Pattern.compile(Utils.getRegexAtLeastOneWord(name));
                    Matcher matcher = p.matcher(product.toStringName().toLowerCase());
                    if (matcher.find()) {
                        Double amount = parseAmount(element);
                        MeasureUnit unit = parseMeasureUnit(amount, element);
                        Ingredient ingredient = new Ingredient(null,
                                                               product.toStringName(),
                                                               product,
                                                               null,
                                                               unit,
                                                               amount,
                                                               ShopListStatus.NONE);
                        ingredients.add(ingredient);
                    }
                }
                ingredientMap.put(name + ": " + element.select(getAmountTag()).text(), ingredients);
            }
        }
        return ingredientMap;
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
                try {
                    if (splited[0].equals("½")) {
                        amount = 0.5;
                    } else if (splited[0].equals("¾")) {
                        amount = 3. / 4.;
                    } else {
                        amount = Double.valueOf(splited[0]);
                    }
                } catch (Exception e) {
                    //something went wrong, it means amount = 0;
                    e.printStackTrace();
                }
            }
        }
        return amount;
    }
}
