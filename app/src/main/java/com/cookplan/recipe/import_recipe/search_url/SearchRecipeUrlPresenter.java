package com.cookplan.recipe.import_recipe.search_url;

/**
 * Created by DariaEfimova on 10.06.17.
 */

public interface SearchRecipeUrlPresenter {

    void searchRecipes(String query);

    void loadNextPart(int offset);

    void onStop();
}
