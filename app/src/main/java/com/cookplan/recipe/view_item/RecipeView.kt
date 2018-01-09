package com.cookplan.recipe.view_item

import com.cookplan.models.Ingredient

/**
 * Created by DariaEfimova on 23.03.17.
 */

interface RecipeView {

    fun setErrorToast(error: String)

    fun setIngredientList(ingredientList: List<Ingredient>)

    fun setIngredientSuccessfulUpdate(ingredient: Ingredient)

    fun ingredListChangedShoplistStatus(isRemoved: Boolean)
}
