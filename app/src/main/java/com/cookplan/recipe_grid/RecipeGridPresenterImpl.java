package com.cookplan.recipe_grid;


import com.cookplan.models.Recipe;
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

/**
 * Created by DariaEfimova on 21.03.17.
 */

public class RecipeGridPresenterImpl implements RecipeGridPresenter {

    private RecipeGridView mainView;
    private DatabaseReference database;

    public RecipeGridPresenterImpl(RecipeGridView mainView) {
        this.mainView = mainView;
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getAsyncRecipeList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = null;
        if (auth != null && auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }
        if (uid != null) {
            Query items = database.child(DatabaseConstants.DATABASE_RECIPE_TABLE)
                    .orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD).equalTo(uid);
            items.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Recipe> recipes = new ArrayList<>();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        Recipe recipe = Recipe.getRecipeFromDBObject(itemSnapshot);
                        recipes.add(recipe);
                    }
                    if (mainView != null) {
                        mainView.setRecipeList(recipes);
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    if (mainView != null) {
                        mainView.setErrorToast(databaseError.getMessage());
                    }
                }
            });
        }
    }
}
