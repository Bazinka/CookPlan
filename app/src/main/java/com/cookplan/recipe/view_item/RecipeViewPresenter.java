package com.cookplan.recipe.view_item;

import com.cookplan.models.Ingredient;
import com.cookplan.models.ShopListStatus;

import java.util.List;

/**
 * Created by DariaEfimova on 23.03.17.
 */

public interface RecipeViewPresenter {

    public void getIngredientList();

    public void addIngredientToShoppingList(Ingredient ingredient);

    public void changeIngredListShopStatus(List<Ingredient> ingredients, ShopListStatus status);

    public void onStop();
}
