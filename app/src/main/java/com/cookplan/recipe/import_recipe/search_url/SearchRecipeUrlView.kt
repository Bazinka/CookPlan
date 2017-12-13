package com.cookplan.recipe.import_recipe.search_url

import com.cookplan.models.network.GoogleRecipe

/**
 * Created by DariaEfimova on 10.06.17.
 */

interface SearchRecipeUrlView {
    fun setResultGoogleSearchList(googleRecipes: List<GoogleRecipe>)

    fun setError(errorResource: Int)
}