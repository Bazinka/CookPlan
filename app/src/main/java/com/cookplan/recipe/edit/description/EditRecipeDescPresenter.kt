package com.cookplan.recipe.edit.description

import android.net.Uri

/**
 * Created by DariaEfimova on 16.03.17.
 */

interface EditRecipeDescPresenter {

    fun getOutputImagePath(): Uri?

    fun doOCR(language: String)
}
