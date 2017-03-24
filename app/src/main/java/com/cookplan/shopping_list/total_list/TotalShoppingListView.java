package com.cookplan.shopping_list.total_list;

import com.cookplan.models.Ingredient;

import java.util.List;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public interface TotalShoppingListView {

    void setErrorToast(String error);

    void setIngredientList(List<Ingredient> ingredientList);
}
