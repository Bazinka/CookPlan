package com.cookplan.shopping_list.add_from_notify;

import com.cookplan.models.Ingredient;

import java.util.List;

/**
 * Created by DariaEfimova on 11.07.17.
 */

public interface ToShopListFromNotifyPresenter {

    void getItems(List<String> recipeIds, List<String> ingredientsIds);

    void onStop();

    void setIngredientsNeedToBuy(List<Ingredient> ingredientList);
}
