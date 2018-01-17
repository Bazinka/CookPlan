package com.cookplan.recipe.edit

import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 16.03.17.
 */

interface EditRecipeView {

    fun setErrorToast(error: String)

    fun showProgressBar()

    fun hideProgressBar()

    fun recipeSavedSuccessfully(recipe: Recipe)

    fun recipeRemovedSuccessfully()
}