package com.cookplan.recipe_new.add_ingredients;

import com.cookplan.models.Ingredient;

import java.util.List;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface NewRecipeIngredientsView {

    void setErrorToast(String error);

    void setIngredientList(List<Ingredient> ingredientList);
}
