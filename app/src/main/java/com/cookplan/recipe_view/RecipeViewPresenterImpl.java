package com.cookplan.recipe_view;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 23.03.17.
 */

public class RecipeViewPresenterImpl implements RecipeViewPresenter {

    private RecipeView mainView;
    private DatabaseReference database;
    private Recipe recipe;

    public RecipeViewPresenterImpl(RecipeView mainView, Recipe recipe) {
        this.mainView = mainView;
        this.recipe = recipe;
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getIngredientList() {
        Query items = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE)
                .orderByChild(DatabaseConstants.DATABASE_RECIPE_ID_FIELD).equalTo(recipe.getId());
        items.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ingredient> ingredients = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Ingredient ingredient = Ingredient.getIngredientFromDBObject(itemSnapshot);
                    ingredients.add(ingredient);
                }
                if (mainView != null) {
                    mainView.setIngredientList(ingredients);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                if (mainView != null) {
                    mainView.setErrorToast(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void saveSelectIngredientList(Ingredient ingredient) {
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
