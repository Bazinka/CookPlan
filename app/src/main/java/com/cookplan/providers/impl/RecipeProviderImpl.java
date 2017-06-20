package com.cookplan.providers.impl;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.auth.FirebaseAuth;
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

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public class RecipeProviderImpl implements RecipeProvider {


    private DatabaseReference database;

    private BehaviorSubject<List<Recipe>> subjectRecipeList;

    public RecipeProviderImpl() {
        this.database = FirebaseDatabase.getInstance().getReference();
        subjectRecipeList = BehaviorSubject.create();
        getFirebaseAllRecipeList();
    }

    private void getFirebaseAllRecipeList() {
        DatabaseReference items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
        items.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = Recipe.getRecipeFromDBObject(itemSnapshot);
                    recipes.add(recipe);
                }

                if (subjectRecipeList != null) {
                    subjectRecipeList.onNext(recipes);
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (subjectRecipeList != null) {
                        subjectRecipeList.onError(new CookPlanError(databaseError));
                    }
                }
            }
        });
    }

    @Override
    public Observable<List<Recipe>> getSharedToMeRecipeList(List<ShareUserInfo> sharedInfoList) {
        return subjectRecipeList.map(allRecipes -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            List<Recipe> resultRecipes = new ArrayList<>();
            for (Recipe recipe : allRecipes) {
                if (recipe.getUserId().equals(uid)) {
                    resultRecipes.add(recipe);
                } else {
                    for (ShareUserInfo sharedInfo : sharedInfoList) {
                        if (sharedInfo.getOwnerUserId().contains(recipe.getUserId())) {
                            recipe.setUserName(sharedInfo.getOwnerUserName());
                            resultRecipes.add(recipe);
                        }
                    }
                }
            }
            return resultRecipes;
        });
    }

    @Override
    public Observable<List<Recipe>> getUserRecipeList() {
        return subjectRecipeList.map(allRecipes -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            List<Recipe> resultRecipes = new ArrayList<>();
            for (Recipe recipe : allRecipes) {
                if (recipe.getUserId().equals(uid)) {
                    resultRecipes.add(recipe);
                }
            }
            return resultRecipes;
        });
    }

    @Override
    public Observable<List<Recipe>> getRecipeListForCooking() {
        return subjectRecipeList.map(allRecipes -> {
            List<Recipe> resultRecipes = new ArrayList<>();
            for (Recipe recipe : allRecipes) {
                if (recipe.getCookingDate() != null && !recipe.getCookingDate().isEmpty()) {
                    resultRecipes.add(recipe);
                }
            }
            return resultRecipes;
        });
    }


    @Override
    public Single<Recipe> createRecipe(Recipe recipe) {
        return Single.create(emitter -> {
            DatabaseReference recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
            recipeRef.push().setValue(recipe.getRecipeDB(), (databaseError, reference) -> {
                if (databaseError != null) {
                    if (emitter != null) {
                        emitter.onError(new CookPlanError(databaseError));
                    }
                } else {
                    recipe.setId(reference.getKey());
                    if (emitter != null) {
                        emitter.onSuccess(recipe);
                    }
                }
            });
        });
    }

    @Override
    public Single<Recipe> update(Recipe recipe) {
        return Single.create(emitter -> {
            Map<String, Object> values = new HashMap<>();
            values.put(DatabaseConstants.DATABASE_NAME_FIELD, recipe.getName());
            values.put(DatabaseConstants.DATABASE_DESCRIPTION_FIELD, recipe.getDesc());
            values.put(DatabaseConstants.DATABASE_IMAGE_URL_LIST_FIELD, recipe.getImageUrls());
            values.put(DatabaseConstants.DATABASE_COOKING_DATE_LIST_FIELD, recipe.getCookingDate());
            DatabaseReference recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
            recipeRef.child(recipe.getId()).updateChildren(values, (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    if (emitter != null) {
                        emitter.onError(new CookPlanError(databaseError));
                    }
                } else {
                    if (emitter != null) {
                        emitter.onSuccess(recipe);
                    }
                }
            });
        });
    }

    @Override
    public Single<Recipe> getRecipeById(String recipeId) {
        return Single.create(emitter -> {
            Query items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE).child(recipeId);
            items.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Recipe recipe = Recipe.getRecipeFromDBObject(dataSnapshot);
                        if (emitter != null) {
                            emitter.onSuccess(recipe);
                        }
                    } else {
                        if (emitter != null) {
                            emitter.onSuccess(new Recipe());
                        }
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    if (emitter != null) {
                        emitter.onError(new CookPlanError(databaseError));
                    }
                }
            });
        });
    }

    @Override
    public Completable removeRecipe(Recipe recipe) {
        return Completable.create(emitter -> {
            if (recipe != null && recipe.getId() != null) {
                DatabaseReference recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
                DatabaseReference ref = recipeRef.child(recipe.getId());
                ref.removeValue()
                        .addOnFailureListener(exeption -> emitter.onError(new CookPlanError(exeption.getMessage())))
                        .addOnCompleteListener(task -> {
                            if (task.isComplete()) {
                                emitter.onComplete();
                            }
                        });
            } else {
                emitter.onError(new CookPlanError(RApplication.getAppContext().getString(R.string.recipe_doesnt_exist)));
            }
        });
    }
}
