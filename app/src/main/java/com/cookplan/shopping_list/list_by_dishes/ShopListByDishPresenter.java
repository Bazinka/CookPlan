package com.cookplan.shopping_list.list_by_dishes;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;
import com.cookplan.shopping_list.ShoppingListBasePresenter;

import java.util.List;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public interface ShopListByDishPresenter extends ShoppingListBasePresenter {

    public void setIngredientBought(Ingredient ingredient, ShopListStatus newStatus);

    public void setRecipeIngredBought(Recipe recipe, List<Ingredient> ingredientList);
}