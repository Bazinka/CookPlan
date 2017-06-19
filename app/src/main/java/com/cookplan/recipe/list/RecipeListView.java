package com.cookplan.recipe.list;

import com.cookplan.models.Recipe;

import java.util.List;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface RecipeListView {

    void setErrorToast(String error);

    void setRecipeList(List<Recipe> ingredientList);

    void setEmptyView();
}
