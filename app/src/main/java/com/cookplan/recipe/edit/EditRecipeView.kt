package com.cookplan.recipe.edit

import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 16.03.17.
 */

interface EditRecipeView {

    fun setErrorToast(errorString: String)

    fun setErrorToast(errorId: Int)

    fun showProgressBar()

    fun hideProgressBar()

    fun recipeSavedSuccessfully(recipe: Recipe)

    fun recipeRemovedSuccessfully()
}