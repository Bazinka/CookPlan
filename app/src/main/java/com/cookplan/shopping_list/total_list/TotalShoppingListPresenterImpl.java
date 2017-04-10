package com.cookplan.shopping_list.total_list;


import com.cookplan.RApplication;
import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.ProductCategory;
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
        for (Map.Entry<String, List<Ingredient>> entry : ProductToIngredientMap.entrySet()) {
            Ingredient needToBuyIng = calculateAmountForEntry(entry, ShopListStatus.NEED_TO_BUY);
            if (needToBuyIng != null) {
                needToBuyIngredients.add(needToBuyIng);
            }

            Ingredient alreadyBoughtIngred = calculateAmountForEntry(entry, ShopListStatus.ALREADY_BOUGHT);
            if (alreadyBoughtIngred != null) {
                needToBuyIngredients.add(alreadyBoughtIngred);
            }
        }
        List<Ingredient> needToBuySortedIngredients = new ArrayList<>();
        for (ProductCategory category : RApplication.getPriorityList()) {
            for (Ingredient ingredient : needToBuyIngredients) {
                if (ingredient.getCategory() == category) {
                    needToBuySortedIngredients.add(ingredient);
                }
            }
        }
        if (mainView != null) {
            if (ProductToIngredientMap.size() != 0) {
                mainView.setIngredientLists(needToBuySortedIngredients);
            } else {
                mainView.setEmptyView();
            }
        }
    }

    private Ingredient calculateAmountForEntry(Map.Entry<String, List<Ingredient>> entry, ShopListStatus status) {
        String productName = entry.getKey();
        List<Ingredient> ingredients = entry.getValue();
        if (!ingredients.isEmpty()) {
            ProductCategory category = ingredients.get(0).getCategory();
            Map<MeasureUnit, Double> shopMap = new HashMap<>();
            Map<MeasureUnit, Double> restMap = new HashMap<>();
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getShopListStatus() == status
                        && ingredient.getShopMeasureList() != null
                        && ingredient.getShopAmountList() != null) {
                    double localAmount;
                    MeasureUnit unit = null;
                    double amount = -1.;
                    for (int i = 0; i < ingredient.getShopMeasureList().size(); i++) {
                        MeasureUnit shopUnit = ingredient.getShopMeasureList().get(i);
                        double shopAmount = ingredient.getShopAmountList().get(i);
                        if (shopAmount > 1e-8) {
                            unit = shopUnit;
                            amount = shopAmount;
                            if (shopMap.containsKey(unit)) {
                                localAmount = shopMap.get(unit) + amount;
                            } else {
                                localAmount = amount;
                            }
                            shopMap.put(unit, localAmount);
                        }
                    }

                    if (amount < 1e-8 && unit == null) {
                        unit = ingredient.getMainMeasureUnit();
                        amount = ingredient.getMainAmount();
                        if (restMap.containsKey(unit)) {
                            localAmount = restMap.get(unit) + amount;
                        } else {
                            localAmount = amount;
                        }
                        restMap.put(unit, localAmount);
                    }
                }
            }

            if (restMap.isEmpty() && shopMap.isEmpty()) {
                return null;
            }
            String shopAmountString = "";
            for (Map.Entry<MeasureUnit, Double> shopEntry : shopMap.entrySet()) {
                String string = shopEntry.getKey().toValueString(shopEntry.getValue());
                if (shopAmountString.isEmpty()) {
                    shopAmountString = string;
                } else {
                    shopAmountString = shopAmountString + " (" + string + ")";
                }
            }
            String restAmountString = "";
            for (Map.Entry<MeasureUnit, Double> measureEntry : restMap.entrySet()) {
                String string = measureEntry.getKey().toValueString(measureEntry.getValue());
                restAmountString = restAmountString.isEmpty() ? string : restAmountString + " + " + string;
            }
            if (!restAmountString.isEmpty()) {
                shopAmountString = shopAmountString + " + " + restAmountString;
            }
            return new Ingredient(productName, shopAmountString, status, category);
        } else {
            return null;
        }


        //        if (!ingredients.isEmpty()) {
        //            Map<MeasureUnit, Double> map = new HashMap<>();
        //            for (Ingredient ingredient : ingredients) {
        //                if (ingredient.getShopListStatus() == status) {
        //                    double localAmount;
        //                    MeasureUnit unit = ingredient.getMainMeasureUnit();
        //                    double amount = ingredient.getMainAmount();
        //                    if (ingredient.getShopListAmount() > 1e-8 && ingredient.getShopListMeasureUnit() != null) {
        //                        unit = ingredient.getShopListMeasureUnit();
        //                        amount = ingredient.getShopListAmount();
        //                    }
        //                    if (map.containsKey(ingredient.getShopListMeasureUnit())) {
        //                        localAmount = map.get(ingredient.getShopListMeasureUnit()) + ingredient.getShopListAmount();
        //                    } else {
        //                        localAmount = ingredient.getShopListAmount();
        //                    }
        //                    map.put(ingredient.getShopListMeasureUnit(), localAmount);
        //                }
        //            }
        //            if (map.isEmpty()) {
        //                return null;
        //            }
        //
        //            Double amount = 0.;
        //            String didntCalculated = "";
        //            MeasureUnit unit = null;
        //            for (Map.Entry<MeasureUnit, Double> measureEntry : map.entrySet()) {
        //                if (measureEntry.getValue() > 1e-8) {
        //                    double multiplier;
        //                    if (unit == null) {
        //                        unit = measureEntry.getKey();
        //                        multiplier = 1;
        //                    } else {
        //                        multiplier = MeasureUnit.getMultiplier(measureEntry.getKey(), unit);
        //                    }
        //                    if (multiplier < 1e-8) {
        //                        String string = measureEntry.getKey().toValueString(measureEntry.getValue());
        //                        didntCalculated = didntCalculated.isEmpty() ? string : " + " + string;
        //                    } else {
        //                        amount = amount + multiplier * measureEntry.getValue();
        //                    }
        //                }
        //            }
        //
        //            didntCalculated = didntCalculated.isEmpty() ? didntCalculated : " + " + didntCalculated;
        //            String amountString = null;
        //            if (amount > 1e-8) {
        //                amountString = String.valueOf(amount) + " " + unit.toString() + didntCalculated;
        //            }
        //            return new Ingredient(productName, amountString, status);
        //        } else {
        //            return null;
        //        }
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
                    .setValue(ingred.getShopListStatus())
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