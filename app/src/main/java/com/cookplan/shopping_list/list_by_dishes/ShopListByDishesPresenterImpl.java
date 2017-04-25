package com.cookplan.shopping_list.list_by_dishes;


import android.content.Context;

import com.cookplan.R;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.cookplan.providers.impl.RecipeProviderImpl;
import com.cookplan.shopping_list.ShoppingListBasePresenterImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public class ShopListByDishesPresenterImpl extends ShoppingListBasePresenterImpl implements ShopListByDishPresenter {

    private static final String WITHOUT_RECIPE_KEY = "without_recipe";
    private ShopListByDishesView mainView;
    private Map<String, List<Ingredient>> recipeIdToIngredientMap;

    private Context context;

    private RecipeProvider recipeDataProvider;
    private IngredientProvider ingredientDataProvider;

    public ShopListByDishesPresenterImpl(ShopListByDishesView mainView, Context context) {
        super();
        this.mainView = mainView;
        this.context = context;
        this.recipeDataProvider = new RecipeProviderImpl();
        this.ingredientDataProvider = new IngredientProviderImpl();
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
        recipeIdToIngredientMap = new HashMap<>();
        for (Ingredient ingredient : userIngredients) {
            if (ingredient.getShopListStatus() != ShopListStatus.NONE) {
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                String key = ingredient.getRecipeId() != null ? ingredient.getRecipeId() : WITHOUT_RECIPE_KEY;
                if (recipeIdToIngredientMap.containsKey(key)) {
                    ingredients = (ArrayList<Ingredient>) recipeIdToIngredientMap.get(key);
                }
                ingredients.add(ingredient);
                recipeIdToIngredientMap.put(key, ingredients);
            }
        }
        if (recipeIdToIngredientMap.size() == 0) {
            if (mainView != null) {
                mainView.setEmptyView();
            }
        } else {
            Map<Recipe, List<Ingredient>> recipeToIngredientsMap = new HashMap<>();
            for (Map.Entry<String, List<Ingredient>> entry : recipeIdToIngredientMap.entrySet()) {
                recipeDataProvider.getRecipeById(entry.getKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Recipe>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Recipe recipe) {
                                if (recipe.getId() != null) {
                                    recipeToIngredientsMap.put(recipe, recipeIdToIngredientMap.get(recipe.getId()));
                                } else {
                                    recipe = new Recipe(context.getString(R.string.without_recipe_title),
                                                        context.getString(R.string.recipe_desc_is_not_needed_title));
                                    recipeToIngredientsMap.put(recipe, recipeIdToIngredientMap.get(WITHOUT_RECIPE_KEY));
                                }
                                if (recipeToIngredientsMap.keySet().size() == recipeIdToIngredientMap.keySet().size()) {
                                    makeValidDataForTheView(recipeToIngredientsMap);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mainView != null && e instanceof CookPlanError) {
                                    setError(e.getMessage());
                                }
                            }
                        });
            }
        }
    }

    private void makeValidDataForTheView(Map<Recipe, List<Ingredient>> recipeToIngredientsMap) {
        if (mainView != null) {
            Map<String, List<Ingredient>> recipeIdsToIngredientMap = new HashMap<>(mainView.getExistedRecipeIdsToingredientsMap());
            List<Recipe> recipeList = new ArrayList<>(mainView.getExistedRecipeList());

            // remove deleted elements
            List<Recipe> needToDeleteRecipeList = new ArrayList<>();
            for (Recipe recipe : recipeList) {
                boolean wasFound = false;
                for (Map.Entry<Recipe, List<Ingredient>> entry : recipeToIngredientsMap.entrySet()) {
                    if (entry.getValue() != null) {
                        if (isRecipeIdsEqual(recipe, entry.getKey())) {
                            wasFound = true;
                            String recipeId = recipe.getId() == null ? WITHOUT_RECIPE_KEY : recipe.getId();
                            List<Ingredient> ingredients = updateIngredientsList(
                                    recipeIdsToIngredientMap.get(recipeId), entry.getValue());
                            recipeIdsToIngredientMap.put(recipeId, ingredients);
                            break;
                        }
                    }
                }
                if (!wasFound) {
                    needToDeleteRecipeList.add(recipe);
                }
            }
            for (Recipe recipe : needToDeleteRecipeList) {
                recipeList.remove(recipe);
                recipeIdsToIngredientMap.remove(recipe.getId() == null ? WITHOUT_RECIPE_KEY : recipe.getId());
            }

            // add new elements
            for (Map.Entry<Recipe, List<Ingredient>> entry : recipeToIngredientsMap.entrySet()) {
                boolean wasFound = false;
                for (Recipe recipe : recipeList) {
                    if (entry.getValue() != null) {
                        if (isRecipeIdsEqual(recipe, entry.getKey())) {
                            wasFound = true;
                            break;
                        }
                    }
                }
                if (!wasFound && entry.getValue() != null) {
                    if (entry.getKey() == null || entry.getKey().getId() == null) {
                        recipeList.add(new Recipe(context.getString(R.string.without_recipe_title),
                                                  context.getString(R.string.recipe_desc_is_not_needed_title)));
                        recipeIdsToIngredientMap.put(WITHOUT_RECIPE_KEY, entry.getValue());

                    } else {
                        recipeList.add(entry.getKey());
                        recipeIdsToIngredientMap.put(entry.getKey().getId(), entry.getValue());
                    }
                }
            }

            mainView.setIngredientListToRecipe(recipeList, recipeIdsToIngredientMap);
        }
    }

    private boolean isRecipeIdsEqual(Recipe recipeFromList, Recipe recipeFromMap) {
        boolean isEqual = false;
        if (recipeFromMap == null || recipeFromMap.getId() == null) {
            if (recipeFromList.getName().equals(context.getString(R.string.without_recipe_title))) {
                isEqual = true;
            }
        } else if (recipeFromList.getId() != null && recipeFromList.getId().equals(recipeFromMap.getId())) {
            isEqual = true;
        }

        return isEqual;
    }

    private List<Ingredient> updateIngredientsList(List<Ingredient> oldIngredients,
                                                   List<Ingredient> newIngredients) {
        List<Ingredient> ingredients = new ArrayList<>(oldIngredients);

        //remove deleted ingredients
        List<Ingredient> needToDeleteIngredients = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            boolean wasFound = false;
            for (Ingredient newIngredient : newIngredients) {
                if (ingredient.getName().equals(newIngredient.getName())) {
                    wasFound = true;
                    ingredient.setName(newIngredient.getName());
                    ingredient.setShopListStatus(newIngredient.getShopListStatus());
                    break;
                }
            }
            if (!wasFound) {
                needToDeleteIngredients.add(ingredient);
            }
        }
        for (Ingredient ingredient : needToDeleteIngredients) {
            ingredients.remove(ingredient);
        }


        //add new ingredients
        for (Ingredient newIngredient : newIngredients) {
            boolean wasFound = false;
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getName().equals(newIngredient.getName())) {
                    wasFound = true;
                    break;
                }
            }
            if (!wasFound) {
                ingredients.add(newIngredient);
            }
        }

        return ingredients;
    }

    @Override
    public void setIngredientBought(Ingredient ingredient, ShopListStatus newStatus) {
        if (newStatus != ShopListStatus.NONE) {
            ingredient.setShopListStatus(newStatus);
            ingredientDataProvider.updateShopStatus(ingredient)
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
    public void setRecipeIngredBought(Recipe recipe, List<Ingredient> ingredientList) {
        boolean isNeedToRemove = recipe.getId() == null;
        for (Ingredient ingred : ingredientList) {
            ingred.setShopListStatus(ShopListStatus.NONE);
            if (isNeedToRemove) {
                ingredientDataProvider.removeIngredient(ingred)
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
                ingredientDataProvider.updateShopStatus(ingred)
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