package com.cookplan.shopping_list;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Product;

import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public interface ShoppingListView {

    void setErrorToast(String error);

    void setProductsList(List<Product> productsList);

    void setIngredientList(List<Ingredient> ingredientList);

}
