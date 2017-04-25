package com.cookplan.providers.impl;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.models.SharedData;
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
        getFirebaseSharedRecipeList();
    }

    private void getFirebaseSharedRecipeList() {
        DatabaseReference items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
        items.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = Recipe.getRecipeFromDBObject(itemSnapshot);
                    recipes.add(recipe);
                }
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Query sharedItems = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE)
                        .orderByChild(DatabaseConstants.DATABASE_CLIENT_USER_EMAIL_FIELD)
                        .equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                sharedItems.addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> userIdList = new ArrayList<>();
                        Map<String, ShareUserInfo> userIdToInfo = new HashMap<>();
                        userIdList.add(uid);
                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                ShareUserInfo userInfo = itemSnapshot.getValue(ShareUserInfo.class);
                                if (userInfo.getSharedData() == SharedData.RECIPE) {
                                    userIdToInfo.put(userInfo.getOwnerUserId(), userInfo);
                                    userIdList.add(userInfo.getOwnerUserId());
                                }
                            }
                        }
                        List<Recipe> resultRecipes = new ArrayList<>();
                        for (String userId : userIdList) {
                            for (Recipe recipe : recipes) {
                                if (recipe.getUserId().equals(userId)) {
                                    if (!userId.equals(uid)) {
                                        recipe.setUserName(userIdToInfo.get(userId).getOwnerUserName());
                                    }
                                    resultRecipes.add(recipe);
                                }
                            }
                        }
                        if (subjectRecipeList != null) {
                            subjectRecipeList.onNext(resultRecipes);
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
    public Observable<List<Recipe>> getAllRecipeList() {
        return subjectRecipeList;
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
