package com.cookplan.recipe.list;

import com.cookplan.models.Recipe;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface RecipeListPresenter {

    public void getRecipeList();

    public void removeRecipe(Recipe recipe);

    void onStop();
}
