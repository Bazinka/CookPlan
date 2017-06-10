package com.cookplan.recipe_import.search_url;

import com.cookplan.models.network.GoogleRecipe;

import java.util.List;

/**
 * Created by DariaEfimova on 10.06.17.
 */

public interface SearchRecipeUrlView {
    void setResultGoogleSearchList(List<GoogleRecipe> googleRecipes);
}
