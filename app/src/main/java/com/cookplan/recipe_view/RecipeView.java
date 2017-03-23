package com.cookplan.recipe_view;

import com.cookplan.models.Ingredient;

import java.util.List;

/**
 * Created by DariaEfimova on 23.03.17.
 */

public interface RecipeView {
    void setErrorToast(String error);

    void setIngredientList(List<Ingredient> ingredientList);
}
