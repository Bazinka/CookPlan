package com.cookplan.recipe_view;

import com.cookplan.models.Ingredient;

/**
 * Created by DariaEfimova on 23.03.17.
 */

public interface RecipeViewPresenter {

    public void getIngredientList();

    public void saveSelectIngredientList(Ingredient ingredient);
}
