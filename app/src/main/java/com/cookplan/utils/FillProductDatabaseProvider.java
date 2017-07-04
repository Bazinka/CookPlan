package com.cookplan.utils;

import com.cookplan.RApplication;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.cookplan.models.MeasureUnit.BOTTLE;
import static com.cookplan.models.MeasureUnit.CUP;
import static com.cookplan.models.MeasureUnit.GRAMM;
import static com.cookplan.models.MeasureUnit.KILOGRAMM;
import static com.cookplan.models.MeasureUnit.LITRE;
import static com.cookplan.models.MeasureUnit.MILILITRE;
import static com.cookplan.models.MeasureUnit.PACKAGE;
import static com.cookplan.models.MeasureUnit.TABLESPOON;
import static com.cookplan.models.MeasureUnit.TEASPOON;
import static com.cookplan.models.MeasureUnit.UNITS;
import static com.cookplan.models.ProductCategory.BAKERY_PRODUCTS;
import static com.cookplan.models.ProductCategory.CHEESE;
import static com.cookplan.models.ProductCategory.CHILD_PRODUCTS;
import static com.cookplan.models.ProductCategory.CONFECTIONERY;
import static com.cookplan.models.ProductCategory.CONSERVATION;
import static com.cookplan.models.ProductCategory.DRINKS_JUICE;
import static com.cookplan.models.ProductCategory.EGGS;
import static com.cookplan.models.ProductCategory.FISH_SEAFOOD_CAVIAR;
import static com.cookplan.models.ProductCategory.FROZEN_FOOD;
import static com.cookplan.models.ProductCategory.FRUITS_VEGETABLES;
import static com.cookplan.models.ProductCategory.GROCERY;
import static com.cookplan.models.ProductCategory.ICE_CREAM;
import static com.cookplan.models.ProductCategory.MEAT_BIRDS;
import static com.cookplan.models.ProductCategory.MEAT_GARSTRONOMY;
import static com.cookplan.models.ProductCategory.MILK_PRODUCT;
import static com.cookplan.models.ProductCategory.TEA_COFFEE_CACAO;
import static com.cookplan.models.ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS;
import static com.cookplan.models.ProductCategory.WITHOUT_CATEGORY;

/**
 * Created by DariaEfimova on 29.03.17.
 * <p>
 * class can use only for filling database for the first time!
 */

public class FillProductDatabaseProvider {

    private static void fillDatabase() {
        fillProductDatabase();
    }

    private static void fillProductDatabase() {
        savePriorityList();
    }

    public static void savePriorityList() {
        RApplication.savePriorityList(Arrays.asList(ProductCategory.values()));
    }


