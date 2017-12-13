package com.cookplan.recipe.import_recipe.search_url

/**
 * Created by DariaEfimova on 10.06.17.
 */

interface SearchRecipeUrlPresenter {

    fun searchRecipes(query: String)

    fun loadNextPart(offset: Int)

    fun onStop()
}
