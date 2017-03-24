package com.cookplan.shopping_list.total_list;


import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.ShopListStatus;
import com.cookplan.shopping_list.ShoppingListBasePresenterImpl;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public class TotalShoppingListPresenterImpl extends ShoppingListBasePresenterImpl implements TotalShoppingListPresenter {

    private TotalShoppingListView mainView;
    private DatabaseReference database;
    private Map<String, List<Ingredient>> ProductToIngredientMap;

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

        //fill the map
        ProductToIngredientMap = new HashMap<>();
        for (Ingredient ingredient : userIngredients) {
            ArrayList<Ingredient> ingredients;
            if (ProductToIngredientMap.containsKey(ingredient.getName())
                    && ProductToIngredientMap.get(ingredient.getName()) != null) {
                ingredients = (ArrayList<Ingredient>) ProductToIngredientMap.get(ingredient.getName());
            } else {
                ingredients = new ArrayList<>();
            }
            ingredients.add(ingredient);
            ProductToIngredientMap.put(ingredient.getName(), ingredients);
        }

        //calculate amounts
        List<Ingredient> needToBuyIngredients = new ArrayList<>();
        List<Ingredient> alreadyBoughtIngredients = new ArrayList<>();
        for (Map.Entry<String, List<Ingredient>> entry : ProductToIngredientMap.entrySet()) {
            Ingredient needToBuyIng = calculateAmountForEntry(entry, ShopListStatus.NEED_TO_BUY);
            if (needToBuyIng != null) {
                needToBuyIngredients.add(needToBuyIng);
            }

            Ingredient alreadyBoughtIngred = calculateAmountForEntry(entry, ShopListStatus.ALREADY_BOUGHT);
            if (alreadyBoughtIngred != null) {
                alreadyBoughtIngredients.add(alreadyBoughtIngred);
            }
        }

        if (mainView != null) {
            mainView.setIngredientLists(needToBuyIngredients, alreadyBoughtIngredients);
        }
    }

    private Ingredient calculateAmountForEntry(Map.Entry<String, List<Ingredient>> entry, ShopListStatus status) {
        String productName = entry.getKey();
        List<Ingredient> ingredients = entry.getValue();
        if (!ingredients.isEmpty()) {
            Map<MeasureUnit, Double> map = new HashMap<>();
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getShopListStatus() == status && ingredient.getAmount() > 1e-8) {
                    double localAmount;
                    if (map.containsKey(ingredient.getMeasureUnit())) {
                        localAmount = map.get(ingredient.getMeasureUnit()) + ingredient.getAmount();
                    } else {
                        localAmount = ingredient.getAmount();
                    }
                    map.put(ingredient.getMeasureUnit(), localAmount);
                }
            }
            if (map.isEmpty()) {
                return null;
            }

            Double amount = 0.;
            String didntCalculated = "";
            MeasureUnit unit = null;
            for (Map.Entry<MeasureUnit, Double> measureEntry : map.entrySet()) {
                double multiplier;
                if (unit == null) {
                    unit = measureEntry.getKey();
                    multiplier = 1;
                } else {
                    multiplier = MeasureUnit.getMultiplier(measureEntry.getKey(), unit);
                }
                if (multiplier < 1e-8) {
                    String string = measureEntry.getKey().toValueString(measureEntry.getValue());
                    didntCalculated = didntCalculated.isEmpty() ? string : " + " + string;
                } else {
                    amount = amount + multiplier * measureEntry.getValue();
                }
            }

            didntCalculated = didntCalculated.isEmpty() ? didntCalculated : " + " + didntCalculated;
            String amountString = String.valueOf(amount) + " " + unit.toString() + didntCalculated;

            return new Ingredient(productName, amountString, status);
        } else {
            return null;
        }
    }


    @Override
    public void changeShopListStatus(Ingredient ingredient, ShopListStatus newStatus) {
        List<Ingredient> ingredientList = ProductToIngredientMap.get(ingredient.getName());
        DatabaseReference ingredientRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
        for (Ingredient ingred : ingredientList) {
            ingred.setShopListStatus(newStatus);
            ingredientRef
                    .child(ingred.getId())
                    .child(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)
                    .setValue(ingred.getShopListStatus().getId())
                    .addOnFailureListener(e -> {
                        if (mainView != null) {
                            mainView.setErrorToast(e.getLocalizedMessage());
                        }
                    });
//                    .addOnSuccessListener(aVoid -> {
//                        if (mainView != null) {
//                            mainView.setIngredientSuccessfulUpdate(ingredient);
//                        }
//                    });
        }
    }
}