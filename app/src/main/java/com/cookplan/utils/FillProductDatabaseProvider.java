package com.cookplan.utils;

import com.cookplan.RApplication;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public static void fillDatabase() {
        fillProductDatabase();
    }

    private static void fillProductDatabase() {
        saveProductList();
        RApplication.savePriorityList(Arrays.asList(ProductCategory.values()));
    }

    private static void saveProductList() {
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

        new CategoryBuilder(WITHOUT_CATEGORY,
                            LITRE,
                            new UnitsListBuilder()
                                    .addAll(volumeUnitArray)
                                    .build())
                .add("Вода",
                     new UnitsMapBuilder(litreUnitMap)
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build());

        //Молочные продукты ProductCategory.MILK_PRODUCT
        new CategoryBuilder(MILK_PRODUCT,
                            LITRE,
                            new UnitsListBuilder()
                                    .addAll(volumeUnitArray)
                                    .build())
                .add("Молоко",
                     new UnitsMapBuilder(litreUnitMap)
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Молочный коктейль",
                     new UnitsMapBuilder(litreUnitMap)
                             .put(PACKAGE, 1.5)
                             .put(BOTTLE, 1.5)
                             .put(UNITS, 1.5)
                             .build())
                .add("Сливки",
                     new UnitsMapBuilder(litreUnitMap)
                             .put(PACKAGE, 0.33)
                             .put(UNITS, 0.33)
                             .put(BOTTLE, 0.33)
                             .build())
                .add("Сливки взбитые",
                     PACKAGE,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{MILILITRE, GRAMM, PACKAGE, BOTTLE})
                             .build(),
                     new UnitsMapBuilder()
                             .put(MILILITRE, 250.)
                             .put(GRAMM, 250.)
                             .build())
                .add("Йогурт питьевой",
                     PACKAGE,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{LITRE, PACKAGE})
                             .build(),
                     new UnitsMapBuilder()
                             .put(LITRE, 1.)
                             .build())
                .add("Сырок творожный",
                     UNITS,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{UNITS})
                             .build())
                .add("Кефир",
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
                     KILOGRAMM,
                     new UnitsListBuilder()
                             .addAll(weightUnitArray)
                             .build(),
                     new UnitsMapBuilder()
                             .put(CUP, 7.4)
                             .build())
                .add("Ацидофилин",
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

                .add("Бифилайф",
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
                     PACKAGE,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{PACKAGE})
                             .build())
                .add("Пудинг",
                     PACKAGE,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{PACKAGE})
                             .build())
                .add("Смусси молочный",
                     PACKAGE,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{PACKAGE})
                             .build())
                .add("Каша рисовая молочная",
                     PACKAGE,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, UNITS, PACKAGE})
                             .build())
                .add("Каша овсяная молочная",
                     PACKAGE,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, UNITS, PACKAGE})
                             .build());

        //Cыры ProductCategory.CHEESE
        new CategoryBuilder(CHEESE,
                            KILOGRAMM,
                            new UnitsListBuilder()
                                    .addAll(weightUnitArray)
                                    .build())
                .add("Сыр твердый натертый",
                     new UnitsListBuilder()
                             .addAll(weightUnitArray)
                             .build(),
                     new UnitsMapBuilder(kilogramUnitMap)
                             .put(CUP, 8.3)
                             .build())
                .add("Сыр твердый",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build())
                .add("Моцарелла",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build())
                .add("Сыр творожный",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build())
                .add("Маскарпоне",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build())
                .add("Рикотта",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build())
                .add("Сыр плавленый",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build())
                .add("Сыр копченый",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build())
                .add("Брынза",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build())
                .add("Сулугуни",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build())
                .add("Сырок плавленный",
                     PACKAGE,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Сыр деликатесный",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .build());

        //Яйца ProductCategory.EGGS
        new CategoryBuilder(EGGS,
                            UNITS,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                    .build())
                .add("Яйцо куриное",
                     new UnitsMapBuilder()
                             .build())
                .add("Яйцо перепелиное",
                     new UnitsMapBuilder()
                             .build());
        //Кондитерские изделия ProductCategory.CONFECTIONERY
        new CategoryBuilder(CONFECTIONERY,
                            PACKAGE,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{PACKAGE, GRAMM, KILOGRAMM, UNITS})
                                    .build())
                .add("Шоколад горький",
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
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Шоколадная паста",
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
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Яйцо шоколадное",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Рулет кондитерский",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Кекс",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Сахарная вата",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Круассан", UNITS)
                .add("Штрудель", UNITS)
                .add("Пирог", UNITS)
                .add("Пирожное", UNITS)
                .add("Торт", UNITS)
                .add("Шарлотка", UNITS)
                .add("Жевательная резинка", UNITS)
                .add("Козинак", UNITS)
                .add("Тарталетки", UNITS)
                .add("Кукурузные палочки");
        new CategoryBuilder(CONFECTIONERY,
                            KILOGRAMM,
                            new UnitsListBuilder()
                                    .addAll(weightUnitArray)
                                    .build())
                .add("Драже")
                .add("Конфеты")
                .add("Трюфели")
                .add("Глазированные фрукты и орехи")
                .add("Печенье")
                .add("Вафли")
                .add("Зефир")
                .add("Мармелад")
                .add("Маршмеллоу")
                .add("Пастила")
                .add("Суфле")
                .add("Кунафа")
                .add("Пахлава")
                .add("Рахат-лукум")
                .add("Халва")
                .add("Чак-чак")
                .add("Щербет");

        //Детские товары" ProductCategory.CHILD_PRODUCTS
        new CategoryBuilder(CHILD_PRODUCTS,
                            UNITS,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                    .build())
                .add("Детское питание",
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{UNITS, PACKAGE, BOTTLE})
                             .build(),
                     new UnitsMapBuilder()
                             .put(BOTTLE, 1.)
                             .build())
                .add("Пюре детское",
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{UNITS, PACKAGE, BOTTLE})
                             .build(),
                     new UnitsMapBuilder()
                             .put(BOTTLE, 1.)
                             .build())
                .add("Подгузники")
                .add("Пеленки")
                .add("Бутылочка детская")
                .add("Кружка детская")
                .add("Поильник детский")
                .add("Пустышка детская")
                .add("Соска детская")
                .add("Ложка детская")
                .add("Тарелка детская");

        //        Напитки, соки ProductCategory.DRINKS_JUICE
        new CategoryBuilder(DRINKS_JUICE,
                            LITRE,
                            new UnitsListBuilder()
                                    .addAll(volumeUnitArray)
                                    .build())
                .add("Вода питьевая",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Кисель",
                     PACKAGE,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Газировка",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.5)
                             .put(BOTTLE, 1.5)
                             .build())
                .add("Вода минеральная",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Лимонад",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Тархун",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Компот",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Морс",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Сок",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Нектар",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .put(BOTTLE, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Квас",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.5)
                             .put(BOTTLE, 1.5)
                             .put(PACKAGE, 1.5)
                             .build())
                .add("Холодный чай (черный)",
                     new UnitsMapBuilder()
                             .build())
                .add("Холодный чай (зеленый)",
                     new UnitsMapBuilder()
                             .build());

        //        Хлебобулочные изделия" ProductCategory.BAKERY_PRODUCTS
        new CategoryBuilder(BAKERY_PRODUCTS,
                            UNITS,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                                    .build())
                .add("Хлеб белый",
                     new UnitsMapBuilder()
                             .put(PACKAGE, 1.)
                             .build())
                .add("Хлеб для тостов и сэндвичей",
                     new UnitsMapBuilder()
                             .put(PACKAGE, 1.)
                             .build())
                .add("Хлеб зерновой",
                     new UnitsMapBuilder()
                             .put(PACKAGE, 1.)
                             .build())
                .add("Хлебцы",
                     new UnitsMapBuilder()
                             .put(PACKAGE, 1.)
                             .build())
                .add("Хлеб черный",
                     new UnitsMapBuilder()
                             .put(PACKAGE, 1.)
                             .build())
                .add("Хлеб кукурузный",
                     new UnitsMapBuilder()
                             .put(PACKAGE, 1.)
                             .build())
                .add("Сухарики",
                     PACKAGE,
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Баранки",
                     KILOGRAMM,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                             .build())
                .add("Соломка",
                     PACKAGE,
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Лепешка",
                     new UnitsMapBuilder()
                             .put(PACKAGE, 1.)
                             .build())
                .add("Сушки",
                     KILOGRAMM,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                             .build())
                .add("Сухари",
                     KILOGRAMM,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                             .build())
                .add("Хлебные палочки",
                     PACKAGE,
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Сдобная выпечка")
                .add("Слоеная выпечка")
                .add("Песочная выпечка");

        //        Овощи, фрукты, грибы, ягоды ProductCategory.
        new CategoryBuilder(FRUITS_VEGETABLES,
                            KILOGRAMM,
                            new UnitsListBuilder()
                                    .addAll(weightUnitArray)//GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS
                                    .build())
                .add("Арахис",
                     new UnitsMapBuilder()
                             .put(CUP, 5.7)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .build())
                .add("Грецкий орех очищенный",
                     new UnitsMapBuilder()
                             .put(CUP, 5.8)
                             .put(TEASPOON, 151.)
                             .put(TABLESPOON, 50.)
                             .build())
                .add("Кедровый орех",
                     new UnitsMapBuilder()
                             .put(CUP, 5.2)
                             .put(TEASPOON, 100.)
                             .put(TABLESPOON, 33.3)
                             .build())
                .add("Кешью",
                     new UnitsMapBuilder()
                             .put(CUP, 5.)
                             .put(TEASPOON, 90.)
                             .put(TABLESPOON, 28.5)
                             .build())
                .add("Миндаль",
                     new UnitsMapBuilder()
                             .put(CUP, 6.2)
                             .put(TEASPOON, 100.)
                             .put(TABLESPOON, 33.3)
                             .build())
                .add("Изюм",
                     new UnitsMapBuilder()
                             .put(CUP, 5.7)
                             .put(TEASPOON, 143.)
                             .put(TABLESPOON, 40.)
                             .build())
                .add("Курага",
                     new UnitsMapBuilder()
                             .put(CUP, 6.7)
                             .build())
                .add("Смесь орехов",
                     new UnitsMapBuilder()
                             .put(CUP, 6.)
                             .build())
                .add("Фундук",
                     new UnitsMapBuilder()
                             .put(CUP, 5.8)
                             .put(TEASPOON, 100.)
                             .put(TABLESPOON, 33.3)
                             .build())
                .add("Фисташки",
                     new UnitsMapBuilder()
                             .put(CUP, 5.8)
                             .put(TEASPOON, 100.)
                             .put(TABLESPOON, 33.3)
                             .build())
                .add("Семечки подсолнечные",
                     new UnitsMapBuilder()
                             .put(TABLESPOON, 40.)
                             .put(TEASPOON, 130.)
                             .put(CUP, 5.7)
                             .build())
                .add("Капуста квашеная",
                     new UnitsMapBuilder()
                             .put(TEASPOON, 100.)
                             .put(TABLESPOON, 33.3)
                             .put(CUP, 5.8)
                             .build())
                .add("Семечки тыквенные",
                     new UnitsMapBuilder()
                             .put(TABLESPOON, 50.)
                             .put(TEASPOON, 150.)
                             .put(CUP, 5.8)
                             .build())
                .add("Имбирь молотый",
                     new UnitsMapBuilder()
                             .put(TABLESPOON, 100.)
                             .put(TEASPOON, 333.)
                             .build())
                .add("Ягоды",
                     new UnitsMapBuilder()
                             .put(UNITS, 100.)
                             .put(CUP, 6.2)
                             .build())
                .add("Фасоль",
                     new UnitsMapBuilder()
                             .put(CUP, 4.5)
                             .build())
                .add("Смесь орехов и сухофруктов")
                .add("Сухофрукты")
                .add("Ягоды замороженные",
                     new UnitsMapBuilder()
                             .put(CUP, 5.)
                             .build())
                .add("Овощное ассорти")
                .add("Виноград",
                     new UnitsMapBuilder()
                             .put(UNITS, 100.)
                             .put(CUP, 5.8)
                             .build())
                .add("Голубика",
                     new UnitsMapBuilder()
                             .put(TABLESPOON, 33.3)
                             .put(TEASPOON, 100.)
                             .put(CUP, 5.2)
                             .build())
                .add("Ежевика",
                     new UnitsMapBuilder()
                             .put(UNITS, 100.)
                             .put(TABLESPOON, 33.3)
                             .put(TEASPOON, 100.)
                             .put(CUP, 6.2)
                             .build())
                .add("Малина",
                     new UnitsMapBuilder()
                             .put(UNITS, 143.)
                             .put(TABLESPOON, 50.)
                             .put(TEASPOON, 143.)
                             .put(CUP, 7.14)
                             .build())
                .add("Лонган",
                     new UnitsMapBuilder()
                             .put(UNITS, 100.)
                             .put(CUP, 5.)
                             .build())
                .add("Слива",
                     new UnitsMapBuilder()
                             .put(UNITS, 29.)
                             .put(CUP, 5.5)
                             .build())
                .add("Смородина черная",
                     new UnitsMapBuilder()
                             .put(UNITS, 100.)
                             .put(TABLESPOON, 33.3)
                             .put(TEASPOON, 100.)
                             .put(CUP, 5.5)
                             .build())
                .add("Смородина красная",
                     new UnitsMapBuilder()
                             .put(UNITS, 100.)
                             .put(TABLESPOON, 33.3)
                             .put(TEASPOON, 100.)
                             .put(CUP, 5.5)
                             .build())
                .add("Смородина белая",
                     new UnitsMapBuilder()
                             .put(UNITS, 100.)
                             .put(TABLESPOON, 33.3)
                             .put(TEASPOON, 100.)
                             .put(CUP, 5.5)
                             .build())
                .add("Грибы сушеные",
                     new UnitsMapBuilder()
                             .put(TABLESPOON, 100.)
                             .put(TEASPOON, 250.)
                             .put(CUP, 10.)
                             .build())
                .add("Пшеница пророщенная",
                     new UnitsMapBuilder()
                             .put(TABLESPOON, 83.3)
                             .put(TEASPOON, 333.)
                             .put(CUP, 4.)
                             .build());

        new CategoryBuilder(FRUITS_VEGETABLES,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{KILOGRAMM, UNITS})
                                    .build(),
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                    .build())
                .add("Финики",
                     KILOGRAMM,
                     new UnitsMapBuilder()
                             .put(UNITS, 28.)
                             .build())
                .add("Чернослив",
                     KILOGRAMM,
                     new UnitsMapBuilder()
                             .put(UNITS, 100.)
                             .build())
                .add("Авокадо",
                     new UnitsMapBuilder()
                             .put(UNITS, 4.3)
                             .build())
                .add("Баклажан",
                     new UnitsMapBuilder()
                             .put(UNITS, 6.7)
                             .build())
                .add("Имбирь корень", UNITS, new UnitsListBuilder().build())
                .add("Кабачок",
                     new UnitsMapBuilder()
                             .put(UNITS, 2.)
                             .build())
                .add("Капуста белокочанная",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Капуста краснокочанная",
                     new UnitsMapBuilder()
                             .put(UNITS, 0.67)
                             .build())
                .add("Капуста цветная",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.3)
                             .build())
                .add("Капуста пекинская",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.6)
                             .build())
                .add("Морская капуста")
                .add("Капуста брюссельская",
                     new UnitsMapBuilder()
                             .put(UNITS, 20.)
                             .build())
                .add("Картофель",
                     new UnitsMapBuilder()
                             .put(UNITS, 10.)
                             .build())
                .add("Кукуруза",
                     new UnitsMapBuilder()
                             .put(UNITS, 4.)
                             .build())
                .add("Лук репчатый",
                     new UnitsMapBuilder()
                             .put(UNITS, 13.3)
                             .build())
                .add("Лук зеленый")
                .add("Морковь",
                     new UnitsMapBuilder()
                             .put(UNITS, 13.3)
                             .build())
                .add("Огурцы",
                     new UnitsMapBuilder()
                             .put(UNITS, 9.)
                             .build())
                .add("Перец болгарский",
                     new UnitsMapBuilder()
                             .put(UNITS, 10.)
                             .build())
                .add("Редис",
                     new UnitsMapBuilder()
                             .put(UNITS, 50.)
                             .build())
                .add("Редька",
                     new UnitsMapBuilder()
                             .put(UNITS, 11.1)
                             .build())
                .add("Репа",
                     new UnitsMapBuilder()
                             .put(UNITS, 5.)
                             .build())
                .add("Свекла",
                     new UnitsMapBuilder()
                             .put(UNITS, 10.)
                             .build())
                .add("Сельдерей",
                     new UnitsMapBuilder()
                             .put(UNITS, 2.)
                             .build())
                .add("Спаржа")
                .add("Помидоры",
                     new UnitsMapBuilder()
                             .put(UNITS, 10.)
                             .build())
                .add("Тыква",
                     new UnitsMapBuilder()
                             .put(UNITS, 0.5)
                             .build())
                .add("Чеснок", UNITS)
                .add("Фрукты")
                .add("Айва",
                     new UnitsMapBuilder()
                             .put(UNITS, 5.8)
                             .build())
                .add("Апельсин",
                     new UnitsMapBuilder()
                             .put(UNITS, 5.2)
                             .build())
                .add("Арбуз",
                     new UnitsMapBuilder()
                             .put(UNITS, 0.16)
                             .build())
                .add("Банан",
                     new UnitsMapBuilder()
                             .put(UNITS, 5.)
                             .build())
                .add("Гранат",
                     new UnitsMapBuilder()
                             .put(UNITS, 5.)
                             .build())
                .add("Грейпфрут",
                     new UnitsMapBuilder()
                             .put(UNITS, 5.)
                             .build())
                .add("Груша",
                     new UnitsMapBuilder()
                             .put(UNITS, 5.8)
                             .build())
                .add("Дыня",
                     new UnitsMapBuilder()
                             .put(UNITS, 0.77)
                             .build())
                .add("Киви",
                     new UnitsMapBuilder()
                             .put(UNITS, 12.5)
                             .build())
                .add("Кокос",
                     new UnitsMapBuilder()
                             .put(UNITS, 0.5)
                             .build())
                .add("Кумкват",
                     new UnitsMapBuilder()
                             .put(UNITS, 33.)
                             .build())
                .add("Лимон",
                     new UnitsMapBuilder()
                             .put(UNITS, 6.67)
                             .build())
                .add("Лайм",
                     new UnitsMapBuilder()
                             .put(UNITS, 10.)
                             .build())
                .add("Манго",
                     new UnitsMapBuilder()
                             .put(UNITS, 3.3)
                             .build())
                .add("Мандарин",
                     new UnitsMapBuilder()
                             .put(UNITS, 12.5)
                             .build())
                .add("Нектарин",
                     new UnitsMapBuilder()
                             .put(UNITS, 11.)
                             .build())
                .add("Персик",
                     new UnitsMapBuilder()
                             .put(UNITS, 9.)
                             .build())
                .add("Помело",
                     new UnitsMapBuilder()
                             .put(UNITS, 6.7)
                             .build())
                .add("Яблоко",
                     new UnitsMapBuilder()
                             .put(UNITS, 6.)
                             .build())
                .add("Хурма",
                     new UnitsMapBuilder()
                             .put(UNITS, 7.7)
                             .build())
                .add("Грибы свежие")
                .add("Шампиньоны",
                     new UnitsMapBuilder()
                             .put(UNITS, 30.)
                             .build())
                .add("Базилик", PACKAGE)
                .add("Зелень в горшочке", PACKAGE)
                .add("Мята", PACKAGE)
                .add("Петрушка", PACKAGE)
                .add("Розмарин", PACKAGE)
                .add("Руккола", PACKAGE)
                .add("Салат", PACKAGE)
                .add("Сельдерей (зелень)", PACKAGE)
                .add("Тархун", PACKAGE)
                .add("Тимьян", PACKAGE)
                .add("Укроп", PACKAGE)
                .add("Фенхель", PACKAGE)
                .add("Шпинат", PACKAGE)
                .add("Щавель", PACKAGE)
                .add("Оливки",
                     new UnitsMapBuilder()
                             .put(UNITS, 66.7)
                             .build())
                .add("Маслины",
                     new UnitsMapBuilder()
                             .put(UNITS, 66.7)
                             .build());

        //        "Бакалея" ProductCategory.GROCERY
        new CategoryBuilder(GROCERY,
                            KILOGRAMM,
                            new UnitsListBuilder()
                                    .addAll(weightUnitArray)//GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS
                                    .build())
                .add("Сахар белый",
                     new UnitsMapBuilder()
                             .put(CUP, 5.)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Сахар тростниковый",
                     new UnitsMapBuilder()
                             .put(CUP, 5.)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .build())
                .add("Сахар рафинад",
                     new UnitsMapBuilder()
                             .put(CUP, 5.)
                             .build())
                .add("Сахарная пудра",
                     new UnitsMapBuilder()
                             .put(CUP, 5.5)
                             .put(TEASPOON, 100.)
                             .put(TABLESPOON, 40.)
                             .build())
                .add("Соль морская",
                     new UnitsMapBuilder()
                             .put(CUP, 4.2)
                             .put(TEASPOON, 66.7)
                             .put(TABLESPOON, 28.5)
                             .build())
                .add("Соль поваренная",
                     new UnitsMapBuilder()
                             .put(CUP, 3.2)
                             .put(TEASPOON, 90.1)
                             .put(TABLESPOON, 30.)
                             .build())
                .add("Мука",
                     new UnitsMapBuilder()
                             .put(CUP, 6.25)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Рис",
                     new UnitsMapBuilder()
                             .put(CUP, 4.2)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Гречневая крупа",
                     new UnitsMapBuilder()
                             .put(CUP, 4.76)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Горох",
                     new UnitsMapBuilder()
                             .put(CUP, 4.3)
                             .put(TEASPOON, 200.1)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Чечевица",
                     new UnitsMapBuilder()
                             .put(CUP, 4.7)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Манная крупа",
                     new UnitsMapBuilder()
                             .put(CUP, 5.)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Кус-Кус",
                     new UnitsMapBuilder()
                             .put(CUP, 4.5)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Кукурузная крупа",
                     new UnitsMapBuilder()
                             .put(CUP, 4.7)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Овес",
                     new UnitsMapBuilder()
                             .put(CUP, 5.5)
                             .put(TEASPOON, 333.3)
                             .put(TABLESPOON, 83.3)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())

                .add("Перловка",
                     new UnitsMapBuilder()
                             .put(CUP, 4.3)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Полба",
                     new UnitsMapBuilder()
                             .put(CUP, 4.)
                             .put(TEASPOON, 333.3)
                             .put(TABLESPOON, 83.3)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Пшено",
                     new UnitsMapBuilder()
                             .put(CUP, 4.5)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Пшеница",
                     new UnitsMapBuilder()
                             .put(CUP, 4.5)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Ячмень",
                     new UnitsMapBuilder()
                             .put(CUP, 5.5)
                             .put(TEASPOON, 200.)
                             .put(TABLESPOON, 50.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Мука",
                     new UnitsMapBuilder()
                             .put(CUP, 6.25)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Мука",
                     new UnitsMapBuilder()
                             .put(CUP, 6.25)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Мука",
                     new UnitsMapBuilder()
                             .put(CUP, 6.25)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build())
                .add("Мука",
                     new UnitsMapBuilder()
                             .put(CUP, 6.25)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 40.)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .build());

        new CategoryBuilder(GROCERY,
                            PACKAGE,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                    .build())
                .add("Заменитель сахара",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Фруктоза",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Хлопья овсяные",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Хлопья кукурузные",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Хлопья смесь злаков",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.325)
                             .put(UNITS, 1.)
                             .build())
                .add("Хлопья гречневые",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Каша-минутка", new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{PACKAGE, UNITS})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Мюсли",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Смесь для выпечки", new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{PACKAGE, UNITS})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Макароны",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Бантики",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Вермишель",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Витки",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Гнезда",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Гребешки",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Листы для лазаньи",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Лапша",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Перья",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Ракушки",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Рожки",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Спагетти",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build())
                .add("Спирали",
                     new UnitsMapBuilder()
                             .put(KILOGRAMM, 0.5)
                             .put(UNITS, 1.)
                             .build());

        new CategoryBuilder(GROCERY,
                            PACKAGE,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{PACKAGE, UNITS})
                                    .build())
                .add("Чипсы",
                     new UnitsMapBuilder()
                             .build())
                .add("Попкорн",
                     new UnitsMapBuilder()
                             .build())
                .add("Снэки",
                     new UnitsMapBuilder()
                             .build())
                .add("Суп быстрого приготовления",
                     new UnitsMapBuilder()
                             .build())
                .add("Лапша быстрого приготовления",
                     new UnitsMapBuilder()
                             .build());

        //        "Мясная гастрономия" ProductCategory.MEAT_GARSTRONOMY
        new CategoryBuilder(MEAT_GARSTRONOMY,
                            KILOGRAMM,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE})
                                    .build())
                .add("Сосиски", new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE, UNITS})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 20.)
                             .build())
                .add("Cардельки", new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE, UNITS})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 10.)
                             .build())
                .add("Колбаски", new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE, UNITS})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 10.)
                             .build())
                .add("Колбаса вареная")
                .add("Колбаса копченая")
                .add("Колбаса")
                .add("Балык")
                .add("Бастурма")
                .add("Бекон")
                .add("Буженина")
                .add("Ветчина")
                .add("Грудинка копченая")
                .add("Корейка копченая")
                .add("Окорок копченый")
                .add("Ребрышки копченые")
                .add("Рулет мясной")
                .add("Рулька")
                .add("Сало")
                .add("Шейка копченая")
                .add("Шпик")
                .add("Паштет")
                .add("Ливер");

        //        "Мясо и птица" ProductCategory.MEAT_BIRDS
        new CategoryBuilder(MEAT_BIRDS,
                            KILOGRAMM,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE})
                                    .build())
                .add("Бедро птицы")
                .add("Голень птицы")
                .add("Грудка птицы")
                .add("Крылья птицы")
                .add("Окорочок птицы")
                .add("Тушка птицы")
                .add("Филе птицы")
                .add("Зразы (полуфабрикат)")
                .add("Колбаски (полуфабрикат)")
                .add("Котлеты (полуфабрикат)")
                .add("Крылья (полуфабрикат)")
                .add("Купаты (полуфабрикат)")
                .add("Медальоны (полуфабрикат)")
                .add("Фарш (полуфабрикат)")
                .add("Стейк (полуфабрикат)")
                .add("Мясные Шарики (полуфабрикат)")
                .add("Шашлык (полуфабрикат)")
                .add("Шницель (полуфабрикат)")
                .add("Желудки (полуфабрикат)")
                .add("Набор суповой (полуфабрикат)")
                .add("Печень (полуфабрикат)")
                .add("Почки (полуфабрикат)")
                .add("Рубец (полуфабрикат)")
                .add("Сердце (полуфабрикат)")
                .add("Язык (полуфабрикат)")
                .add("Баранина")
                .add("Говядина")
                .add("Кролик")
                .add("Свинина")
                .add("Телятина")
                .add("Утка")
                .add("Курица");

        //        "Замороженные продукты" ProductCategory.FROZEN_FOOD
        new CategoryBuilder(FROZEN_FOOD,
                            KILOGRAMM,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE})
                                    .build())
                .add("Мясо замороженное")
                .add("Птица замороженное")
                .add("Морепродукты замороженные");

        //        "Рыба, морепродукты и икра" ProductCategory.FISH_SEAFOOD_CAVIAR
        new CategoryBuilder(FISH_SEAFOOD_CAVIAR,
                            KILOGRAMM,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{KILOGRAMM, GRAMM, PACKAGE, UNITS})
                                    .build())
                .add("Рыба")
                .add("Крабовое мясо, палочки")
                .add("Икра красная")
                .add("Пресервы")
                .add("Рыба соленая")
                .add("Рыба копченая")
                .add("Рыба вяленая")
                .add("Рыба сушеная")
                .add("Гребешки")
                .add("Кальмар")
                .add("Креветки")
                .add("Мидии")
                .add("Морской коктейль")
                .add("Улитки")
                .add("Горбуша")
                .add("Дорада")
                .add("Камбала")
                .add("Карась")
                .add("Карп")
                .add("Кета")
                .add("Кижуч")
                .add("Лосось")
                .add("Минтай")
                .add("Мойва")
                .add("Окунь")
                .add("Осетр")
                .add("Палтус")
                .add("Путассу")
                .add("Пангасиус")
                .add("Сибас")
                .add("Скумбрия")
                .add("Стерлядь")
                .add("Тилапия")
                .add("Судак")
                .add("Толстолобик")
                .add("Треска")
                .add("Тунец")
                .add("Форель")
                .add("Щука")
                .add("Хек");

        //        "Растительные масла, соусы и приправы" ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS
        new CategoryBuilder(VEGETABLE_OIL_SAUCE_CONDIMENTS,
                            LITRE,
                            new UnitsListBuilder()
                                    .addAll(volumeUnitArray)//LITRE, MILILITRE, CUP, TEASPOON, TABLESPOON, PACKAGE, BOTTLE
                                    .build())
                .add("Масло растительное",
                     new UnitsMapBuilder(litreUnitMap)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .put(BOTTLE, 1.)
                             .build())
                .add("Масло оливковое",
                     new UnitsMapBuilder(litreUnitMap)
                             .put(UNITS, 1.)
                             .put(PACKAGE, 1.)
                             .put(BOTTLE, 1.)
                             .build())
                .add("Масло кунжутное",
                     new UnitsMapBuilder(litreUnitMap)
                             .build())
                .add("Масло льняное",
                     new UnitsMapBuilder(litreUnitMap)
                             .build())
                .add("Томатная паста",
                     new UnitsMapBuilder(litreUnitMap)
                             .build())
                .add("Кетчуп",
                     new UnitsMapBuilder(litreUnitMap)
                             .build())
                .add("Соус",
                     new UnitsMapBuilder(litreUnitMap)
                             .build())
                .add("Аджика",
                     new UnitsMapBuilder(litreUnitMap)
                             .build());
        new CategoryBuilder(VEGETABLE_OIL_SAUCE_CONDIMENTS,
                            PACKAGE,
                            new UnitsListBuilder()
                                    .addAll(weightUnitArray)//GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS
                                    .build())
                .add("Приправа")
                .add("Панировочные сухари")
                .add("Ароматизатор")
                .add("Желе")
                .add("Коржи")
                .add("Мастика")
                .add("Глазурь")
                .add("Дрожжи")
                .add("Посыпка кондитерская")
                .add("Желатин");

        new CategoryBuilder(VEGETABLE_OIL_SAUCE_CONDIMENTS,
                            KILOGRAMM,
                            new UnitsListBuilder()
                                    .addAll(weightUnitArray)//GRAMM, KILOGRAMM, CUP, TEASPOON, TABLESPOON, PACKAGE, UNITS
                                    .build())
                .add("Корица",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .put(TEASPOON, 125.)
                             .put(TABLESPOON, 50.)
                             .build())
                .add("Кокосовая стружка",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .put(TEASPOON, 143.)
                             .put(TABLESPOON, 50.)
                             .put(CUP, 7.1)
                             .build())
                .add("Крахмал",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .put(CUP, 5.5)
                             .put(TEASPOON, 100.)
                             .put(TABLESPOON, 33.3)
                             .put(PACKAGE, 5.)
                             .build())
                .add("Какао-порошок",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .put(TEASPOON, 83.3)
                             .put(TABLESPOON, 200.)
                             .put(PACKAGE, 4.)
                             .build())
                .add("Разрыхлитель",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .put(CUP, 6.25)
                             .put(TEASPOON, 100.)
                             .put(TABLESPOON, 33.3)
                             .put(PACKAGE, 12.5)
                             .build())
                .add("Лимонный сок",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .put(CUP, 4.)
                             .put(TEASPOON, 200.)
                             .put(TABLESPOON, 55.5)
                             .put(PACKAGE, 8.)
                             .build())
                .add("Уксус столовый",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .put(CUP, 4.)
                             .put(TEASPOON, 200.)
                             .put(TABLESPOON, 66.7)
                             .build())
                .add("Сода пищевая",
                     new UnitsMapBuilder(kilogramUnitMap)
                             .put(CUP, 4.16)
                             .put(TEASPOON, 711.)
                             .put(TABLESPOON, 25.)
                             .build())
                .add("Декор для выпечки",
                     UNITS, new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{UNITS, PACKAGE})
                             .build());

        //        "Чай, кофе, какао" ProductCategory.TEA_COFFEE_CACAO
        new CategoryBuilder(TEA_COFFEE_CACAO,
                            PACKAGE,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{PACKAGE, UNITS})
                                    .build())
                .add("Чай",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Кофе",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Какао растворимый",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Горячий шоколад",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Цикорий",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build());

        //        "Консервация" ProductCategory.CONSERVATION
        new CategoryBuilder(CONSERVATION,
                            PACKAGE,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                    .build())
                .add("Мандарины в собственном соку",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Горошек консервированный",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Кукуруза консервированная",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Огурцы соленые",
                     KILOGRAMM,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 9.)
                             .build())
                .add("Огурцы маринованные",
                     KILOGRAMM,
                     new UnitsListBuilder()
                             .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                             .build(),
                     new UnitsMapBuilder()
                             .put(UNITS, 9.)
                             .build())
                .add("Томаты консервированные",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Оливки (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Маслины (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Фасоль (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Горбуша (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Кальмар (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Кета (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Килька (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Лосось (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Мидии (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Морская капуста (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Пыжьян (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Сайра (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Сардина (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Семга (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Скумбрия (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Тунец (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Треска (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Форель (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Шпроты (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Язь (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Паштет (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Тушенка (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Сливки (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Белые грибы (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Грузди (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Маслята (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Моховики (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Опята (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Рыжики (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Шампиньоны (консервы)",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Ягоды протертые",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Фрукты в сиропе",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build());

        new CategoryBuilder(CONSERVATION,
                            LITRE,
                            new UnitsListBuilder()
                                    .addAll(volumeUnitArray)//LITRE, MILILITRE, CUP, TEASPOON, TABLESPOON, PACKAGE, BOTTLE
                                    .build())
                .add("Сгущенка вареная",
                     new UnitsMapBuilder(litreUnitMap)
                             .put(PACKAGE, 2.5)
                             .put(UNITS, 2.5)
                             .build())
                .add("Сгущенка с добавками",
                     new UnitsMapBuilder(litreUnitMap)
                             .put(PACKAGE, 2.5)
                             .put(UNITS, 2.5)
                             .build())
                .add("Сгущенка цельная",
                     new UnitsMapBuilder(litreUnitMap)
                             .put(PACKAGE, 2.5)
                             .put(UNITS, 2.5)
                             .build())
                .add("Мед гречишный")
                .add("Мед липовый")
                .add("Мед цветочный")
                .add("Курд лимонный")
                .add("Варенье Абрикос")
                .add("Варенье Брусника")
                .add("Варенье Вишня")
                .add("Варенье Клубника")
                .add("Варенье Клюква")
                .add("Варенье Крыжовник")
                .add("Варенье Малина")
                .add("Варенье Смородина")
                .add("Варенье Черника")
                .add("Джем Абрикос")
                .add("Джем Брусника")
                .add("Джем Вишня")
                .add("Джем Клубника")
                .add("Джем Клюква")
                .add("Джем Крыжовник")
                .add("Джем Малина")
                .add("Джем Смородина")
                .add("Джем Черника")
                .add("Компот Абрикос")
                .add("Компот Брусника")
                .add("Компот Вишня")
                .add("Компот Клубника")
                .add("Компот Клюква")
                .add("Компот Крыжовник")
                .add("Компот Малина")
                .add("Компот Смородина")
                .add("Компот Черника")
                .add("Сироп");

        //        "Мороженое" ProductCategory.ICE_CREAM
        new CategoryBuilder(ICE_CREAM,
                            PACKAGE,
                            new UnitsListBuilder()
                                    .addAll(new MeasureUnit[]{GRAMM, KILOGRAMM, PACKAGE, UNITS})
                                    .build())
                .add("Мороженое",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
                             .build())
                .add("Пломбир",
                     new UnitsMapBuilder()
                             .put(UNITS, 1.)
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
    }

    private static class CategoryBuilder {
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
        }

        private CategoryBuilder(ProductCategory productCategory,
                                List<MeasureUnit> defaultMainUnitList,
                                List<MeasureUnit> defaultUnitList) {
            this.productCategory = productCategory;
            this.defaultMainUnitList = new LinkedList<>(defaultMainUnitList);
            this.defaultUnitList = new LinkedList<>(defaultUnitList);
        }

        public CategoryBuilder add(String name) {
            add(name, defaultMainUnitList, defaultUnitList, new HashMap<>());
            return this;
        }

        public CategoryBuilder add(String name, MeasureUnit unit) {
            add(name, unit, defaultUnitList, new HashMap<>());
            return this;
        }

        public CategoryBuilder add(String name, Map<MeasureUnit, Double> map) {
            add(name, defaultMainUnitList, defaultUnitList, map);
            return this;
        }


        public CategoryBuilder add(String name,
                                   List<MeasureUnit> measureUnitList,
                                   Map<MeasureUnit, Double> map) {
            add(name, defaultMainUnitList, measureUnitList, map);
            return this;
        }

        public CategoryBuilder add(String name,
                                   MeasureUnit unit,
                                   Map<MeasureUnit, Double> map) {
            add(name, unit, defaultUnitList, map);
            return this;
        }

        public CategoryBuilder add(String name,
                                   MeasureUnit unit,
                                   List<MeasureUnit> measureUnitList) {
            add(name, unit, measureUnitList, new HashMap<>());
            return this;
        }

        public CategoryBuilder add(String name,
                                   MeasureUnit unit,
                                   List<MeasureUnit> measureUnitList,
                                   Map<MeasureUnit, Double> map) {
            add(name, Collections.singletonList(unit), measureUnitList, map);
            return this;
        }

        public CategoryBuilder add(String name,
                                   List<MeasureUnit> mainUnits,
                                   List<MeasureUnit> measureUnitList,
                                   Map<MeasureUnit, Double> map) {
            Product product = new Product(productCategory, name,
                                          mainUnits,
                                          measureUnitList);
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
            productRef.push().setValue(product, (databaseError, databaseReference) -> {
                product.fillTheMap(map);
                productRef.child(databaseReference.getKey()).child(DatabaseConstants.DATABASE_MEASURE_MAP_FIELD)
                        .setValue(product.getMeasureStringToAmoutMap());
            });
            return this;
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