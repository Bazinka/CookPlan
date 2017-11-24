package com.cookplan.recipe.grid

import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 21.03.17.
 */

interface RecipeGridView {

    fun setErrorToast(error: String)

    fun setRecipeList(recipeList: List<Recipe>)

    fun setEmptyView()
}
