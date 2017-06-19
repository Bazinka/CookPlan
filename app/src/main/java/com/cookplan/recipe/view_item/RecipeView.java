package com.cookplan.recipe.view_item;

import com.cookplan.models.Ingredient;

import java.util.List;

/**
 * Created by DariaEfimova on 23.03.17.
 */

public interface RecipeView {
    void setErrorToast(String error);

    void setIngredientList(List<Ingredient> ingredientList);

    void setIngredientSuccessfulUpdate(Ingredient ingredient);

}
