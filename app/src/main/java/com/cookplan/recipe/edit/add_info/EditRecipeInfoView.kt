package com.cookplan.recipe.edit.add_info

import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 16.03.17.
 */

interface EditRecipeInfoView {

    fun setErrorToSnackBar(error: String)

    fun setAsyncErrorToSnackBar(error: String)

    fun setErrorToast(error: String)

    fun setAsyncTextResult(result: String)

    fun showProgressBar()

    fun hideProgressBar()

    fun setNextActivity(recipe: Recipe)
}
