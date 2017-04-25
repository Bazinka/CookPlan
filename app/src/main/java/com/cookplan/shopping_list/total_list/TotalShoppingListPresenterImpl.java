package com.cookplan.shopping_list.total_list;


import com.cookplan.RApplication;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.ProductCategory;
import com.cookplan.models.ShopListStatus;
import com.cookplan.shopping_list.ShoppingListBasePresenterImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public class TotalShoppingListPresenterImpl extends ShoppingListBasePresenterImpl implements TotalShoppingListPresenter {

    private TotalShoppingListView mainView;
    private Map<String, List<Ingredient>> ProductToIngredientMap;

    public TotalShoppingListPresenterImpl(TotalShoppingListView mainView) {
        super();
        this.mainView = mainView;
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
            if (ingredient.getShopListStatus() != ShopListStatus.NONE) {
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
    }


    @Override
    public void changeShopListStatus(Ingredient totalIngredient, ShopListStatus newStatus) {
        List<Ingredient> ingredientList = ProductToIngredientMap.get(totalIngredient.getName());
        for (Ingredient realIngredient : ingredientList) {
            realIngredient.setShopListStatus(newStatus);
            ingredientDataProvider.updateShopStatus(realIngredient)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null) {
                                mainView.setErrorToast(e.getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void deleteIngredients(List<Ingredient> localIngredients) {
        for (Ingredient totalIngredient : localIngredients) {
            ShopListStatus status = totalIngredient.getShopListStatus();
            List<Ingredient> realIngredients = ProductToIngredientMap.get(totalIngredient.getName());
            for (Ingredient realIngredient : realIngredients) {
                if (status == realIngredient.getShopListStatus()) {
                    realIngredient.setShopListStatus(ShopListStatus.NONE);
                    if (realIngredient.getRecipeId() == null) {
                        ingredientDataProvider.removeIngredient(realIngredient)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (mainView != null && e instanceof CookPlanError) {
                                            mainView.setErrorToast(e.getMessage());
                                        }
                                    }
                                });
                    } else {
                        ingredientDataProvider.updateShopStatus(realIngredient)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (mainView != null) {
                                            mainView.setErrorToast(e.getMessage());
                                        }
                                    }
                                });
                    }
                }
            }
        }
    }
}