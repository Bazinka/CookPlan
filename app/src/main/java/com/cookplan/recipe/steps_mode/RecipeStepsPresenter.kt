package com.cookplan.recipe.steps_mode

import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 13.04.17.
 */

interface RecipeStepsPresenter {

    fun getRecipeSteps(recipe: Recipe): List<String>
}
