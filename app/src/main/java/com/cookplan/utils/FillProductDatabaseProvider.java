package com.cookplan.utils;

import com.cookplan.RApplication;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.cookplan.models.MeasureUnit.BOTTLE;
import static com.cookplan.models.MeasureUnit.GRAMM;
import static com.cookplan.models.MeasureUnit.LITRE;
import static com.cookplan.models.MeasureUnit.MILILITRE;
import static com.cookplan.models.MeasureUnit.PACKAGE;
import static com.cookplan.models.MeasureUnit.UNITS;
import static com.cookplan.models.ProductCategory.ALCOHOL_DRINKS;
import static com.cookplan.models.ProductCategory.ANIMALS_PRODUCTS;
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
        List<Product>     productList = getProductList();
        DatabaseReference database    = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productRef  = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
        for (Product product : productList) {
            productRef.push().setValue(product);
        }
        RApplication.savePriorityList(Arrays.asList(ProductCategory.values()));
    }

    private static List<Product> getProductList() {
        List<Product> list = new ArrayList<>();
        //Алкогольные напитки ProductCategory.ALCOHOL_DRINKS
        list.addAll(new CategoryBuilder(ALCOHOL_DRINKS, BOTTLE)
                .add("Пиво")
                .add("Водка")
                .add("Коньяк")
                .add("Виски")
                .add("Ром")
                .add("Текила")
                .add("Джин")
                .add("Самбука")
                .add("Граппа")
                .add("Ликер")
                .add("Настойка")
                .add("Бальзам")
                .add("Вино")
                .add("Шампанское, игристое вино")
                .add("Вермут")
                .add("Вермут")
                .add("Коктейль")
                .add("Сидр")
                .add("Квас")
                .build());

        //Молочные продукты ProductCategory.MILK_PRODUCT
        list.add(new Product(LITRE, "Молоко коровье", MILK_PRODUCT));
        list.add(new Product(LITRE, "Молоко козье", MILK_PRODUCT));
        list.add(new Product(LITRE, "Молоко соевое", MILK_PRODUCT));
        list.add(new Product(LITRE, "Молоко миндальное", MILK_PRODUCT));
        list.add(new Product(LITRE, "Молоко кокосовое", MILK_PRODUCT));
        list.add(new Product(LITRE, "Молоко овсяное", MILK_PRODUCT));
        list.add(new Product(LITRE, "Молоко рисовое", MILK_PRODUCT));
        list.add(new Product(LITRE, "Молочный коктейль", MILK_PRODUCT));
        list.add(new Product(MILILITRE, "Сливки", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Сливки взбитые", MILK_PRODUCT));
        list.add(new Product(LITRE, "Сливочный коктейль", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Йогурт", MILK_PRODUCT));
        list.add(new Product(BOTTLE, "Йогурт питьевой", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Сырок творожный", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Творожок", MILK_PRODUCT));
        list.add(new Product(GRAMM, "Творог", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Творожная масса", MILK_PRODUCT));
        list.add(new Product(LITRE, "Кефир", MILK_PRODUCT));
        list.add(new Product(UNITS, "Детское питание молочное", MILK_PRODUCT));
        list.add(new Product(BOTTLE, "Айран", MILK_PRODUCT));
        list.add(new Product(BOTTLE, "Ацидофилин", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Варенец", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Закваска", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Бифилайф", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Мацони", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Простокваша", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Ряженка", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Снежок", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Тан", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Маргарин", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Масло кокосовое", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Масло сливочное", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Масло топленое", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Масло шоколадное", MILK_PRODUCT));
        list.add(new Product(MILILITRE, "Сметана", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Мусс", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Пудинг", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Смусси молочный", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Каша рисовая молочная", MILK_PRODUCT));
        list.add(new Product(PACKAGE, "Каша овсяная молочная", MILK_PRODUCT));

        //Cыры ProductCategory.CHEESE
        list.add(new Product(GRAMM, "Сыр твердый натертый", CHEESE));
        list.add(new Product(GRAMM, "Сыр твердый (нарезка)", CHEESE));
        list.add(new Product(GRAMM, "Сыр твердый", CHEESE));
        list.add(new Product(GRAMM, "Моцарелла", CHEESE));
        list.add(new Product(GRAMM, "Сыр творожный", CHEESE));
        list.add(new Product(GRAMM, "Брынза", CHEESE));
        list.add(new Product(GRAMM, "Сулугуни", CHEESE));
        list.add(new Product(GRAMM, "Маскарпоне", CHEESE));
        list.add(new Product(GRAMM, "Рикотта", CHEESE));
        list.add(new Product(GRAMM, "Сыр копченый", CHEESE));
        list.add(new Product(PACKAGE, "Сыр плавленый", CHEESE));
        list.add(new Product(UNITS, "Сырок плавленный", CHEESE));
        list.add(new Product(GRAMM, "Сыр деликатесный", CHEESE));

        //Яйца ProductCategory.EGGS
        list.add(new Product(UNITS, "Яйцо куриное", EGGS));
        list.add(new Product(UNITS, "Яйцо перепелиное", EGGS));

        //Кондитерские изделия ProductCategory.CONFECTIONERY
        list.add(new Product(UNITS, "Шоколад горький", CONFECTIONERY));
        list.add(new Product(UNITS, "Шоколад молочный", CONFECTIONERY));
        list.add(new Product(UNITS, "Шоколад белый", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Шоколадная паста", CONFECTIONERY));
        list.add(new Product(UNITS, "Батончик шоколадный", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Драже", CONFECTIONERY));
        list.add(new Product(UNITS, "Шоколадная фигурка", CONFECTIONERY));
        list.add(new Product(UNITS, "Яйцо шоколадное", CONFECTIONERY));
        list.add(new Product(GRAMM, "Конфеты", CONFECTIONERY));
        list.add(new Product(GRAMM, "Трюфели", CONFECTIONERY));
        list.add(new Product(GRAMM, "Глазированные фрукты и орехи", CONFECTIONERY));
        list.add(new Product(GRAMM, "Печенье", CONFECTIONERY));
        list.add(new Product(GRAMM, "Вафли", CONFECTIONERY));
        list.add(new Product(UNITS, "Рулет кондитерский", CONFECTIONERY));
        list.add(new Product(UNITS, "Кекс", CONFECTIONERY));
        list.add(new Product(UNITS, "Тарталетки", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Кукурузные палочки", CONFECTIONERY));
        list.add(new Product(UNITS, "Круассан", CONFECTIONERY));
        list.add(new Product(UNITS, "Штрудель", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Зефир", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Мармелад", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Маршмеллоу", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Пастила", CONFECTIONERY));
        list.add(new Product(UNITS, "Сахарная вата", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Суфле", CONFECTIONERY));
        list.add(new Product(UNITS, "Пирог", CONFECTIONERY));
        list.add(new Product(UNITS, "Пирожное", CONFECTIONERY));
        list.add(new Product(UNITS, "Торт", CONFECTIONERY));
        list.add(new Product(UNITS, "Шарлотка", CONFECTIONERY));
        list.add(new Product(UNITS, "Жевательная резинка", CONFECTIONERY));
        list.add(new Product(UNITS, "Козинак", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Кунафа", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Пахлава", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Рахат-лукум", CONFECTIONERY));
        list.add(new Product(GRAMM, "Халва", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Чак-чак", CONFECTIONERY));
        list.add(new Product(PACKAGE, "Щербет", CONFECTIONERY));

        //Детские товары" ProductCategory.CHILD_PRODUCTS
        list.add(new Product(PACKAGE, "Детское питание", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Пюре детское", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Подгузники", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Пеленки", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Бутылочка детская", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Кружка детская", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Поильник детский", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Пустышка детская", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Соска детская", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Ложка детская", CHILD_PRODUCTS));
        list.add(new Product(PACKAGE, "Тарелка детская", CHILD_PRODUCTS));

//        Напитки, соки ProductCategory.DRINKS_JUICE
        list.add(new Product(PACKAGE, "Кисель", DRINKS_JUICE));
        list.add(new Product(BOTTLE, "Вода питьевая", DRINKS_JUICE));
        list.add(new Product(BOTTLE, "Газировка", DRINKS_JUICE));
        list.add(new Product(BOTTLE, "Вода минеральная", DRINKS_JUICE));
        list.add(new Product(BOTTLE, "Лимонад", DRINKS_JUICE));
        list.add(new Product(BOTTLE, "Тархун", DRINKS_JUICE));
        list.add(new Product(PACKAGE, "Компот", DRINKS_JUICE));
        list.add(new Product(PACKAGE, "Морс", DRINKS_JUICE));
        list.add(new Product(PACKAGE, "Сок", DRINKS_JUICE));
        list.add(new Product(PACKAGE, "Нектар", DRINKS_JUICE));
        list.add(new Product(BOTTLE, "Квас", DRINKS_JUICE));
        list.add(new Product(BOTTLE, "Холодный чай (черный)", DRINKS_JUICE));
        list.add(new Product(BOTTLE, "Холодный чай (зеленый)", DRINKS_JUICE));

//        Хлебобулочные изделия" ProductCategory.BAKERY_PRODUCTS
        list.add(new Product(UNITS, "Хлеб белый", BAKERY_PRODUCTS));
        list.add(new Product(UNITS, "Хлеб для тостов и сэндвичей", BAKERY_PRODUCTS));
        list.add(new Product(UNITS, "Хлеб зерновой", BAKERY_PRODUCTS));
        list.add(new Product(PACKAGE, "Хлебцы", BAKERY_PRODUCTS));
        list.add(new Product(UNITS, "Хлеб черный", BAKERY_PRODUCTS));
        list.add(new Product(UNITS, "Хлеб кукурузный", BAKERY_PRODUCTS));
        list.add(new Product(PACKAGE, "Сухарики", BAKERY_PRODUCTS));
        list.add(new Product(PACKAGE, "Баранки", BAKERY_PRODUCTS));
        list.add(new Product(PACKAGE, "Соломка", BAKERY_PRODUCTS));
        list.add(new Product(PACKAGE, "Сухари", BAKERY_PRODUCTS));
        list.add(new Product(UNITS, "Лепешка", BAKERY_PRODUCTS));
        list.add(new Product(PACKAGE, "Сушки", BAKERY_PRODUCTS));
        list.add(new Product(PACKAGE, "Хлебные палочки", BAKERY_PRODUCTS));
        list.add(new Product(UNITS, "Сдобная выпечка", BAKERY_PRODUCTS));
        list.add(new Product(UNITS, "Слоеная выпечка", BAKERY_PRODUCTS));
        list.add(new Product(UNITS, "Песочная выпечка", BAKERY_PRODUCTS));

//        Овощи, фрукты, грибы, ягоды ProductCategory.FRUITS_VEGETABLES
        list.add(new Product(PACKAGE, "Арахис", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Грецкий орех", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Изюм", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Кедровый орех", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Кешью", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Курага", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Миндаль", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Семечки подсолнечные", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Семечки тыквенные", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Смесь орехов", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Смесь орехов и сухофруктов", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Сухофрукты", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Финики", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Фисташки", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Фундук", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Чернослив", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Ягоды сушеные", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Ягоды замороженные", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Овощное ассорти", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Авокадо", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Баклажан", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Имбирь", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Кабачок", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Капуста", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Картофель", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Кукуруза", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Лук репчатый", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Лук зеленый", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Морковь", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Огурец", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Перец", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Редис", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Редька", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Репа", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Свекла", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Сельдерей", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Спаржа", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Помидор", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Тыква", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Фасоль", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Чеснок", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Фрукты", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Айва", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Апельсин", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Арбуз", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Банан", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Виноград", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Голубика", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Гранат", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Грейпфрут", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Груша", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Дыня", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Ежевика", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Киви", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Кокос", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Кумкват", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Лимон", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Лонган", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Лайм", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Малина", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Манго", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Мандарин", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Нектарин", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Персик", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Помело", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Слива", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Смородина", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Яблоко", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Хурма", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Грибы сушеные", FRUITS_VEGETABLES));
        list.add(new Product(PACKAGE, "Грибы свежие", FRUITS_VEGETABLES));
        list.add(new Product(UNITS, "Шампиньоны", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Базилик", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Зелень в горшочке", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Мята", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Петрушка", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Пшеница", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Розмарин", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Руккола", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Салат", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Сельдерей", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Тархун", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Тимьян", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Укроп", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Фенхель", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Шпинат", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Щавель", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Оливки", FRUITS_VEGETABLES));
        list.add(new Product(GRAMM, "Маслины", FRUITS_VEGETABLES));

//        "Бакалея" ProductCategory.GROCERY
        list.add(new Product(GRAMM, "Сахар-песок белый", GROCERY));
        list.add(new Product(GRAMM, "Сахар-песок тростниковый", GROCERY));
        list.add(new Product(PACKAGE, "Сахар рафинад", GROCERY));
        list.add(new Product(GRAMM, "Сахарная пудра", GROCERY));
        list.add(new Product(PACKAGE, "Заменитель сахара", GROCERY));
        list.add(new Product(PACKAGE, "Фруктоза", GROCERY));
        list.add(new Product(GRAMM, "Соль морская", GROCERY));
        list.add(new Product(GRAMM, "Соль поваренная", GROCERY));
        list.add(new Product(PACKAGE, "Хлопья овсяные", GROCERY));
        list.add(new Product(PACKAGE, "Хлопья кукурузные", GROCERY));
        list.add(new Product(PACKAGE, "Хлопья смесь злаков", GROCERY));
        list.add(new Product(PACKAGE, "Хлопья гречневые", GROCERY));
        list.add(new Product(PACKAGE, "Каша-минутка", GROCERY));
        list.add(new Product(PACKAGE, "Мюсли", GROCERY));
        list.add(new Product(PACKAGE, "Готовый завтрак Nestle", GROCERY));
        list.add(new Product(PACKAGE, "Смесь для выпечки", GROCERY));
        list.add(new Product(GRAMM, "Мука", GROCERY));
        list.add(new Product(PACKAGE, "Чипсы", GROCERY));
        list.add(new Product(PACKAGE, "Сухарики", GROCERY));
        list.add(new Product(PACKAGE, "Попкорн", GROCERY));
        list.add(new Product(PACKAGE, "Снэки", GROCERY));
        list.add(new Product(PACKAGE, "Макароны", GROCERY));
        list.add(new Product(PACKAGE, "Суп быстрого приготовления", GROCERY));
        list.add(new Product(PACKAGE, "Лапша быстрого приготовления", GROCERY));
        list.add(new Product(PACKAGE, "Бантики", GROCERY));
        list.add(new Product(PACKAGE, "Вермишель", GROCERY));
        list.add(new Product(PACKAGE, "Витки", GROCERY));
        list.add(new Product(PACKAGE, "Гнезда", GROCERY));
        list.add(new Product(PACKAGE, "Гребешки", GROCERY));
        list.add(new Product(PACKAGE, "Листы для лазаньи", GROCERY));
        list.add(new Product(PACKAGE, "Лапша", GROCERY));
        list.add(new Product(PACKAGE, "Перья", GROCERY));
        list.add(new Product(PACKAGE, "Ракушки", GROCERY));
        list.add(new Product(PACKAGE, "Рожки", GROCERY));
        list.add(new Product(PACKAGE, "Спагетти", GROCERY));
        list.add(new Product(PACKAGE, "Спирали", GROCERY));
        list.add(new Product(GRAMM, "Рис", GROCERY));
        list.add(new Product(GRAMM, "Гречневая крупа", GROCERY));
        list.add(new Product(GRAMM, "Горох", GROCERY));
        list.add(new Product(GRAMM, "Фасоль", GROCERY));
        list.add(new Product(GRAMM, "Чечевица", GROCERY));
        list.add(new Product(GRAMM, "Манная крупа", GROCERY));
        list.add(new Product(GRAMM, "Кус-Кус", GROCERY));
        list.add(new Product(GRAMM, "Кукурузная крупа", GROCERY));
        list.add(new Product(GRAMM, "Льняная крупа", GROCERY));
        list.add(new Product(GRAMM, "Овес", GROCERY));
        list.add(new Product(GRAMM, "Перловка", GROCERY));
        list.add(new Product(GRAMM, "Полба", GROCERY));
        list.add(new Product(GRAMM, "Пшено", GROCERY));
        list.add(new Product(GRAMM, "Пшеница", GROCERY));
        list.add(new Product(GRAMM, "Ячмень", GROCERY));


//        "Мясная гастрономия" ProductCategory.MEAT_GARSTRONOMY
        list.add(new Product(GRAMM, "Сосиски", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Cардельки", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Колбаски", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Колбаса вареная", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Колбаса копченая", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Колбаса", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Балык", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Бастурма", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Бекон", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Буженина", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Ветчина", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Грудинка копченая", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Корейка копченая", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Окорок копченый", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Ребрышки копченые", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Рулет мясной", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Рулька", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Сало", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Шейка копченая", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Шпик", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Паштет", MEAT_GARSTRONOMY));
        list.add(new Product(GRAMM, "Ливер", MEAT_GARSTRONOMY));

//        "Мясо и птица" ProductCategory.MEAT_BIRDS
        list.add(new Product(GRAMM, "Бедро птицы", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Голень птицы", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Грудка птицы", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Крылья птицы", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Окорочок птицы", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Тушка птицы", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Филе птицы", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Зразы (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Колбаски (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Котлеты (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Крылья (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Купаты (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Медальоны (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Стейк (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Фарш (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Мясные Шарики (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Шашлык (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Шницель (полуфабрикат)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Желудки (субпродукты)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Набор суповой (субпродукты)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Печень (субпродукты)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Почки (субпродукты)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Рубец (субпродукты)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Сердце (субпродукты)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Язык (субпродукты)", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Баранина", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Говядина", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Кролик", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Свинина", MEAT_BIRDS));
        list.add(new Product(GRAMM, "Телятина", MEAT_BIRDS));

//        "Замороженные продукты" ProductCategory.FROZEN_FOOD
        list.add(new Product(GRAMM, "Мясо замороженное", FROZEN_FOOD));
        list.add(new Product(GRAMM, "Птица замороженная", FROZEN_FOOD));
        list.add(new Product(GRAMM, "Морепродукты замороженные", FROZEN_FOOD));

//        "Рыба, морепродукты и икра" ProductCategory.FISH_SEAFOOD_CAVIAR
        list.add(new Product(GRAMM, "Рыба", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Крабовое мясо, палочки", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Икра красная", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Пресервы", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Рыба соленая", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Рыба копченая", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Рыба вяленая", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Рыба сушеная", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Гребешки", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Кальмар", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Креветки", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Мидии", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Морской коктейль", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Улитки", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Горбуша", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Дорада", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Камбала", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Карась", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Карп", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Кета", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Кижуч", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Лосось", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Минтай", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Мойва", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Окунь", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Осетр", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Палтус", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Путассу", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Пангасиус", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Сибас", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Скумбрия", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Стерлядь", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Тилапия", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Судак", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Толстолобик", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Треска", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Тунец", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Форель", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Щука", FISH_SEAFOOD_CAVIAR));
        list.add(new Product(GRAMM, "Хек", FISH_SEAFOOD_CAVIAR));

//        "Растительные масла, соусы и приправы" ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS
        list.add(new Product(BOTTLE, "Масло растительное", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(BOTTLE, "Масло оливковое", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(BOTTLE, "Масло кунжутное", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(BOTTLE, "Масло льняное", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Приправа", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(BOTTLE, "Кетчуп", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(BOTTLE, "Томатная паста", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Панировочные сухари", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(BOTTLE, "Соус", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(BOTTLE, "Аджика", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Ароматизатор", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(UNITS, "Декор для выпечки", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Желе", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Коржи", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Корица", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Мастика", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Начинка для выпечки", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Глазурь", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Дрожжи", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Крахмал", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Кокосовая стружка", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Посыпка кондитерская", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Желатин", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Какао-порошок", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Разрыхлитель", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Лимонный сок", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Уксус столовый", VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(PACKAGE, "Сода пищевая", VEGETABLE_OIL_SAUCE_CONDIMENTS));

//        "Чай, кофе, какао" ProductCategory.TEA_COFFEE_CACAO
        list.add(new Product(PACKAGE, "Чай", TEA_COFFEE_CACAO));
        list.add(new Product(PACKAGE, "Кофе", TEA_COFFEE_CACAO));
        list.add(new Product(PACKAGE, "Какао", TEA_COFFEE_CACAO));
        list.add(new Product(PACKAGE, "Горячий шоколад", TEA_COFFEE_CACAO));
        list.add(new Product(PACKAGE, "Цикорий", TEA_COFFEE_CACAO));

//        "Товары для животных" ProductCategory.ANIMALS_PRODUCTS
        list.add(new Product(PACKAGE, "Корм для животных", ANIMALS_PRODUCTS));
        list.add(new Product(PACKAGE, "Корм для животных влажный", ANIMALS_PRODUCTS));
        list.add(new Product(PACKAGE, "Корм для животных сухой", ANIMALS_PRODUCTS));
        list.add(new Product(UNITS, "Лакомства для животных", ANIMALS_PRODUCTS));
        list.add(new Product(UNITS, "Консервы для животных", ANIMALS_PRODUCTS));
        list.add(new Product(PACKAGE, "Витамины для животных", ANIMALS_PRODUCTS));
        list.add(new Product(UNITS, "Миска", ANIMALS_PRODUCTS));
        list.add(new Product(UNITS, "Игрушка", ANIMALS_PRODUCTS));
        list.add(new Product(UNITS, "Туалет", ANIMALS_PRODUCTS));
        list.add(new Product(UNITS, "Поводок", ANIMALS_PRODUCTS));
        list.add(new Product(UNITS, "Когтеточка", ANIMALS_PRODUCTS));
        list.add(new Product(UNITS, "Лежанка", ANIMALS_PRODUCTS));
        list.add(new Product(PACKAGE, "Шампунь для животных", ANIMALS_PRODUCTS));
        list.add(new Product(PACKAGE, "Наполнитель для кошачьего туалета", ANIMALS_PRODUCTS));

//        "Консервация" ProductCategory.CONSERVATION
        list.add(new Product(UNITS, "Горошек консервированный", CONSERVATION));
        list.add(new Product(UNITS, "Кукуруза консервированная", CONSERVATION));
        list.add(new Product(UNITS, "Огурцы соленые", CONSERVATION));
        list.add(new Product(UNITS, "Огурцы маринованные", CONSERVATION));
        list.add(new Product(UNITS, "Томаты консервированные", CONSERVATION));
        list.add(new Product(UNITS, "Оливки (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Маслины (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Фасоль (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Горбуша (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Кальмар (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Кета (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Килька (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Лосось (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Мидии (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Морская капуста (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Пыжьян (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Сайра (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Сардина (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Семга (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Скумбрия (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Тунец (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Треска (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Форель (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Шпроты (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Язь (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Паштет", CONSERVATION));
        list.add(new Product(UNITS, "Тушенка (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Сгущенка вареная", CONSERVATION));
        list.add(new Product(UNITS, "Сгущенка с добавками", CONSERVATION));
        list.add(new Product(UNITS, "Сгущенка цельная", CONSERVATION));
        list.add(new Product(UNITS, "Сливки(консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Белые грибы (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Грузди (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Маслята (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Моховики (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Опята (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Рыжики (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Шампиньоны (консервы)", CONSERVATION));
        list.add(new Product(UNITS, "Мед гречишный", CONSERVATION));
        list.add(new Product(UNITS, "Мед липовый", CONSERVATION));
        list.add(new Product(UNITS, "Мед цветочный", CONSERVATION));
        list.add(new Product(UNITS, "Варенье", CONSERVATION));
        list.add(new Product(UNITS, "Джем", CONSERVATION));
        list.add(new Product(UNITS, "Компот", CONSERVATION));
        list.add(new Product(UNITS, "Сироп", CONSERVATION));
        list.add(new Product(UNITS, "Фрукты в сиропе", CONSERVATION));
        list.add(new Product(UNITS, "Ягоды протертые", CONSERVATION));

//        "Мороженое" ProductCategory.ICE_CREAM
        list.addAll(new CategoryBuilder(ICE_CREAM, PACKAGE)
                .add("Мороженое")
                .add(GRAMM, "Пломбир")
                .build());

        //list.add(new Product(PACKAGE, "Мороженое", ICE_CREAM));
        //list.add(new Product(GRAMM, "Пломбир", ICE_CREAM));

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
        return list;
    }

    private static class CategoryBuilder {
        private final ProductCategory productCategory;
        private final MeasureUnit defaultUnit;
        private final List<Product> products;

        public CategoryBuilder(ProductCategory productCategory) {
            this(productCategory, null);

        }

        public CategoryBuilder(ProductCategory productCategory, MeasureUnit defaultUnit) {
            this(productCategory, defaultUnit, new LinkedList<>());
        }

        private CategoryBuilder(ProductCategory productCategory, MeasureUnit defaultUnit, List<Product> products) {
            this.productCategory = productCategory;
            this.defaultUnit = defaultUnit;
            this.products = products;
        }

        public CategoryBuilder add(String name) {
            products.add(new Product(defaultUnit, name, productCategory));
            return this;
        }

        public CategoryBuilder add(MeasureUnit unit, String name) {
            products.add(new Product(unit, name, productCategory));
            return this;
        }

        public List<Product> build() {
            return products;
        }
    }
}