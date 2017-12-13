package com.cookplan.recipe.import_recipe.parser

import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 09.06.17.
 */

interface ParserResultListener {

    fun onSuccess(recipe: Recipe, ingredientList: MutableMap<String, List<Ingredient>>)

    fun onError(error: String)
}