package com.cookplan.shopping_list.list_by_dishes

import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.models.ShopListStatus
import com.cookplan.shopping_list.ShoppingListBasePresenter

/**
 * Created by DariaEfimova on 24.03.17.
 */

interface ShopListByDishPresenter : ShoppingListBasePresenter {

    fun changeIngredientStatus(ingredient: Ingredient, newStatus: ShopListStatus)

    fun recipeIngredBought(recipe: Recipe, ingredientList: List<Ingredient>)
}