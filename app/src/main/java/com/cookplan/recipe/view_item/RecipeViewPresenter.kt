package com.cookplan.recipe.view_item

import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.models.ShopListStatus

/**
 * Created by DariaEfimova on 23.03.17.
 */

interface RecipeViewPresenter {

    fun getRecipeObject(): Recipe

    fun getIngredientList()

    fun addIngredientToShoppingList(ingredient: Ingredient)

    fun changeIngredListShopStatus(ingredients: List<Ingredient>, status: ShopListStatus)

    fun onStop()
}
