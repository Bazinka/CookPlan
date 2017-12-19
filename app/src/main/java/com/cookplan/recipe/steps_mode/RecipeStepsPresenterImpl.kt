package com.cookplan.recipe.steps_mode

import com.cookplan.models.Recipe
import com.cookplan.recipe.import_recipe.parser.Parser
import com.cookplan.utils.Utils

/**
 * Created by DariaEfimova on 13.04.17.
 */

class RecipeStepsPresenterImpl : RecipeStepsPresenter {

    override fun getRecipeSteps(recipe: Recipe): List<String> {
        val list = recipe.desc.split(Utils.SEPARATOR_IN_TEXT)
        return list.filter { !it.isEmpty() }
    }
}
