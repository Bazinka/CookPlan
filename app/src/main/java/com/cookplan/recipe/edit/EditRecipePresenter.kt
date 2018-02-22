package com.cookplan.recipe.edit

import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 16.03.17.
 */

interface EditRecipePresenter {

    fun saveRecipe(recipe: Recipe)

    fun removeRecipe(recipe: Recipe, ingredients: List<Ingredient> = listOf())
}