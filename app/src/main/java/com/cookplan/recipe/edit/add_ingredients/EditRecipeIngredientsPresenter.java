package com.cookplan.recipe.edit.add_ingredients;

import com.cookplan.models.Ingredient;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface EditRecipeIngredientsPresenter {
    public void getAsyncIngredientList();

    public void removeIngredient(Ingredient ingredient);

    void onStop();
}