    public static List<Product> getNewProductList() {
        List<Product> productList = new LinkedList<>();
        MeasureUnit[] volumeUnitArray = new MeasureUnit[]{LITRE, MILILITRE, CUP, TEASPOON, TABLESPOON, PACKAGE, BOTTLE};
        Map<MeasureUnit, Double> litreUnitMap = new UnitsMapBuilder()
                .put(MILILITRE, 1000.)
                .put(CUP, 4.) // 250 мл.
                .put(TEASPOON, 202.)// 5 мл.
                .put(TABLESPOON, 67.)// 15 мл.
                .build();

        MeasureUnit[] weightUnitArray = new MeasureUnit[]{GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS};
        Map<MeasureUnit, Double> kilogramUnitMap = new UnitsMapBuilder()
                .put(GRAMM, 1000.)
                .build();

        productList.addAll(new CategoryBuilder(WITHOUT_CATEGORY,
                                               LITRE,
                                               new UnitsListBuilder()
                                                       .addAll(volumeUnitArray)
                                                       .build())
                                   .add("Вода",
                                        "Water",
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build()).build());


        //Молочные продукты ProductCategory.MILK_PRODUCT
        productList.addAll(new CategoryBuilder(MILK_PRODUCT,
                                               LITRE,
                                               new UnitsListBuilder()
                                                       .addAll(volumeUnitArray)
                                                       .build())
                                   .add("Молоко",
                                        "Milk",
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Молочный коктейль",
                                        "Milk shake",
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(PACKAGE, 1.5)
                                                .put(BOTTLE, 1.5)
                                                .put(UNITS, 1.5)
                                                .build())
                                   .add("Сливки",
                                        "Cream",
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(PACKAGE, 0.33)
                                                .put(UNITS, 0.33)
                                                .put(BOTTLE, 0.33)
                                                .build())
                                   .add("Сливки взбитые",
                                        "Whipped cream",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{MILILITRE, GRAMM, PACKAGE, BOTTLE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(MILILITRE, 250.)
                                                .put(GRAMM, 250.)
                                                .build())
                                   .add("Йогурт питьевой",
                                        "Drinking yogurt",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{LITRE, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(LITRE, 1.)
                                                .build())
                                   .add("Сырок творожный",
                                        "Curd cheese \\ quark",
                                        UNITS,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{UNITS})
                                                .build())
                                   .add("Кефир",
                                        "Kefir",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .addAll(new MeasureUnit[]{LITRE, MILILITRE})
                                                .build(),
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 4.)
                                                .put(TEASPOON, 200.)
                                                .put(TABLESPOON, 55.6)
                                                .put(LITRE, 1.1)
                                                .put(MILILITRE, 1100.)
                                                .build())
                                   .add("Йогурт",
                                        "Yogurt",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .addAll(new MeasureUnit[]{LITRE, MILILITRE})
                                                .build(),
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 4.)
                                                .put(TEASPOON, 200.)
                                                .put(TABLESPOON, 55.6)
                                                .put(LITRE, 1.1)
                                                .put(MILILITRE, 1100.)
                                                .build())
                                   .add("Греческий йогурт (большой)",
                                        "Greek yogurt",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .addAll(new MeasureUnit[]{LITRE, MILILITRE})
                                                .build(),
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 4.)
                                                .put(TEASPOON, 200.)
                                                .put(TABLESPOON, 55.6)
                                                .put(LITRE, 1.1)
                                                .put(MILILITRE, 1100.)
                                                .build())
                                   .add("Творог",
                                        "Cottage cheese",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(CUP, 7.4)
                                                .build())
                                   .add("Ацидофилин",
                                        "Acidophilus",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, MILILITRE, TEASPOON, TABLESPOON, CUP, UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(GRAMM, 500.)
                                                .put(MILILITRE, 500.)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 30.)
                                                .put(CUP, 2.)
                                                .put(UNITS, 1.)
                                                .build())

                                   .add("Варенец",
                                        "Clotted cream",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, MILILITRE, TEASPOON, TABLESPOON, CUP, UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(GRAMM, 500.)
                                                .put(MILILITRE, 500.)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 30.)
                                                .put(CUP, 2.)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Простокваша",
                                        "Clabber",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, MILILITRE, TEASPOON, TABLESPOON, CUP, UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(GRAMM, 500.)
                                                .put(MILILITRE, 500.)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 30.)
                                                .put(CUP, 2.)
                                                .put(UNITS, 1.)
                                                .build())

                                   .add("Ряженка",
                                        "Natural youghurt",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, MILILITRE, TEASPOON, TABLESPOON, CUP, UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(GRAMM, 500.)
                                                .put(MILILITRE, 500.)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 30.)
                                                .put(CUP, 2.)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Снежок",
                                        null,
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, MILILITRE, TEASPOON, TABLESPOON, CUP, UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(GRAMM, 500.)
                                                .put(MILILITRE, 500.)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 30.)
                                                .put(CUP, 2.)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Тан",
                                        "Tan",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, MILILITRE, TEASPOON, TABLESPOON, CUP, UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(GRAMM, 500.)
                                                .put(MILILITRE, 500.)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 30.)
                                                .put(CUP, 2.)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Мацони",
                                        "Maconi",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, MILILITRE, TEASPOON, TABLESPOON, CUP, UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(GRAMM, 300.)
                                                .put(MILILITRE, 300.)
                                                .put(TEASPOON, 60.)
                                                .put(TABLESPOON, 16.7)
                                                .put(CUP, 1.2)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Маргарин",
                                        "Margarine",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .build(),
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(TEASPOON, 250.)
                                                .put(TABLESPOON, 66.7)
                                                .put(CUP, 4.3)
                                                .build())
                                   .add("Масло сливочное",
                                        "Butter",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .build(),
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 50.)
                                                .put(CUP, 4.2)
                                                .build())
                                   .add("Масло топленое",
                                        "Melted butter",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .build(),
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 50.)
                                                .put(CUP, 4.2)
                                                .build())
                                   .add("Масло шоколадное",
                                        "Chocolate butter",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .build(),
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(TEASPOON, 217.)
                                                .put(TABLESPOON, 71.)
                                                .put(CUP, 5.)
                                                .build())
                                   .add("Сметана",
                                        "Sour cream",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .addAll(new MeasureUnit[]{LITRE, MILILITRE})
                                                .build(),
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 4.)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 40.)
                                                .put(LITRE, 1.)
                                                .put(MILILITRE, 1000.)
                                                .build())
                                   .add("Мусс",
                                        "Mousse",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{PACKAGE})
                                                .build())
                                   .add("Пудинг",
                                        "Pudding",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{PACKAGE})
                                                .build())
                                   .add("Смусси молочный",
                                        "Milky smoothie",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{PACKAGE})
                                                .build())
                                   .add("Каша рисовая молочная",
                                        "Milk rice porridge",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, UNITS, PACKAGE})
                                                .build())
                                   .add("Каша овсяная молочная",
                                        "Milk porridge",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, UNITS, PACKAGE})
                                                .build())
                                   .build());

        //Cыры ProductCategory.CHEESE
        productList.addAll(new CategoryBuilder(CHEESE,
                                               KILOGRAMM,
                                               new UnitsListBuilder()
                                                       .addAll(weightUnitArray)
                                                       .build())
                                   .add("Сыр твердый натертый",
                                        "Grated cheese",
                                        new UnitsListBuilder()
                                                .addAll(weightUnitArray)
                                                .build(),
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 8.3)
                                                .build())
                                   .add("Сыр твердый",
                                        "Hard cheese",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .add("Моцарелла",
                                        "Mozzarella",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .add("Сыр творожный",
                                        "Cream cheese",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .add("Маскарпоне",
                                        "Mascarpone",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .add("Рикотта",
                                        "Ricotta",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .add("Сыр плавленый",
                                        "Process cheese",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .add("Сыр копченый",
                                        "Smoked cheese",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .add("Брынза",
                                        "Brynza",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .add("Сулугуни",
                                        "Suluguni",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .add("Сырок плавленный",
                                        null,
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Сыр деликатесный",
                                        "Delicacy cheese",
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .build())
                                   .build());

        //Яйца ProductCategory.EGGS
        productList.addAll(new CategoryBuilder(EGGS,
                                               UNITS,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                                       .build())
                                   .add("Яйцо куриное",
                                        "Chicken egg",
                                        new UnitsMapBuilder()
                                                .build())
                                   .add("Яйцо перепелиное",
                                        "Quail egg",
                                        new UnitsMapBuilder()
                                                .build())
                                   .build());
        //Кондитерские изделия ProductCategory.CONFECTIONERY
        productList.addAll(new CategoryBuilder(CONFECTIONERY,
                                               PACKAGE,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{PACKAGE, GRAMM, KILOGRAMM, UNITS})
                                                       .build())
                                   .add("Шоколад горький",
                                        "Dark chocolate",
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{PACKAGE, GRAMM, KILOGRAMM, UNITS, CUP})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(GRAMM, 100.)
                                                .put(KILOGRAMM, 0.1)
                                                .put(UNITS, 1.)
                                                .put(CUP, 8.3)
                                                .build())
                                   .add("Шоколад молочный",
                                        "Milk chocolate",
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{PACKAGE, GRAMM, KILOGRAMM, UNITS, CUP})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(GRAMM, 100.)
                                                .put(KILOGRAMM, 0.1)
                                                .put(UNITS, 1.)
                                                .put(CUP, 8.3)
                                                .build())
                                   .add("Шоколад белый",
                                        "White chocolate",
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Шоколадная паста",
                                        "Chocolate paste",
                                        LITRE,
                                        new UnitsListBuilder()
                                                .addAll(volumeUnitArray)
                                                .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM})
                                                .build(),
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(GRAMM, 1400.)
                                                .put(KILOGRAMM, 1.4)
                                                .build())
                                   .add("Шоколадная фигурка",
                                        "Chocolate figure",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Яйцо шоколадное",
                                        "Chocolate egg",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Рулет кондитерский",
                                        "Rolls confectionery",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Кекс",
                                        "Tea cake",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Сахарная вата",
                                        "Cotton candy",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Круассан",
                                        "Croissant",
                                        UNITS)
                                   .add("Штрудель", "Strudel", UNITS)
                                   .add("Пирог", "Pie", UNITS)
                                   .add("Пирожное", "Cream cake", UNITS)
                                   .add("Торт", "Cake \\ Tart", UNITS)
                                   .add("Шарлотка", "Apple pie", UNITS)
                                   .add("Жевательная резинка", "Gum", UNITS)
                                   .add("Козинак", "Kozinak", UNITS)
                                   .add("Тарталетки", "Tartlets", UNITS)
                                   .add("Кукурузные палочки", "Corn sticks")
                                   .build());

        productList.addAll(new CategoryBuilder(CONFECTIONERY,
                                               KILOGRAMM,
                                               new UnitsListBuilder()
                                                       .addAll(weightUnitArray)
                                                       .build())
                                   .add("Драже", "Dragee")
                                   .add("Конфеты", "Candy")
                                   .add("Трюфели", "Truffles")
                                   .add("Глазированные фрукты и орехи", "Glazed fruits and nuts")
                                   .add("Печенье", "Cookies")
                                   .add("Вафли", "Waffles")
                                   .add("Зефир", "Zephyr food")
                                   .add("Мармелад", "Marmalade")
                                   .add("Маршмеллоу", "Marshmallow")
                                   .add("Пастила", "Pastila")
                                   .add("Суфле", "Souffle")
                                   .add("Кунафа", "Cunafa")
                                   .add("Пахлава", "Baklava")
                                   .add("Рахат-лукум", "Turkish Delight")
                                   .add("Халва", "Halva")
                                   .add("Чак-чак", "Chuck-chak")
                                   .add("Щербет", "Scherbet")
                                   .build());

        //Детские товары" ProductCategory.CHILD_PRODUCTS
        productList.addAll(new CategoryBuilder(CHILD_PRODUCTS,
                                               UNITS,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                                       .build())
                                   .add("Детское питание",
                                        "Baby food",
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{UNITS, PACKAGE, BOTTLE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(BOTTLE, 1.)
                                                .build())
                                   .add("Пюре детское",
                                        "Baby puree",
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{UNITS, PACKAGE, BOTTLE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(BOTTLE, 1.)
                                                .build())
                                   .add("Подгузники", "Diapers")
                                   .add("Пеленки", null)
                                   .add("Бутылочка детская", "Baby Bottles")
                                   .add("Кружка детская", "Children's mug")
                                   .add("Поильник детский", "Children's fountain")
                                   .add("Пустышка детская", "Child's dummy")
                                   .add("Ложка детская", "Children's spoon")
                                   .add("Тарелка детская", "Children's plate")
                                   .build());

        //        Напитки, соки ProductCategory.DRINKS_JUICE
        productList.addAll(new CategoryBuilder(DRINKS_JUICE,
                                               LITRE,
                                               new UnitsListBuilder()
                                                       .addAll(volumeUnitArray)
                                                       .build())
                                   .add("Вода питьевая",
                                        "Drinking water (bottle)",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Кисель",
                                        "Kissel",
                                        PACKAGE,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Газировка",
                                        "Fizzy water",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.5)
                                                .put(BOTTLE, 1.5)
                                                .build())
                                   .add("Вода минеральная",
                                        "Mineral water",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Лимонад",
                                        "Lemonade",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Тархун",
                                        "Tarhun \\ Estragon (drink)",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Компот",
                                        "Compote",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Морс",
                                        "Fruit-drink",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Сок",
                                        "Juice",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Нектар",
                                        "Nectar",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .put(BOTTLE, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Квас",
                                        "Kvass",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.5)
                                                .put(BOTTLE, 1.5)
                                                .put(PACKAGE, 1.5)
                                                .build())
                                   .add("Холодный чай (черный)",
                                        "Ice tea (black)",
                                        new UnitsMapBuilder()
                                                .build())
                                   .add("Холодный чай (зеленый)",
                                        "Ice tea (green)",
                                        new UnitsMapBuilder()
                                                .build())
                                   .build());

        //        Хлебобулочные изделия" ProductCategory.BAKERY_PRODUCTS
        productList.addAll(new CategoryBuilder(BAKERY_PRODUCTS,
                                               UNITS,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                                       .build())
                                   .add("Хлеб белый",
                                        "White bread",
                                        new UnitsMapBuilder()
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Хлеб для тостов и сэндвичей",
                                        "Bread for toasts and sandwiches",
                                        new UnitsMapBuilder()
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Хлеб зерновой",
                                        "Bread of grain",
                                        new UnitsMapBuilder()
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Хлебцы",
                                        null,
                                        new UnitsMapBuilder()
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Хлеб черный",
                                        "Black bread",
                                        new UnitsMapBuilder()
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Хлеб кукурузный",
                                        "Corn bread",
                                        new UnitsMapBuilder()
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Сухарики",
                                        "Crackers",
                                        PACKAGE,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Баранки",
                                        "Baranki",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                                .build())
                                   .add("Соломка",
                                        "Straw",
                                        PACKAGE,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Лепешка",
                                        null,
                                        new UnitsMapBuilder()
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Сушки",
                                        "Drying bread",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                                .build())
                                   .add("Сухари",
                                        "Sukhari",
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                                .build())
                                   .add("Хлебные палочки",
                                        "Bread sticks",
                                        PACKAGE,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Сдобная выпечка", "Butter buns")
                                   .add("Слоеная выпечка", "Layered pastry")
                                   .add("Песочная выпечка", "Sand pastry")
                                   .build());

        //        Овощи, фрукты, грибы, ягоды ProductCategory.
        productList.addAll(new CategoryBuilder(FRUITS_VEGETABLES,
                                               KILOGRAMM,
                                               new UnitsListBuilder()
                                                       .addAll(weightUnitArray)//GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS
                                                       .build())
                                   .add("Арахис",
                                        "Peanut",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.7)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .build())
                                   .add("Грецкий орех очищенный",
                                        "Walnut",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.8)
                                                .put(TEASPOON, 151.)
                                                .put(TABLESPOON, 50.)
                                                .build())
                                   .add("Кедровый орех",
                                        "Pine nut",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.2)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .build())
                                   .add("Кешью",
                                        "Cashew",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.)
                                                .put(TEASPOON, 90.)
                                                .put(TABLESPOON, 28.5)
                                                .build())
                                   .add("Миндаль",
                                        "Almond",
                                        new UnitsMapBuilder()
                                                .put(CUP, 6.2)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .build())
                                   .add("Изюм",
                                        "Raisins",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.7)
                                                .put(TEASPOON, 143.)
                                                .put(TABLESPOON, 40.)
                                                .build())
                                   .add("Курага",
                                        "Dried apricots",
                                        new UnitsMapBuilder()
                                                .put(CUP, 6.7)
                                                .build())
                                   .add("Смесь орехов",
                                        "Mix of nuts",
                                        new UnitsMapBuilder()
                                                .put(CUP, 6.)
                                                .build())
                                   .add("Фундук",
                                        "Hazelnut",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.8)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .build())
                                   .add("Фисташки",
                                        "Pistachio",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.8)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .build())
                                   .add("Семечки подсолнечные",
                                        "Sunflower seeds",
                                        new UnitsMapBuilder()
                                                .put(TABLESPOON, 40.)
                                                .put(TEASPOON, 130.)
                                                .put(CUP, 5.7)
                                                .build())
                                   .add("Капуста квашеная",
                                        "Sauerkraut",
                                        new UnitsMapBuilder()
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .put(CUP, 5.8)
                                                .build())
                                   .add("Семечки тыквенные",
                                        "Seeds of pumpkin",
                                        new UnitsMapBuilder()
                                                .put(TABLESPOON, 50.)
                                                .put(TEASPOON, 150.)
                                                .put(CUP, 5.8)
                                                .build())
                                   .add("Имбирь молотый",
                                        "Round ginger",
                                        new UnitsMapBuilder()
                                                .put(TABLESPOON, 100.)
                                                .put(TEASPOON, 333.)
                                                .build())
                                   .add("Ягоды",
                                        "Berries",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 100.)
                                                .put(CUP, 6.2)
                                                .build())
                                   .add("Фасоль",
                                        "Beans",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.5)
                                                .build())
                                   .add("Смесь орехов и сухофруктов", "Mix of nuts and dried fruits")
                                   .add("Сухофрукты", "Dried fruits")
                                   .add("Ягоды замороженные",
                                        "Frozen berries",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.)
                                                .build())
                                   .add("Овощное ассорти", "Vegetable mix")
                                   .add("Виноград", "Grapes",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 100.)
                                                .put(CUP, 5.8)
                                                .build())
                                   .add("Голубика", "Blueberry",
                                        new UnitsMapBuilder()
                                                .put(TABLESPOON, 33.3)
                                                .put(TEASPOON, 100.)
                                                .put(CUP, 5.2)
                                                .build())
                                   .add("Ежевика", "Blackberry",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .put(TEASPOON, 100.)
                                                .put(CUP, 6.2)
                                                .build())
                                   .add("Малина", "Raspberries",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 143.)
                                                .put(TABLESPOON, 50.)
                                                .put(TEASPOON, 143.)
                                                .put(CUP, 7.14)
                                                .build())
                                   .add("Лонган", "Longan",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 100.)
                                                .put(CUP, 5.)
                                                .build())
                                   .add("Слива", "Plum",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 29.)
                                                .put(CUP, 5.5)
                                                .build())
                                   .add("Смородина черная",
                                        "Black currant",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .put(TEASPOON, 100.)
                                                .put(CUP, 5.5)
                                                .build())
                                   .add("Смородина красная",
                                        "Red currant",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .put(TEASPOON, 100.)
                                                .put(CUP, 5.5)
                                                .build())
                                   .add("Смородина белая",
                                        "White currant",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .put(TEASPOON, 100.)
                                                .put(CUP, 5.5)
                                                .build())
                                   .add("Грибы сушеные",
                                        "Dried mushrooms",
                                        new UnitsMapBuilder()
                                                .put(TABLESPOON, 100.)
                                                .put(TEASPOON, 250.)
                                                .put(CUP, 10.)
                                                .build())
                                   .add("Пшеница пророщенная",
                                        "Sprouted wheat",
                                        new UnitsMapBuilder()
                                                .put(TABLESPOON, 83.3)
                                                .put(TEASPOON, 333.)
                                                .put(CUP, 4.)
                                                .build())
                                   .build());

        productList.addAll(new CategoryBuilder(FRUITS_VEGETABLES,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{KILOGRAMM, UNITS})
                                                       .build(),
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                                       .build())
                                   .add("Финики",
                                        "Dates",
                                        KILOGRAMM,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 28.)
                                                .build())
                                   .add("Чернослив",
                                        "Prunes",
                                        KILOGRAMM,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 100.)
                                                .build())
                                   .add("Авокадо",
                                        "Avocado",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 4.3)
                                                .build())
                                   .add("Баклажан",
                                        "Eggplant",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 6.7)
                                                .build())
                                   .add("Имбирь корень",
                                        "Root of Ginger",
                                        UNITS, new UnitsListBuilder().build())
                                   .add("Кабачок",
                                        "Marrow",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 2.)
                                                .build())
                                   .add("Капуста белокочанная",
                                        "White cabbage",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Капуста краснокочанная",
                                        "Red cabbage",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 0.67)
                                                .build())
                                   .add("Капуста цветная",
                                        "Cauliflower",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.3)
                                                .build())
                                   .add("Капуста пекинская",
                                        "Chinese cabbage",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.6)
                                                .build())
                                   .add("Морская капуста", "Laminaria")
                                   .add("Капуста брюссельская",
                                        "Brussels sprouts",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 20.)
                                                .build())
                                   .add("Картофель",
                                        "Potatoes",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 10.)
                                                .build())
                                   .add("Кукуруза",
                                        "Corn",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 4.)
                                                .build())
                                   .add("Лук репчатый",
                                        "Onion",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 13.3)
                                                .build())
                                   .add("Лук зеленый", "Green onion")
                                   .add("Морковь",
                                        "Carrot",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 13.3)
                                                .build())
                                   .add("Огурцы",
                                        "Cucumbers",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 9.)
                                                .build())
                                   .add("Перец болгарский", "Paprika",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 10.)
                                                .build())
                                   .add("Редис", "Radish",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 50.)
                                                .build())
                                   .add("Редька", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 11.1)
                                                .build())
                                   .add("Репа", "Turnip",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 5.)
                                                .build())
                                   .add("Свекла", "Beet",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 10.)
                                                .build())
                                   .add("Сельдерей", "Celery",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 2.)
                                                .build())
                                   .add("Спаржа", "Asparagus")
                                   .add("Помидоры", "Tomatoes",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 10.)
                                                .build())
                                   .add("Тыква", "Pumpkin",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 0.5)
                                                .build())
                                   .add("Чеснок", "Garlic", UNITS)
                                   .add("Фрукты", "Fruit")
                                   .add("Айва", "Quince",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 5.8)
                                                .build())
                                   .add("Апельсин", "Orange",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 5.2)
                                                .build())
                                   .add("Арбуз", "Watermelon",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 0.16)
                                                .build())
                                   .add("Банан", "Banana",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 5.)
                                                .build())
                                   .add("Гранат", "Garnet",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 5.)
                                                .build())
                                   .add("Грейпфрут", "Grapefruit",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 5.)
                                                .build())
                                   .add("Груша", "Pear",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 5.8)
                                                .build())
                                   .add("Дыня", "Melon",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 0.77)
                                                .build())
                                   .add("Киви", "Kiwi",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 12.5)
                                                .build())
                                   .add("Кокос", "Coconut",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 0.5)
                                                .build())
                                   .add("Кумкват", "Kumquat",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 33.)
                                                .build())
                                   .add("Лимон", "Lemon",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 6.67)
                                                .build())
                                   .add("Лайм", "Lime",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 10.)
                                                .build())
                                   .add("Манго", "Mango",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 3.3)
                                                .build())
                                   .add("Мандарин", "Mandarin",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 12.5)
                                                .build())
                                   .add("Нектарин", "Nectarine",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 11.)
                                                .build())
                                   .add("Персик", "Peach",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 9.)
                                                .build())
                                   .add("Помело", "Pomelo",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 6.7)
                                                .build())
                                   .add("Яблоко", "Apple",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 6.)
                                                .build())
                                   .add("Хурма", "Persimmon",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 7.7)
                                                .build())
                                   .add("Грибы свежие", "Fresh mushrooms")
                                   .add("Шампиньоны", "Champignon",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 30.)
                                                .build())
                                   .add("Базилик", "Basil", PACKAGE)
                                   .add("Зелень в горшочке", "Green in the pot", PACKAGE)
                                   .add("Мята", "Mint", PACKAGE)
                                   .add("Петрушка", "Parsley", PACKAGE)
                                   .add("Розмарин", "Rosemary", PACKAGE)
                                   .add("Руккола", "Rukkola", PACKAGE)
                                   .add("Салат", "Salad", PACKAGE)
                                   .add("Сельдерей (зелень)", "Celery (greens)", PACKAGE)
                                   .add("Тархун", "Tarhun (greens)", PACKAGE)
                                   .add("Тимьян", "Thyme", PACKAGE)
                                   .add("Укроп", "Dill", PACKAGE)
                                   .add("Фенхель", "Fennel", PACKAGE)
                                   .add("Шпинат", "Spinach", PACKAGE)
                                   .add("Щавель", "Sorrel", PACKAGE)
                                   .add("Оливки", "Olives",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 66.7)
                                                .build())
                                   .add("Маслины", "Black olives",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 66.7)
                                                .build())
                                   .build());

        //        "Бакалея" ProductCategory.GROCERY
        productList.addAll(new CategoryBuilder(GROCERY,
                                               KILOGRAMM,
                                               new UnitsListBuilder()
                                                       .addAll(weightUnitArray)//GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS
                                                       .build())
                                   .add("Сахар белый", "White sugar",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Сахар тростниковый", "Cane sugar",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .build())
                                   .add("Сахар рафинад", "Rafinated sugar",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.)
                                                .build())
                                   .add("Сахарная пудра", "Powdered sugar",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.5)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 40.)
                                                .build())
                                   .add("Соль морская", "Salt sea",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.2)
                                                .put(TEASPOON, 66.7)
                                                .put(TABLESPOON, 28.5)
                                                .build())
                                   .add("Соль поваренная", "Salt",
                                        new UnitsMapBuilder()
                                                .put(CUP, 3.2)
                                                .put(TEASPOON, 90.1)
                                                .put(TABLESPOON, 30.)
                                                .build())
                                   .add("Мука", "Flour",
                                        new UnitsMapBuilder()
                                                .put(CUP, 6.25)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Рис", "Rice",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.2)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Гречневая крупа", "Buckwheat grain",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.76)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Горох", "Peas",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.3)
                                                .put(TEASPOON, 200.1)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Чечевица", "Lentil",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.7)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Манная крупа", "Semolina",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Кус-Кус", "Kus-Kus",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.5)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Кукурузная крупа", "Cornmeal",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.7)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Овес", "Oats",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.5)
                                                .put(TEASPOON, 333.3)
                                                .put(TABLESPOON, 83.3)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())

                                   .add("Перловка", "Pearl barley",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.3)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Полба", "Polba",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.)
                                                .put(TEASPOON, 333.3)
                                                .put(TABLESPOON, 83.3)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Пшено", "Millet",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.5)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Пшеница", "Wheat",
                                        new UnitsMapBuilder()
                                                .put(CUP, 4.5)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 40.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .add("Ячмень", "Barley",
                                        new UnitsMapBuilder()
                                                .put(CUP, 5.5)
                                                .put(TEASPOON, 200.)
                                                .put(TABLESPOON, 50.)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .build())
                                   .build());

        productList.addAll(new CategoryBuilder(GROCERY,
                                               PACKAGE,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                                       .build())
                                   .add("Заменитель сахара", "Sugar substitute",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Фруктоза", "Fructose",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Хлопья овсяные", "Oat flakes",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Хлопья кукурузные", "Corn flakes",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Хлопья смесь злаков", "Mix of cereals flakes",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.325)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Хлопья гречневые", "Buckwheat flakes",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Каша-минутка", null, new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{PACKAGE, UNITS})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Мюсли", "Muesli",
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Смесь для выпечки", "Mix for baking", new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{PACKAGE, UNITS})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Макароны", "Pasta",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Бантики", "Farfalloni",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Вермишель", "Vermicelli",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Витки", "Cavatappi",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Гнезда", "Barbina",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Гребешки", "Creste di galli",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Листы для лазаньи", "Lasagne",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Лапша", null,
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Перья", null,
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Ракушки", null,
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Рожки", null,
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Спагетти", "Spaghetti",
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Спирали", null,
                                        new UnitsMapBuilder()
                                                .put(KILOGRAMM, 0.5)
                                                .put(UNITS, 1.)
                                                .build())
                                   .build());

        productList.addAll(new CategoryBuilder(GROCERY,
                                               PACKAGE,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{PACKAGE, UNITS})
                                                       .build())
                                   .add("Чипсы", "Chips",
                                        new UnitsMapBuilder()
                                                .build())
                                   .add("Попкорн", "Popcorn",
                                        new UnitsMapBuilder()
                                                .build())
                                   .add("Снэки", "Snacks",
                                        new UnitsMapBuilder()
                                                .build())
                                   .add("Суп быстрого приготовления", "Instant soup",
                                        new UnitsMapBuilder()
                                                .build())
                                   .add("Лапша быстрого приготовления", "Instant noodles",
                                        new UnitsMapBuilder()
                                                .build())
                                   .build());

        //        "Мясная гастрономия" ProductCategory.MEAT_GARSTRONOMY
        productList.addAll(new CategoryBuilder(MEAT_GARSTRONOMY,
                                               KILOGRAMM,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE})
                                                       .build())
                                   .add("Сосиски", null,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE, UNITS})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 20.)
                                                .build())
                                   .add("Cардельки", null,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE, UNITS})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 10.)
                                                .build())
                                   .add("Колбаски", null,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE, UNITS})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 10.)
                                                .build())
                                   .add("Колбаса вареная", null)
                                   .add("Колбаса копченая", null)
                                   .add("Колбаса", null)
                                   .add("Балык", null)
                                   .add("Бастурма", null)
                                   .add("Бекон", null)
                                   .add("Буженина", null)
                                   .add("Ветчина", null)
                                   .add("Грудинка копченая", null)
                                   .add("Корейка копченая", null)
                                   .add("Окорок копченый", null)
                                   .add("Ребрышки копченые", null)
                                   .add("Рулет мясной", null)
                                   .add("Рулька", null)
                                   .add("Сало", null)
                                   .add("Шейка копченая", null)
                                   .add("Шпик", null)
                                   .add("Паштет", null)
                                   .add("Ливер", null)
                                   .build());

        //        "Мясо и птица" ProductCategory.MEAT_BIRDS
        productList.addAll(new CategoryBuilder(MEAT_BIRDS,
                                               KILOGRAMM,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE})
                                                       .build())
                                   .add("Бедро птицы", null)
                                   .add("Голень птицы", null)
                                   .add("Грудка птицы", null)
                                   .add("Крылья птицы", null)
                                   .add("Окорочок птицы", null)
                                   .add("Тушка птицы", null)
                                   .add("Филе птицы", null)
                                   .add("Зразы (полуфабрикат)", null)
                                   .add("Колбаски (полуфабрикат)", null)
                                   .add("Котлеты (полуфабрикат)", null)
                                   .add("Крылья (полуфабрикат)", null)
                                   .add("Купаты (полуфабрикат)", null)
                                   .add("Медальоны (полуфабрикат)", null)
                                   .add("Фарш (полуфабрикат)", null)
                                   .add("Стейк (полуфабрикат)", null)
                                   .add("Мясные Шарики (полуфабрикат)", null)
                                   .add("Шашлык (полуфабрикат)", null)
                                   .add("Шницель (полуфабрикат)", null)
                                   .add("Желудки (полуфабрикат)", null)
                                   .add("Набор суповой (полуфабрикат)", null)
                                   .add("Печень (полуфабрикат)", null)
                                   .add("Почки (полуфабрикат)", null)
                                   .add("Рубец (полуфабрикат)", null)
                                   .add("Сердце (полуфабрикат)", null)
                                   .add("Язык (полуфабрикат)", null)
                                   .add("Баранина", null)
                                   .add("Говядина", null)
                                   .add("Кролик", null)
                                   .add("Свинина", null)
                                   .add("Телятина", null)
                                   .add("Утка", null)
                                   .add("Курица", null)
                                   .build());

        //        "Замороженные продукты" ProductCategory.FROZEN_FOOD
        productList.addAll(new CategoryBuilder(FROZEN_FOOD,
                                               KILOGRAMM,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE})
                                                       .build())
                                   .add("Мясо замороженное", null)
                                   .add("Птица замороженное", null)
                                   .add("Морепродукты замороженные", null)
                                   .build());

        //        "Рыба, морепродукты и икра" ProductCategory.FISH_SEAFOOD_CAVIAR
        productList.addAll(new CategoryBuilder(FISH_SEAFOOD_CAVIAR,
                                               KILOGRAMM,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE, UNITS})
                                                       .build())
                                   .add("Рыба", "Fish")
                                   .add("Крабовое мясо, палочки", null)
                                   .add("Икра красная", null)
                                   .add("Пресервы", null)
                                   .add("Рыба соленая", null)
                                   .add("Рыба копченая", null)
                                   .add("Рыба вяленая", null)
                                   .add("Рыба сушеная", null)
                                   .add("Гребешки", null)
                                   .add("Кальмар", null)
                                   .add("Креветки", null)
                                   .add("Мидии", null)
                                   .add("Морской коктейль", null)
                                   .add("Улитки", null)
                                   .add("Горбуша", null)
                                   .add("Дорада", null)
                                   .add("Камбала", null)
                                   .add("Карась", null)
                                   .add("Карп", null)
                                   .add("Кета", null)
                                   .add("Кижуч", null)
                                   .add("Лосось", null)
                                   .add("Минтай", null)
                                   .add("Мойва", null)
                                   .add("Окунь", null)
                                   .add("Осетр", null)
                                   .add("Палтус", null)
                                   .add("Путассу", null)
                                   .add("Пангасиус", null)
                                   .add("Сибас", null)
                                   .add("Скумбрия", null)
                                   .add("Стерлядь", null)
                                   .add("Тилапия", null)
                                   .add("Судак", null)
                                   .add("Толстолобик", null)
                                   .add("Треска", null)
                                   .add("Тунец", null)
                                   .add("Форель", null)
                                   .add("Щука", null)
                                   .add("Хек", null)
                                   .build());

        //        "Растительные масла, соусы и приправы" ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS
        productList.addAll(new CategoryBuilder(VEGETABLE_OIL_SAUCE_CONDIMENTS,
                                               PACKAGE,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{UNITS, PACKAGE})//GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS
                                                       .build())
                                   .add("Лавровый лист", null)
                                   .build());

        productList.addAll(new CategoryBuilder(VEGETABLE_OIL_SAUCE_CONDIMENTS,
                                               LITRE,
                                               new UnitsListBuilder()
                                                       .addAll(volumeUnitArray)//LITRE, MILILITRE, CUP, TEASPOON, TABLESPOON, PACKAGE, BOTTLE
                                                       .build())
                                   .add("Масло подсолнечное", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .put(BOTTLE, 1.)
                                                .build())
                                   .add("Масло растительное", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .put(BOTTLE, 1.)
                                                .build())
                                   .add("Масло оливковое", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(UNITS, 1.)
                                                .put(PACKAGE, 1.)
                                                .put(BOTTLE, 1.)
                                                .build())
                                   .add("Масло кунжутное", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .build())
                                   .add("Масло льняное", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .build())
                                   .add("Томатная паста", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .build())
                                   .add("Кетчуп", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .build())
                                   .add("Соус", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .build())
                                   .add("Аджика", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .build())
                                   .build());

        productList.addAll(new CategoryBuilder(VEGETABLE_OIL_SAUCE_CONDIMENTS,
                                               PACKAGE,
                                               new UnitsListBuilder()
                                                       .addAll(weightUnitArray)//GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS
                                                       .build())
                                   .add("Приправа", null)
                                   .add("Панировочные сухари", null)
                                   .add("Ароматизатор", null)
                                   .add("Желе", null)
                                   .add("Коржи", null)
                                   .add("Мастика", null)
                                   .add("Глазурь", null)
                                   .add("Дрожжи", null)
                                   .add("Посыпка кондитерская", null)
                                   .add("Желатин", null)
                                   .build());


        productList.addAll(new CategoryBuilder(VEGETABLE_OIL_SAUCE_CONDIMENTS,
                                               KILOGRAMM,
                                               new UnitsListBuilder()
                                                       .addAll(weightUnitArray)//GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS
                                                       .build())
                                   .add("Корица", null,
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(TEASPOON, 125.)
                                                .put(TABLESPOON, 50.)
                                                .build())
                                   .add("Кокосовая стружка", null,
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(TEASPOON, 143.)
                                                .put(TABLESPOON, 50.)
                                                .put(CUP, 7.1)
                                                .build())
                                   .add("Крахмал", null,
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 5.5)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .put(PACKAGE, 5.)
                                                .build())
                                   .add("Какао-порошок", null,
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(TEASPOON, 83.3)
                                                .put(TABLESPOON, 200.)
                                                .put(PACKAGE, 4.)
                                                .build())
                                   .add("Разрыхлитель", null,
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 6.25)
                                                .put(TEASPOON, 100.)
                                                .put(TABLESPOON, 33.3)
                                                .put(PACKAGE, 12.5)
                                                .build())
                                   .add("Лимонный сок", null,
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 4.)
                                                .put(TEASPOON, 200.)
                                                .put(TABLESPOON, 55.5)
                                                .put(PACKAGE, 8.)
                                                .build())
                                   .add("Уксус столовый", null,
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 4.)
                                                .put(TEASPOON, 200.)
                                                .put(TABLESPOON, 66.7)
                                                .build())
                                   .add("Сода пищевая", null,
                                        new UnitsMapBuilder(kilogramUnitMap)
                                                .put(CUP, 4.16)
                                                .put(TEASPOON, 711.)
                                                .put(TABLESPOON, 25.)
                                                .build())
                                   .add("Декор для выпечки", null,
                                        UNITS, new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                                .build())
                                   .build());

        //        "Чай, кофе, какао" ProductCategory.TEA_COFFEE_CACAO
        productList.addAll(new CategoryBuilder(TEA_COFFEE_CACAO,
                                               PACKAGE,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{PACKAGE, UNITS})
                                                       .build())
                                   .add("Чай", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Кофе", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Какао растворимый", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Горячий шоколад", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Цикорий", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .build());

        //        "Консервация" ProductCategory.CONSERVATION
        productList.addAll(new CategoryBuilder(CONSERVATION,
                                               PACKAGE,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                                       .build())
                                   .add("Мандарины в собственном соку", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Горошек консервированный", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Кукуруза консервированная", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Огурцы соленые", null,
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 9.)
                                                .build())
                                   .add("Огурцы маринованные", null,
                                        KILOGRAMM,
                                        new UnitsListBuilder()
                                                .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                                .build(),
                                        new UnitsMapBuilder()
                                                .put(UNITS, 9.)
                                                .build())
                                   .add("Томаты консервированные", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Оливки (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Маслины (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Фасоль (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Горбуша (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Кальмар (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Кета (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Килька (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Лосось (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Мидии (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Морская капуста (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Пыжьян (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Сайра (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Сардина (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Семга (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Скумбрия (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Тунец (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Треска (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Форель (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Шпроты (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Язь (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Паштет (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Тушенка (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Сливки (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Белые грибы (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Грузди (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Маслята (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Моховики (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Опята (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Рыжики (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Шампиньоны (консервы)", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Ягоды протертые", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Фрукты в сиропе", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .build());

        productList.addAll(new CategoryBuilder(CONSERVATION,
                                               LITRE,
                                               new UnitsListBuilder()
                                                       .addAll(volumeUnitArray)//LITRE, MILILITRE, CUP, TEASPOON, TABLESPOON, PACKAGE, BOTTLE
                                                       .build())
                                   .add("Сгущенка вареная", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(PACKAGE, 2.5)
                                                .put(UNITS, 2.5)
                                                .build())
                                   .add("Сгущенка с добавками", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(PACKAGE, 2.5)
                                                .put(UNITS, 2.5)
                                                .build())
                                   .add("Сгущенка цельная", null,
                                        new UnitsMapBuilder(litreUnitMap)
                                                .put(PACKAGE, 2.5)
                                                .put(UNITS, 2.5)
                                                .build())
                                   .add("Мед гречишный", null)
                                   .add("Мед липовый", null)
                                   .add("Мед цветочный", null)
                                   .add("Курд лимонный", null)
                                   .add("Варенье Абрикос", null)
                                   .add("Варенье Брусника", null)
                                   .add("Варенье Вишня", null)
                                   .add("Варенье Клубника", null)
                                   .add("Варенье Клюква", null)
                                   .add("Варенье Крыжовник", null)
                                   .add("Варенье Малина", null)
                                   .add("Варенье Смородина", null)
                                   .add("Варенье Черника", null)
                                   .add("Джем Абрикос", null)
                                   .add("Джем Брусника", null)
                                   .add("Джем Вишня", null)
                                   .add("Джем Клубника", null)
                                   .add("Джем Клюква", null)
                                   .add("Джем Крыжовник", null)
                                   .add("Джем Малина", null)
                                   .add("Джем Смородина", null)
                                   .add("Джем Черника", null)
                                   .add("Компот Абрикос", null)
                                   .add("Компот Брусника", null)
                                   .add("Компот Вишня", null)
                                   .add("Компот Клубника", null)
                                   .add("Компот Клюква", null)
                                   .add("Компот Крыжовник", null)
                                   .add("Компот Малина", null)
                                   .add("Компот Смородина", null)
                                   .add("Компот Черника", null)
                                   .add("Сироп", null)
                                   .build());

        //        "Мороженое" ProductCategory.ICE_CREAM
        productList.addAll(new CategoryBuilder(ICE_CREAM,
                                               PACKAGE,
                                               new UnitsListBuilder()
                                                       .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                                       .build())
                                   .add("Мороженое", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .add("Пломбир", null,
                                        new UnitsMapBuilder()
                                                .put(UNITS, 1.)
                                                .build())
                                   .build());

        //        "Товары для животных" ProductCategory.ANIMALS_PRODUCTS
        //        list.add(new Product(PACKAGE, "Корм для животных", ANIMALS_PRODUCTS));
        //        list.add(new Product(PACKAGE, "Корм для животных влажный", ANIMALS_PRODUCTS));
        //        list.add(new Product(PACKAGE, "Корм для животных сухой", ANIMALS_PRODUCTS));
        //        list.add(new Product(UNITS, "Лакомства для животных", ANIMALS_PRODUCTS));
        //        list.add(new Product(UNITS, "Консервы для животных", ANIMALS_PRODUCTS));
        //        list.add(new Product(PACKAGE, "Витамины для животных", ANIMALS_PRODUCTS));
        //        list.add(new Product(UNITS, "Миска", ANIMALS_PRODUCTS));
        //        list.add(new Product(UNITS, "Игрушка", ANIMALS_PRODUCTS));
        //        list.add(new Product(UNITS, "Туалет", ANIMALS_PRODUCTS));
        //        list.add(new Product(UNITS, "Поводок", ANIMALS_PRODUCTS));
        //        list.add(new Product(UNITS, "Когтеточка", ANIMALS_PRODUCTS));
        //        list.add(new Product(UNITS, "Лежанка", ANIMALS_PRODUCTS));
        //        list.add(new Product(PACKAGE, "Шампунь для животных", ANIMALS_PRODUCTS));
        //        list.add(new Product(PACKAGE, "Наполнитель для кошачьего туалета", ANIMALS_PRODUCTS));
        //

        //        "Диабетическое питание", ProductCategory.DIABETIC_NUTRITION

        //        "Готовая кулинария", ProductCategory.COOKED_FOOD

        //        "Бытовая химия и товары для дома" ProductCategory.HOUSEHOLD_CHEMICALS
        //        list.add(new Product(PACKAGE, "Пломбир", HOUSEHOLD_CHEMICALS));

        //        "Косметика и гигиена", R.color.category_d

        //        "Игры и игрушки", R.color.category_orange

        //        "Электротовары и бытовая техника", R.colo
        //        "Товары для ремонта, дачи и отдыха", R.co
        //        "Автотовары", R.color.category_dark_deep_
        //        "Гардероб", R.color.category_brown, 28));
        //        "Канцелярские товары", R.color.category_d
        //        "Книги и печатная продукция", R.color.cat
        //        "Всё для сада и огорода", R.color.categor
        //Алкогольные напитки ProductCategory.ALCOHOL_DRINKS
        //        list.addAll(new CategoryBuilder(ALCOHOL_DRINKS, BOTTLE)
        //                            .add("Пиво", new UnitsListBuilder().add(LITRE))
        //                            .add("Водка")
        //                            .add("Коньяк")
        //                            .add("Виски")
        //                            .add("Ром")
        //                            .add("Текила")
        //                            .add("Джин")
        //                            .add("Самбука")
        //                            .add("Граппа")
        //                            .add("Ликер")
        //                            .add("Настойка")
        //                            .add("Бальзам")
        //                            .add("Вино")
        //                            .add("Шампанское, игристое вино")
        //                            .add("Вермут")
        //                            .add("Вермут")
        //                            .add("Коктейль")
        //                            .add("Сидр")
        //                            .add("Квас")
        //                            .build());
        return productList;
    }

    private static class CategoryBuilder {
        private final List<Product> productList;
        private final ProductCategory productCategory;
        private final List<MeasureUnit> defaultMainUnitList;
        private final List<MeasureUnit> defaultUnitList;


        public CategoryBuilder(ProductCategory productCategory, MeasureUnit defaultUnit) {
            this(productCategory, defaultUnit, new LinkedList<>());
        }

        private CategoryBuilder(ProductCategory productCategory,
                                MeasureUnit defaultUnit,
                                List<MeasureUnit> defaultUnitList) {
            this.productCategory = productCategory;
            this.defaultMainUnitList = Collections.singletonList(defaultUnit);
            this.defaultUnitList = defaultUnitList;
            this.productList = new LinkedList<>();
        }

        private CategoryBuilder(ProductCategory productCategory,
                                List<MeasureUnit> defaultMainUnitList,
                                List<MeasureUnit> defaultUnitList) {
            this.productCategory = productCategory;
            this.defaultMainUnitList = new LinkedList<>(defaultMainUnitList);
            this.defaultUnitList = new LinkedList<>(defaultUnitList);
            this.productList = new LinkedList<>();
        }

        public CategoryBuilder add(String rusName, String engName) {
            add(rusName, engName, defaultMainUnitList, defaultUnitList, new HashMap<>());
            return this;
        }

        public CategoryBuilder add(String rusName, String engName, MeasureUnit unit) {
            add(rusName, engName, unit, defaultUnitList, new HashMap<>());
            return this;
        }

        public CategoryBuilder add(String rusName, String engName, Map<MeasureUnit, Double> map) {
            add(rusName, engName, defaultMainUnitList, defaultUnitList, map);
            return this;
        }


        public CategoryBuilder add(String rusName, String engName,
                                   List<MeasureUnit> measureUnitList,
                                   Map<MeasureUnit, Double> map) {
            add(rusName, engName, defaultMainUnitList, measureUnitList, map);
            return this;
        }

        public CategoryBuilder add(String rusName, String engName,
                                   MeasureUnit unit,
                                   Map<MeasureUnit, Double> map) {
            add(rusName, engName, unit, defaultUnitList, map);
            return this;
        }

        public CategoryBuilder add(String rusName, String engName,
                                   MeasureUnit unit,
                                   List<MeasureUnit> measureUnitList) {
            add(rusName, engName, unit, measureUnitList, new HashMap<>());
            return this;
        }

        public CategoryBuilder add(String rusName, String engName,
                                   MeasureUnit unit,
                                   List<MeasureUnit> measureUnitList,
                                   Map<MeasureUnit, Double> map) {
            add(rusName, engName, Collections.singletonList(unit), measureUnitList, map);
            return this;
        }

        public CategoryBuilder add(String rusName, String engName,
                                   List<MeasureUnit> mainUnits,
                                   List<MeasureUnit> measureUnitList,
                                   Map<MeasureUnit, Double> map) {
            Product product = new Product(productCategory,
                                          rusName,
                                          engName,
                                          mainUnits,
                                          measureUnitList,
                                          map, null);
            productList.add(product);
            //            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            //            DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
            //            productRef.push().setValue(product, (databaseError, databaseReference) -> {
            //                //                product.fillTheMap(map);
            //                //                productRef.child(databaseReference.getKey()).child(DatabaseConstants.DATABASE_MEASURE_MAP_FIELD)
            //                //                        .setValue(product.getMeasureStringToAmoutMap());
            //            });
            return this;
        }

        public List<Product> build() {
            return productList;
        }
    }

    private static class UnitsMapBuilder {
        private final Map<MeasureUnit, Double> measureUnitToAmoutMap;

        public UnitsMapBuilder() {
            measureUnitToAmoutMap = new HashMap<>();
        }

        public UnitsMapBuilder(Map<MeasureUnit, Double> map) {
            measureUnitToAmoutMap = new HashMap<>(map);
        }

        public UnitsMapBuilder put(MeasureUnit unit, Double amount) {
            measureUnitToAmoutMap.put(unit, amount);
            return this;
        }

        public Map<MeasureUnit, Double> build() {
            return measureUnitToAmoutMap;
        }
    }

    private static class UnitsListBuilder {
        private final List<MeasureUnit> measureUnitList;

        public UnitsListBuilder() {
            measureUnitList = new LinkedList<>();
        }

        public UnitsListBuilder add(MeasureUnit unit) {
            measureUnitList.add(unit);
            return this;
        }

        public UnitsListBuilder addAll(MeasureUnit[] unit) {
            measureUnitList.addAll(Arrays.asList(unit));
            return this;
        }

        public List<MeasureUnit> build() {
            return measureUnitList;
        }
    }
}