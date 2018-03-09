package com.cookplan.recipe.import_recipe.approve_result

import com.cookplan.BaseView
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 08.06.17.
 */

interface ImportRecipeView : BaseView {

    fun setImportResult(recipe: Recipe, recipeToingredientsMap: MutableMap<String, List<Ingredient>>)

    fun setError(s: String)

    fun setRecipeSavedSuccessfully(recipe: Recipe)

    fun setIngredientSavedSuccessfully(key: String)
}
