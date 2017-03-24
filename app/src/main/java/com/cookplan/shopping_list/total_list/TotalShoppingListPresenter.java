package com.cookplan.shopping_list.total_list;

import com.cookplan.models.Ingredient;
import com.cookplan.models.ShopListStatus;
import com.cookplan.shopping_list.ShoppingListBasePresenter;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public interface TotalShoppingListPresenter extends ShoppingListBasePresenter {

    public void changeShopListStatus(Ingredient ingredient, ShopListStatus newStatus);
}
