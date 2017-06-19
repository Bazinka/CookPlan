package com.cookplan.recipe.edit.add_ingredients;

import com.cookplan.models.Ingredient;

import java.util.List;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface EditRecipeIngredientsView {

    void setErrorToast(String error);

    void setIngredientList(List<Ingredient> ingredientList);
}
