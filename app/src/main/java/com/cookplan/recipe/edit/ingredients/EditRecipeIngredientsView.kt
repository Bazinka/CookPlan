package com.cookplan.recipe.edit.ingredients

import com.cookplan.models.Ingredient

/**
 * Created by DariaEfimova on 21.03.17.
 */

interface EditRecipeIngredientsView {

    fun setErrorToast(error: String)

    fun setIngredientList(ingredientList: List<Ingredient>)
}
