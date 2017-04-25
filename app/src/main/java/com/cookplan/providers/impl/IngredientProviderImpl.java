package com.cookplan.providers.impl;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public class IngredientProviderImpl implements IngredientProvider {


    private DatabaseReference database;

    private BehaviorSubject<List<Ingredient>> subjectAllIngredientList;

    public IngredientProviderImpl() {
        this.database = FirebaseDatabase.getInstance().getReference();
        subjectAllIngredientList = BehaviorSubject.create();
        getFirebaseAllIngredientList();
    }

    private void getFirebaseAllIngredientList() {
        Query items = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
        items.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Ingredient> allIngredients = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Ingredient ingredient = Ingredient.getIngredientFromDBObject(itemSnapshot);
                    allIngredients.add(ingredient);
                }
                if (subjectAllIngredientList != null) {
                    subjectAllIngredientList.onNext(allIngredients);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (subjectAllIngredientList != null) {
                        subjectAllIngredientList.onError(new CookPlanError(databaseError));
                    }
                }
            }
        });
    }

    @Override
    public Observable<List<Ingredient>> getAllIngredientList() {
        return subjectAllIngredientList;
    }

    @Override
    public Observable<List<Ingredient>> getIngredientListByRecipeId(String recipeId) {
        return subjectAllIngredientList.map(allIngredients -> {
            List<Ingredient> recipeIngredients = new ArrayList<>();
            for (Ingredient ingredient : allIngredients) {
                String ingredRecipeId = ingredient.getRecipeId();
                if (ingredRecipeId != null && ingredRecipeId.equals(recipeId)) {
                    recipeIngredients.add(ingredient);
                }
            }
            return recipeIngredients;
        });
    }

    @Override
    public Single<Ingredient> createIngredient(Ingredient ingredient) {
        return Single.create(emitter -> {
            DatabaseReference ingredRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
            ingredRef.push().setValue(ingredient, (databaseError, reference) -> {
                if (emitter != null) {
                    if (databaseError != null) {
                        emitter.onError(new CookPlanError(databaseError));
                    } else {
                        emitter.onSuccess(ingredient);
                    }
                }
            });
        });
    }
    
    @Override
    public Completable removeIngredient(Ingredient ingredient) {
        return Completable.create(emitter -> {
            if (ingredient != null && ingredient.getId() != null) {
                DatabaseReference ingredRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
                DatabaseReference ref = ingredRef.child(ingredient.getId());
                ref.removeValue()
                        .addOnFailureListener(exeption -> emitter.onError(new CookPlanError(exeption.getMessage())))
                        .addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                emitter.onComplete();
                            }
                        });
            } else {
                emitter.onError(new CookPlanError(RApplication.getAppContext().getString(R.string.ingred_remove_error)));
            }
        });
    }

    @Override
    public Completable updateShopStatus(Ingredient ingredient) {
        return Completable.create(emitter -> {
            DatabaseReference ingredientRef = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
            ingredientRef
                    .child(ingredient.getId())
                    .child(DatabaseConstants.DATABASE_SHOP_LIST_STATUS_FIELD)
                    .setValue(ingredient.getShopListStatus())
                    .addOnSuccessListener(aVoid -> emitter.onComplete())
                    .addOnFailureListener(exception -> emitter.onError(new CookPlanError(exception.getMessage())));
        });
    }

    //    @Override
    //    public Single<Recipe> createRecipe(Recipe recipe) {
    //        return Single.create(emitter -> {
    //            DatabaseReference recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
    //            recipeRef.push().setValue(recipe.getRecipeDB(), (databaseError, reference) -> {
    //                if (databaseError != null) {
    //                    if (emitter != null) {
    //                        emitter.onError(new CookPlanError(databaseError));
    //                    }
    //                } else {
    //                    recipe.setId(reference.getKey());
    //                    if (emitter != null) {
    //                        emitter.onSuccess(recipe);
    //                    }
    //                }
    //            });
    //        });
    //    }
    //
    //    @Override
    //    public Single<Recipe> update(Recipe recipe) {
    //        return Single.create(emitter -> {
    //            Map<String, Object> values = new HashMap<>();
    //            values.put(DatabaseConstants.DATABASE_NAME_FIELD, recipe.getName());
    //            values.put(DatabaseConstants.DATABASE_DESCRIPTION_FIELD, recipe.getDesc());
    //            DatabaseReference recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
    //            recipeRef.child(recipe.getId()).updateChildren(values, (databaseError, databaseReference) -> {
    //                if (databaseError != null) {
    //                    if (emitter != null) {
    //                        emitter.onError(new CookPlanError(databaseError));
    //                    }
    //                } else {
    //                    if (emitter != null) {
    //                        emitter.onSuccess(recipe);
    //                    }
    //                }
    //            });
    //        });
    //    }
    //
    //    @Override
    //    public Single<Recipe> getRecipeById(String recipeId) {
    //        return Single.create(emitter -> {
    //            Query items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE).child(recipeId);
    //            items.addListenerForSingleValueEvent(new ValueEventListener() {
    //                public void onDataChange(DataSnapshot dataSnapshot) {
    //                    if (dataSnapshot.getValue() != null) {
    //                        Recipe recipe = Recipe.getRecipeFromDBObject(dataSnapshot);
    //                        if (emitter != null) {
    //                            emitter.onSuccess(recipe);
    //                        }
    //                    } else {
    //                        if (emitter != null) {
    //                            emitter.onSuccess(null);
    //                        }
    //                    }
    //                }
    //
    //                public void onCancelled(DatabaseError databaseError) {
    //                    if (emitter != null) {
    //                        emitter.onError(new CookPlanError(databaseError));
    //                    }
    //                }
    //            });
    //        });
    //    }
    //
}
