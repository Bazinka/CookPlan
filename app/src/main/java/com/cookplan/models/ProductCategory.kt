package com.cookplan.models

import com.cookplan.R
import java.io.Serializable

/**
 * Created by DariaEfimova on 29.03.17.
 */

enum class ProductCategory private constructor(private val nameResourceId: Int, val colorId: Int, var id: Int) : Serializable {
    WITHOUT_CATEGORY(R.string.without_category_title, R.color.black, 0),
    ALCOHOL_DRINKS(R.string.alcohol_drinks_title, R.color.category_red, 1),
    MILK_PRODUCT(R.string.milk_product_title, R.color.category_pink, 2),
    CHEESE(R.string.cheese_title, R.color.category_dark_pink, 3),
    EGGS(R.string.eggs_title, R.color.category_purple, 4),
    CONFECTIONERY(R.string.confectionery_title, R.color.category_dark_purple, 5),
    CHILD_PRODUCTS(R.string.child_product_title, R.color.category_deep_purple, 6),
    DRINKS_JUICE(R.string.drink_juices_title, R.color.category_dark_deep_purple, 7),
    BAKERY_PRODUCTS(R.string.bakery_products_title, R.color.category_indigo, 8),
    FRUITS_VEGETABLES(R.string.vegetables_fruits_title, R.color.category_dark_indigo, 9),
    GROCERY(R.string.grocery_title, R.color.category_blue, 10),
    MEAT_GARSTRONOMY(R.string.meat_gastronomy_title, R.color.category_dark_blue, 11),
    MEAT_BIRDS(R.string.meat_birds_title, R.color.category_cyan, 12),
    FROZEN_FOOD(R.string.frozen_food_title, R.color.category_dark_cyan, 13),
    FISH_SEAFOOD_CAVIAR(R.string.fish_seafood_caviar_title, R.color.category_teal, 14),
    VEGETABLE_OIL_SAUCE_CONDIMENTS(R.string.vegetable_oils_sauces_and_condiments_title, R.color.category_dark_teal, 15),
    TEA_COFFEE_CACAO(R.string.tea_coffee_cacao_title, R.color.category_green, 16),
    ANIMALS_PRODUCTS(R.string.animals_products_title, R.color.category_dark_green, 17),
    CONSERVATION(R.string.conservation_title, R.color.category_lime, 18),
    ICE_CREAM(R.string.ice_cream_title, R.color.category_dark_lime, 19),
    COOKED_FOOD(R.string.cooked_food_title, R.color.category_yellow, 20),
    HOUSEHOLD_CHEMICALS(R.string.household_chemicals_and_household_goods_title, R.color.category_dark_yellow, 21),
    COSMETICS_HYGIENE(R.string.cosmetics_and_hygiene_title, R.color.category_amber, 22),
    MEDICINES(R.string.medicines_title, R.color.category_dark_amber, 23),
    GAMES_AND_TOYS(R.string.games_and_toys, R.color.category_orange, 24),
    ELECTRIC_AND_HOUSEHOLD_APPLIANCES(R.string.electric_appliances_and_household_appliances_title, R.color.category_dark_orange, 25),
    REPAIR_COTTAGE_REST_PRODUCTS(R.string.repair_cottages_and_rest_products_title, R.color.category_deep_orange, 26),
    CAR_PRODUCTS(R.string.car_products_title, R.color.category_dark_deep_orange, 27),
    WARDROBE(R.string.wardrobe_title, R.color.category_brown, 28),
    STATIONERY(R.string.stationery_title, R.color.category_dark_brown, 29),
    BOOKS_PRINTED_PRODUCTS(R.string.books_and_printed_matter_title, R.color.category_blue_grey, 30),
    GADREN_PRODUCTS(R.string.products_for_garden_title, R.color.category_dark_blue_grey, 31);

    fun getNameResourceId(): Int {
        return nameResourceId
    }

    companion object {

        fun getProductCategoryByName(name: String): ProductCategory {
            if (name == WITHOUT_CATEGORY.name) {
                return WITHOUT_CATEGORY
            }
            if (name == ALCOHOL_DRINKS.name) {
                return ALCOHOL_DRINKS
            }
            if (name == MILK_PRODUCT.name) {
                return MILK_PRODUCT
            }
            if (name == CHEESE.name) {
                return CHEESE
            }
            if (name == EGGS.name) {
                return EGGS
            }
            if (name == CONFECTIONERY.name) {
                return CONFECTIONERY
            }
            if (name == CHILD_PRODUCTS.name) {
                return CHILD_PRODUCTS
            }
            if (name == DRINKS_JUICE.name) {
                return DRINKS_JUICE
            }
            if (name == BAKERY_PRODUCTS.name) {
                return BAKERY_PRODUCTS
            }
            if (name == FRUITS_VEGETABLES.name) {
                return FRUITS_VEGETABLES
            }
            if (name == GROCERY.name) {
                return GROCERY
            }
            if (name == MEAT_GARSTRONOMY.name) {
                return MEAT_GARSTRONOMY
            }
            if (name == MEAT_BIRDS.name) {
                return MEAT_BIRDS
            }
            if (name == FROZEN_FOOD.name) {
                return FROZEN_FOOD
            }
            if (name == FISH_SEAFOOD_CAVIAR.name) {
                return FISH_SEAFOOD_CAVIAR
            }
            if (name == VEGETABLE_OIL_SAUCE_CONDIMENTS.name) {
                return VEGETABLE_OIL_SAUCE_CONDIMENTS
            }
            if (name == TEA_COFFEE_CACAO.name) {
                return TEA_COFFEE_CACAO
            }
            if (name == ANIMALS_PRODUCTS.name) {
                return ANIMALS_PRODUCTS
            }
            if (name == CONSERVATION.name) {
                return CONSERVATION
            }
            if (name == ICE_CREAM.name) {
                return ICE_CREAM
            }
            if (name == MEDICINES.name) {
                return MEDICINES
            }
            if (name == COOKED_FOOD.name) {
                return COOKED_FOOD
            }
            if (name == HOUSEHOLD_CHEMICALS.name) {
                return HOUSEHOLD_CHEMICALS
            }
            if (name == COSMETICS_HYGIENE.name) {
                return COSMETICS_HYGIENE
            }
            if (name == GAMES_AND_TOYS.name) {
                return GAMES_AND_TOYS
            }
            if (name == ELECTRIC_AND_HOUSEHOLD_APPLIANCES.name) {
                return ELECTRIC_AND_HOUSEHOLD_APPLIANCES
            }
            if (name == REPAIR_COTTAGE_REST_PRODUCTS.name) {
                return REPAIR_COTTAGE_REST_PRODUCTS
            }
            if (name == CAR_PRODUCTS.name) {
                return CAR_PRODUCTS
            }
            if (name == WARDROBE.name) {
                return WARDROBE
            }
            if (name == STATIONERY.name) {
                return STATIONERY
            }
            if (name == BOOKS_PRINTED_PRODUCTS.name) {
                return BOOKS_PRINTED_PRODUCTS
            }
            return if (name == GADREN_PRODUCTS.name) {
                GADREN_PRODUCTS
            } else WITHOUT_CATEGORY
        }
    }
}
