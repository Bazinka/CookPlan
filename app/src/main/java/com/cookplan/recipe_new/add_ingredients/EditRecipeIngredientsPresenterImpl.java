package com.cookplan.recipe_new.add_ingredients;


import com.cookplan.models.Ingredient;
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

public class EditRecipeIngredientsPresenterImpl implements EditRecipeIngredientsPresenter {
    private EditRecipeIngredientsView mainView;
    private DatabaseReference mDatabase;

    public EditRecipeIngredientsPresenterImpl(EditRecipeIngredientsView mainView) {
        this.mainView = mainView;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getAsyncIngredientList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = null;
        if (auth != null && auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }
        if (uid != null) {
            Query items = mDatabase.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE).
                    orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD).equalTo(uid);
            items.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Ingredient> ingredients = new ArrayList<>();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        Ingredient ingredient = Ingredient.getIngredientFromDBObject(itemSnapshot);
                        ingredient.setId(itemSnapshot.getKey());
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
    }
}