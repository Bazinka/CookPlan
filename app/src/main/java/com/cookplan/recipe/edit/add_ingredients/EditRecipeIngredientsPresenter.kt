package com.cookplan.recipe.edit.add_ingredients

import com.cookplan.models.Ingredient

/**
 * Created by DariaEfimova on 21.03.17.
 */

interface EditRecipeIngredientsPresenter {
    fun getAsyncIngredientList()

    fun removeIngredient(ingredient: Ingredient)

    fun onStop()
}
