package com.cookplan.shopping_list.total_list;


import com.cookplan.models.Ingredient;
import com.cookplan.shopping_list.ShoppingListBasePresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public class TotalShoppingListPresenterImpl extends ShoppingListBasePresenterImpl implements TotalShoppingListPresenter {

    private TotalShoppingListView mainView;
//    private DatabaseReference database;

    public TotalShoppingListPresenterImpl(TotalShoppingListView mainView) {
        super();
        this.mainView = mainView;
//        this.database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void setError(String message) {
        if (mainView != null) {
            mainView.setErrorToast(message);
        }
    }

    @Override
    public void sortIngredientList(List<Ingredient> userIngredients) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient ingredient : userIngredients) {
            if (ingredient.isNeedToBuy()) {
                ingredients.add(ingredient);
            }
        }
        if (mainView != null) {
            mainView.setIngredientList(ingredients);
        }
    }
}