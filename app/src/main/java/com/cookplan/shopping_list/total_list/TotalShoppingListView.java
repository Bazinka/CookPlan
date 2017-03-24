package com.cookplan.shopping_list.total_list;

import com.cookplan.models.Ingredient;
import com.cookplan.shopping_list.ShoppingListView;

import java.util.List;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public interface TotalShoppingListView extends ShoppingListView {

    void setIngredientSuccessfulUpdate(Ingredient ingredient);

    void setIngredientLists(List<Ingredient> needToBuyIngredientList,
                            List<Ingredient> alreadyBoughtIngredientList);
}
