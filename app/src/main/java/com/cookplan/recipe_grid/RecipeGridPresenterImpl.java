package com.cookplan.recipe_grid;


import android.support.annotation.NonNull;

import com.cookplan.models.Recipe;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.models.SharedData;
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

/**
 * Created by DariaEfimova on 21.03.17.
 */

public class RecipeGridPresenterImpl implements RecipeGridPresenter, FirebaseAuth.AuthStateListener {


    private RecipeGridView mainView;
    private DatabaseReference database;

    public RecipeGridPresenterImpl(RecipeGridView mainView) {
        this.mainView = mainView;
        this.database = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void getAsyncRecipeList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = null;
        if (auth != null && auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }
        if (uid != null) {
            Query items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
            items.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Recipe> recipes = new ArrayList<>();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        Recipe recipe = Recipe.getRecipeFromDBObject(itemSnapshot);
                        recipes.add(recipe);
                    }
                    checkSharedRecipies(recipes);
                }

                public void onCancelled(DatabaseError databaseError) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        if (mainView != null) {
                            mainView.setErrorToast(databaseError.getMessage());
                        }
                    }
                }
            });
        }
    }

    private void checkSharedRecipies(List<Recipe> recipes) {
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
                if (mainView != null) {
                    if (recipes.size() != 0) {
                        mainView.setRecipeList(resultRecipes);
                    } else {
                        mainView.setEmptyView();
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (mainView != null) {
                        mainView.setErrorToast(databaseError.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void removeRecipe(Recipe recipe) {
        if (recipe != null && recipe.getId() != null) {
            DatabaseReference recipeRef = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE);
            DatabaseReference ref = recipeRef.child(recipe.getId());
            ref.removeValue();
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            getAsyncRecipeList();
        }
    }
}
