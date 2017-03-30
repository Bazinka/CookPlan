package com.cookplan.utils;

import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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
        List<Product> productList = getProductList();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productRef = database.child(DatabaseConstants.DATABASE_PRODUCT_TABLE);
        for (Product product : productList) {
            productRef.push().setValue(product);
        }
    }

    private static List<Product> getProductList() {
        List<Product> list = new ArrayList<>();
        //Алкогольные напитки ProductCategory.ALCOHOL_DRINKS
        list.add(new Product(MeasureUnit.BOTTLE, "Пиво", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Водка", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Коньяк", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Виски", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Ром", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Текила", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Джин", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Самбука", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Граппа", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Ликер", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Настойка", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Бальзам", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Вино", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Шампанское, игристое вино", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Вермут", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Вермут", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Коктейль", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Сидр", ProductCategory.ALCOHOL_DRINKS));
        list.add(new Product(MeasureUnit.BOTTLE, "Квас", ProductCategory.ALCOHOL_DRINKS));

        //Молочные продукты ProductCategory.MILK_PRODUCT
        list.add(new Product(MeasureUnit.LITRE, "Молоко коровье", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.LITRE, "Молоко козье", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.LITRE, "Молоко соевое", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.LITRE, "Молоко миндальное", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.LITRE, "Молоко кокосовое", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.LITRE, "Молоко овсяное", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.LITRE, "Молоко рисовое", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.LITRE, "Молочный коктейль", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.MILILITRE, "Сливки", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Сливки взбитые", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.LITRE, "Сливочный коктейль", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Йогурт", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.BOTTLE, "Йогурт питьевой", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Сырок творожный", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Творожок", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.GRAMM, "Творог", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Творожная масса", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.LITRE, "Кефир", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.UNITS, "Детское питание молочное", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.BOTTLE, "Айран", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.BOTTLE, "Ацидофилин", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Варенец", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Закваска", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Бифилайф", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Мацони", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Простокваша", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Ряженка", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Снежок", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Тан", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Маргарин", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Масло кокосовое", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Масло сливочное", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Масло топленое", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Масло шоколадное", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.MILILITRE, "Сметана", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Мусс", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Пудинг", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Смусси молочный", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Каша рисовая молочная", ProductCategory.MILK_PRODUCT));
        list.add(new Product(MeasureUnit.PACKAGE, "Каша овсяная молочная", ProductCategory.MILK_PRODUCT));

        //Cыры ProductCategory.CHEESE
        list.add(new Product(MeasureUnit.GRAMM, "Сыр твердый натертый", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Сыр твердый (нарезка)", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Сыр твердый", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Моцарелла", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Сыр творожный", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Брынза", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Сулугуни", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Маскарпоне", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Рикотта", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Сыр копченый", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.PACKAGE, "Сыр плавленый", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.UNITS, "Сырок плавленный", ProductCategory.CHEESE));
        list.add(new Product(MeasureUnit.GRAMM, "Сыр деликатесный", ProductCategory.CHEESE));

        //Яйца ProductCategory.EGGS
        list.add(new Product(MeasureUnit.UNITS, "Яйцо куриное", ProductCategory.EGGS));
        list.add(new Product(MeasureUnit.UNITS, "Яйцо перепелиное", ProductCategory.EGGS));

        //Кондитерские изделия ProductCategory.CONFECTIONERY
        list.add(new Product(MeasureUnit.UNITS, "Шоколад горький", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Шоколад молочный", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Шоколад белый", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Шоколадная паста", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Батончик шоколадный", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Драже", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Шоколадная фигурка", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Яйцо шоколадное", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.GRAMM, "Конфеты", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.GRAMM, "Трюфели", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.GRAMM, "Глазированные фрукты и орехи", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.GRAMM, "Печенье", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.GRAMM, "Вафли", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Рулет кондитерский", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Кекс", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Тарталетки", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Кукурузные палочки", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Круассан", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Штрудель", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Зефир", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Мармелад", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Маршмеллоу", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Пастила", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Сахарная вата", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Суфле", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Пирог", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Пирожное", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Торт", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Шарлотка", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Жевательная резинка", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.UNITS, "Козинак", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Кунафа", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Пахлава", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Рахат-лукум", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.GRAMM, "Халва", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Чак-чак", ProductCategory.CONFECTIONERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Щербет", ProductCategory.CONFECTIONERY));

        //Детские товары" ProductCategory.CHILD_PRODUCTS
        list.add(new Product(MeasureUnit.PACKAGE, "Детское питание", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Пюре детское", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Подгузники", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Пеленки", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Бутылочка детская", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Кружка детская", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Поильник детский", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Пустышка детская", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Соска детская", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Ложка детская", ProductCategory.CHILD_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Тарелка детская", ProductCategory.CHILD_PRODUCTS));

//        Напитки, соки ProductCategory.DRINKS_JUICE
        list.add(new Product(MeasureUnit.PACKAGE, "Кисель", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.BOTTLE, "Вода питьевая", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.BOTTLE, "Газировка", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.BOTTLE, "Вода минеральная", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.BOTTLE, "Лимонад", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.BOTTLE, "Тархун", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.PACKAGE, "Компот", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.PACKAGE, "Морс", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.PACKAGE, "Сок", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.PACKAGE, "Нектар", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.BOTTLE, "Квас", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.BOTTLE, "Холодный чай (черный)", ProductCategory.DRINKS_JUICE));
        list.add(new Product(MeasureUnit.BOTTLE, "Холодный чай (зеленый)", ProductCategory.DRINKS_JUICE));

//        Хлебобулочные изделия" ProductCategory.BAKERY_PRODUCTS
        list.add(new Product(MeasureUnit.UNITS, "Хлеб белый", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Хлеб для тостов и сэндвичей", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Хлеб зерновой", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Хлебцы", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Хлеб черный", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Хлеб кукурузный", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Сухарики", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Баранки", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Соломка", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Сухари", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Лепешка", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Сушки", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Хлебные палочки", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Сдобная выпечка", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Слоеная выпечка", ProductCategory.BAKERY_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Песочная выпечка", ProductCategory.BAKERY_PRODUCTS));

//        Овощи, фрукты, грибы, ягоды ProductCategory.FRUITS_VEGETABLES
        list.add(new Product(MeasureUnit.PACKAGE, "Арахис", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Грецкий орех", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Изюм", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Кедровый орех", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Кешью", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Курага", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Миндаль", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Семечки подсолнечные", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Семечки тыквенные", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Смесь орехов", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Смесь орехов и сухофруктов", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Сухофрукты", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Финики", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Фисташки", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Фундук", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Чернослив", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Ягоды сушеные", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Ягоды замороженные", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Овощное ассорти", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Авокадо", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Баклажан", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Имбирь", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Кабачок", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Капуста", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Картофель", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Кукуруза", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Лук репчатый", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Лук зеленый", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Морковь", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Огурец", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Перец", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Редис", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Редька", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Репа", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Свекла", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Сельдерей", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Спаржа", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Помидор", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Тыква", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Фасоль", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Чеснок", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Фрукты", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Айва", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Апельсин", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Арбуз", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Банан", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Виноград", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Голубика", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Гранат", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Грейпфрут", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Груша", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Дыня", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Ежевика", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Киви", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Кокос", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Кумкват", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Лимон", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Лонган", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Лайм", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Малина", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Манго", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Мандарин", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Нектарин", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Персик", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Помело", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Слива", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Смородина", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Яблоко", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Хурма", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Грибы сушеные", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.PACKAGE, "Грибы свежие", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.UNITS, "Шампиньоны", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Базилик", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Зелень в горшочке", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Мята", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Петрушка", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Пшеница", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Розмарин", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Руккола", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Салат", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Сельдерей", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Тархун", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Тимьян", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Укроп", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Фенхель", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Шпинат", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Щавель", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Оливки", ProductCategory.FRUITS_VEGETABLES));
        list.add(new Product(MeasureUnit.GRAMM, "Маслины", ProductCategory.FRUITS_VEGETABLES));

//        "Бакалея" ProductCategory.GROCERY
        list.add(new Product(MeasureUnit.GRAMM, "Сахар-песок белый", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Сахар-песок тростниковый", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Сахар рафинад", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Сахарная пудра", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Заменитель сахара", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Фруктоза", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Соль морская", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Соль поваренная", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Хлопья овсяные", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Хлопья кукурузные", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Хлопья смесь злаков", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Хлопья гречневые", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Каша-минутка", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Мюсли", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Готовый завтрак Nestle", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Смесь для выпечки", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Мука", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Чипсы", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Сухарики", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Попкорн", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Снэки", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Макароны", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Суп быстрого приготовления", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Лапша быстрого приготовления", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Бантики", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Вермишель", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Витки", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Гнезда", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Гребешки", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Листы для лазаньи", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Лапша", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Перья", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Ракушки", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Рожки", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Спагетти", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.PACKAGE, "Спирали", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Рис", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Гречневая крупа", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Горох", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Фасоль", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Чечевица", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Манная крупа", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Кус-Кус", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Кукурузная крупа", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Льняная крупа", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Овес", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Перловка", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Полба", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Пшено", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Пшеница", ProductCategory.GROCERY));
        list.add(new Product(MeasureUnit.GRAMM, "Ячмень", ProductCategory.GROCERY));


//        "Мясная гастрономия" ProductCategory.MEAT_GARSTRONOMY
        list.add(new Product(MeasureUnit.GRAMM, "Сосиски", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Cардельки", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Колбаски", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Колбаса вареная", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Колбаса копченая", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Колбаса", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Балык", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Бастурма", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Бекон", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Буженина", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Ветчина", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Грудинка копченая", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Корейка копченая", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Окорок копченый", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Ребрышки копченые", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Рулет мясной", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Рулька", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Сало", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Шейка копченая", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Шпик", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Паштет", ProductCategory.MEAT_GARSTRONOMY));
        list.add(new Product(MeasureUnit.GRAMM, "Ливер", ProductCategory.MEAT_GARSTRONOMY));

//        "Мясо и птица" ProductCategory.MEAT_BIRDS
        list.add(new Product(MeasureUnit.GRAMM, "Бедро птицы", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Голень птицы", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Грудка птицы", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Крылья птицы", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Окорочок птицы", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Тушка птицы", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Филе птицы", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Зразы (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Колбаски (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Котлеты (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Крылья (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Купаты (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Медальоны (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Стейк (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Фарш (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Мясные Шарики (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Шашлык (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Шницель (полуфабрикат)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Желудки (субпродукты)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Набор суповой (субпродукты)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Печень (субпродукты)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Почки (субпродукты)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Рубец (субпродукты)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Сердце (субпродукты)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Язык (субпродукты)", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Баранина", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Говядина", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Кролик", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Свинина", ProductCategory.MEAT_BIRDS));
        list.add(new Product(MeasureUnit.GRAMM, "Телятина", ProductCategory.MEAT_BIRDS));

//        "Замороженные продукты" ProductCategory.FROZEN_FOOD
        list.add(new Product(MeasureUnit.GRAMM, "Мясо замороженное", ProductCategory.FROZEN_FOOD));
        list.add(new Product(MeasureUnit.GRAMM, "Птица замороженная", ProductCategory.FROZEN_FOOD));
        list.add(new Product(MeasureUnit.GRAMM, "Морепродукты замороженные", ProductCategory.FROZEN_FOOD));

//        "Рыба, морепродукты и икра" ProductCategory.FISH_SEAFOOD_CAVIAR
        list.add(new Product(MeasureUnit.GRAMM, "Рыба", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Крабовое мясо, палочки", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Икра красная", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Пресервы", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Рыба соленая", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Рыба копченая", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Рыба вяленая", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Рыба сушеная", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Гребешки", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Кальмар", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Креветки", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Мидии", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Морской коктейль", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Улитки", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Горбуша", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Дорада", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Камбала", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Карась", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Карп", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Кета", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Кижуч", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Лосось", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Минтай", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Мойва", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Окунь", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Осетр", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Палтус", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Путассу", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Пангасиус", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Сибас", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Скумбрия", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Стерлядь", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Тилапия", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Судак", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Толстолобик", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Треска", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Тунец", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Форель", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Щука", ProductCategory.FISH_SEAFOOD_CAVIAR));
        list.add(new Product(MeasureUnit.GRAMM, "Хек", ProductCategory.FISH_SEAFOOD_CAVIAR));

//        "Растительные масла, соусы и приправы" ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS
        list.add(new Product(MeasureUnit.BOTTLE, "Масло растительное", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.BOTTLE, "Масло оливковое", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.BOTTLE, "Масло кунжутное", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.BOTTLE, "Масло льняное", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Приправа", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.BOTTLE, "Кетчуп", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.BOTTLE, "Томатная паста", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Панировочные сухари", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.BOTTLE, "Соус", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.BOTTLE, "Аджика", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Ароматизатор", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.UNITS, "Декор для выпечки", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Желе", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Коржи", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Корица", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Мастика", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Начинка для выпечки", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Глазурь", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Дрожжи", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Крахмал", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Кокосовая стружка", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Посыпка кондитерская", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Желатин", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Какао-порошок", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Разрыхлитель", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Лимонный сок", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Уксус столовый", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Сода пищевая", ProductCategory.VEGETABLE_OIL_SAUCE_CONDIMENTS));

//        "Чай, кофе, какао" ProductCategory.TEA_COFFEE_CACAO
        list.add(new Product(MeasureUnit.PACKAGE, "Чай", ProductCategory.TEA_COFFEE_CACAO));
        list.add(new Product(MeasureUnit.PACKAGE, "Кофе", ProductCategory.TEA_COFFEE_CACAO));
        list.add(new Product(MeasureUnit.PACKAGE, "Какао", ProductCategory.TEA_COFFEE_CACAO));
        list.add(new Product(MeasureUnit.PACKAGE, "Горячий шоколад", ProductCategory.TEA_COFFEE_CACAO));
        list.add(new Product(MeasureUnit.PACKAGE, "Цикорий", ProductCategory.TEA_COFFEE_CACAO));

//        "Товары для животных" ProductCategory.ANIMALS_PRODUCTS
        list.add(new Product(MeasureUnit.PACKAGE, "Корм для животных", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Корм для животных влажный", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Корм для животных сухой", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Лакомства для животных", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Консервы для животных", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Витамины для животных", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Миска", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Игрушка", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Туалет", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Поводок", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Когтеточка", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.UNITS, "Лежанка", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Шампунь для животных", ProductCategory.ANIMALS_PRODUCTS));
        list.add(new Product(MeasureUnit.PACKAGE, "Наполнитель для кошачьего туалета", ProductCategory.ANIMALS_PRODUCTS));

//        "Консервация" ProductCategory.CONSERVATION
        list.add(new Product(MeasureUnit.UNITS, "Горошек консервированный", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Кукуруза консервированная", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Огурцы соленые", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Огурцы маринованные", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Томаты консервированные", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Оливки (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Маслины (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Фасоль (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Горбуша (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Кальмар (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Кета (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Килька (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Лосось (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Мидии (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Морская капуста (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Пыжьян (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Сайра (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Сардина (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Семга (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Скумбрия (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Тунец (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Треска (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Форель (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Шпроты (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Язь (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Паштет", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Тушенка (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Сгущенка вареная", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Сгущенка с добавками", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Сгущенка цельная", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Сливки(консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Белые грибы (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Грузди (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Маслята (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Моховики (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Опята (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Рыжики (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Шампиньоны (консервы)", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, " Мед гречишный", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Мед липовый", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Мед цветочный", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Варенье", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Джем", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Компот", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Сироп", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, "Фрукты в сиропе", ProductCategory.CONSERVATION));
        list.add(new Product(MeasureUnit.UNITS, " Ягоды протертые", ProductCategory.CONSERVATION));

//        "Мороженое" ProductCategory.ICE_CREAM
        list.add(new Product(MeasureUnit.PACKAGE, "Мороженое", ProductCategory.ICE_CREAM));
        list.add(new Product(MeasureUnit.GRAMM, "Пломбир", ProductCategory.ICE_CREAM));

//        "Диабетическое питание", ProductCategory.DIABETIC_NUTRITION

//        "Готовая кулинария", ProductCategory.COOKED_FOOD

//        "Бытовая химия и товары для дома" ProductCategory.HOUSEHOLD_CHEMICALS
        list.add(new Product(MeasureUnit.PACKAGE, "Пломбир", ProductCategory.HOUSEHOLD_CHEMICALS));

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
}