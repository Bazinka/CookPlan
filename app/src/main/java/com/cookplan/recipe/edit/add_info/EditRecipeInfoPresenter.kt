package com.cookplan.recipe.edit.add_info

import android.net.Uri

import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 16.03.17.
 */

interface EditRecipeInfoPresenter {

    fun getOutputImagePath(): Uri?

    fun doOCR(language: String)

    fun saveRecipe(recipe: Recipe?, newName: String, newDesc: String)
}
