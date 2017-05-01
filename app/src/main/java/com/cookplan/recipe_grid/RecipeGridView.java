package com.cookplan.recipe_grid;

import com.cookplan.models.Recipe;
import com.cookplan.share.ShareView;

import java.util.List;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface RecipeGridView extends ShareView{

    void setErrorToast(String error);

    void setRecipeList(List<Recipe> ingredientList);

    void setEmptyView();
}
