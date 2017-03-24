package com.cookplan.shopping_list.total_list;


import com.cookplan.models.Ingredient;
import com.cookplan.models.ShopListStatus;
import com.cookplan.shopping_list.ShoppingListBasePresenterImpl;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public class TotalShoppingListPresenterImpl extends ShoppingListBasePresenterImpl implements TotalShoppingListPresenter {

    private TotalShoppingListView mainView;
    private DatabaseReference database;

    public TotalShoppingListPresenterImpl(TotalShoppingListView mainView) {
        super();
        this.mainView = mainView;
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void setError(String message) {
        if (mainView != null) {
            mainView.setErrorToast(message);
        }
    }

    @Override
    public void sortIngredientList(List<Ingredient> userIngredients) {
        List<Ingredient> needToBuyIngredients = new ArrayList<>();
        List<Ingredient> alreadyBoughtIngredients = new ArrayList<>();
        for (Ingredient ingredient : userIngredients) {
            if (ingredient.getShopListStatus() == ShopListStatus.NEED_TO_BUY) {
                needToBuyIngredients.add(ingredient);
            }
            if (ingredient.getShopListStatus() == ShopListStatus.ALREADY_BOUGHT) {
                alreadyBoughtIngredients.add(ingredient);
            }
        }
        if (mainView != null) {
            mainView.setIngredientLists(needToBuyIngredients, alreadyBoughtIngredients);
        }
    }

    @Override
    public void changeShopListStatus(Ingredient ingredient, ShopListStatus newStatus) {
        ingredient.setShopListStatus(newStatus);
        DatabaseReference ingredientRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
        ingredientRef.child(ingredient.getId())
                .child(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)
                .setValue(ingredient.getIngredientDBObject().getShopListStatusId())
                .addOnFailureListener(e -> {
                    if (mainView != null) {
                        mainView.setErrorToast(e.getLocalizedMessage());
                    }
                })
                .addOnSuccessListener(aVoid -> {
                    if (mainView != null) {
                        mainView.setIngredientSuccessfulUpdate(ingredient);
                    }
                });
    }
}