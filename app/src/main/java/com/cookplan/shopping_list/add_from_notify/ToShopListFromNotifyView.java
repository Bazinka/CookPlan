package com.cookplan.shopping_list.add_from_notify;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import java.util.List;

/**
 * Created by DariaEfimova on 11.07.17.
 */

public interface ToShopListFromNotifyView {

    void setError(String error);

    void setResults(Recipe recipe, List<Ingredient> ingredientList);
}
