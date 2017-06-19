package com.cookplan.recipe.view_item;

import com.cookplan.models.Ingredient;

import java.util.List;

/**
 * Created by DariaEfimova on 23.03.17.
 */

public interface RecipeViewPresenter {

    public void getIngredientList();

    public void addIngredientToShoppingList(Ingredient ingredient);

    public void addAllIngredientToShoppingList(List<Ingredient> ingredients);

    public void onStop();
}
