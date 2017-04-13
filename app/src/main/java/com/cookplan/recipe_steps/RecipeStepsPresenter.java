package com.cookplan.recipe_steps;

import com.cookplan.models.Recipe;

import java.util.List;

/**
 * Created by DariaEfimova on 13.04.17.
 */

public interface RecipeStepsPresenter {

    public List<String> getRecipeSteps(Recipe recipe);
}
