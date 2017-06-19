package com.cookplan.recipe.steps_mode;

import com.cookplan.models.Recipe;

import java.util.Arrays;
import java.util.List;

/**
 * Created by DariaEfimova on 13.04.17.
 */

public class RecipeStepsPresenterImpl implements RecipeStepsPresenter {
    public RecipeStepsPresenterImpl() {
    }

    @Override
    public List<String> getRecipeSteps(Recipe recipe) {
        String[] lines = recipe.getDesc().split("[\\r\\n]+");
        return Arrays.asList(lines);
    }
}
