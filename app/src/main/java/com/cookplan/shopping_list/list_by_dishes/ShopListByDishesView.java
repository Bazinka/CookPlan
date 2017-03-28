package com.cookplan.shopping_list.list_by_dishes;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.shopping_list.ShoppingListView;

import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public interface ShopListByDishesView extends ShoppingListView {

    public void setIngredientListToRecipe(Map<Recipe, List<Ingredient>> RecipeToingredientsMap);
}
