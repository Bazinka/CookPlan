package com.cookplan.recipe_grid;

import com.cookplan.models.Recipe;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface RecipeGridPresenter {

    public void getRecipeList();

    public void removeRecipe(Recipe recipe);

    void onStop();
}
