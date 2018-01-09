package com.cookplan.recipe.edit

import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 16.03.17.
 */

interface EditRecipePresenter {

    fun saveRecipe(recipe: Recipe?, newName: String = String(), newDesc: String)
}