package com.cookplan.recipe.edit.description

/**
 * Created by DariaEfimova on 16.03.17.
 */

interface EditRecipeDescView {

    fun setErrorToSnackBar(error: String)

    fun setAsyncErrorToSnackBar(error: String)

    fun setAsyncTextResult(result: String)
}
