package com.cookplan.recipe.steps_mode

import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 13.04.17.
 */

class RecipeStepsPresenterImpl : RecipeStepsPresenter {

    override fun getRecipeSteps(recipe: Recipe): List<String> {
        val list = recipe.desc.split("\n")//[\r\n]+
        return list.filter { !it.isEmpty() }
    }
}
