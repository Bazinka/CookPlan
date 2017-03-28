package com.cookplan.shopping_list.list_by_dishes;


import android.content.Context;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;
import com.cookplan.shopping_list.ShoppingListBasePresenterImpl;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public class ShopListByDishesPresenterImpl extends ShoppingListBasePresenterImpl implements ShopListByDishPresenter {

    private static final String WITHOUT_RECIPE_KEY = "without_recipe";
    private ShopListByDishesView mainView;
    private DatabaseReference database;
    private Map<String, List<Ingredient>> recipeIdToIngredientMap;

    private Context context;

    public ShopListByDishesPresenterImpl(ShopListByDishesView mainView, Context context) {
        super();
        this.mainView = mainView;
        this.context = context;
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
                Query items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE).child(entry.getKey());
                items.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Recipe recipe = Recipe.getRecipeFromDBObject(dataSnapshot);
                            if (!recipeToIngredientsMap.containsKey(recipe)) {
                                recipeToIngredientsMap.put(recipe, recipeIdToIngredientMap.get(recipe.getId()));
                            }
                        } else {
                            Recipe recipe = new Recipe(context.getString(R.string.without_recipe_title),
                                    context.getString(R.string.recipe_desc_is_not_needed_title));
                            if (!recipeToIngredientsMap.containsKey(recipe)) {
                                recipeToIngredientsMap.put(recipe, recipeIdToIngredientMap.get(WITHOUT_RECIPE_KEY));
                            }
                        }
                        if (recipeToIngredientsMap.keySet().size() == recipeIdToIngredientMap.keySet().size()
                                && mainView != null) {
                            mainView.setIngredientListToRecipe(recipeToIngredientsMap);
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        setError(databaseError.getMessage());
                    }
                });
            }
        }
    }


    @Override
    public void setIngredientBought(Ingredient ingredient, ShopListStatus newStatus) {
        if (newStatus != ShopListStatus.NONE) {
            DatabaseReference ingredientRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
            ingredient.setShopListStatus(newStatus);
            ingredientRef
                    .child(ingredient.getId())
                    .child(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)
                    .setValue(ingredient.getShopListStatus().getId())
                    .addOnFailureListener(e -> {
                        if (mainView != null) {
                            mainView.setErrorToast(e.getLocalizedMessage());
                        }
                    });
        }
    }

    @Override
    public void setRecipeIngredBought(Recipe recipe, List<Ingredient> ingredientList) {
        boolean isNeedToRemove = recipe.getId() == null;
        DatabaseReference ingredientRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
        for (Ingredient ingred : ingredientList) {
            ingred.setShopListStatus(ShopListStatus.NONE);
            DatabaseReference ref = ingredientRef.child(ingred.getId());
            if (isNeedToRemove) {
                ref.removeValue()
                        .addOnFailureListener(e -> {
                            if (mainView != null) {
                                mainView.setErrorToast(e.getLocalizedMessage());
                            }
                        });
            } else {
                ref.child(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)
                        .setValue(ingred.getShopListStatus().getId())
                        .addOnFailureListener(e -> {
                            if (mainView != null) {
                                mainView.setErrorToast(e.getLocalizedMessage());
                            }
                        });
            }
        }
    }
}