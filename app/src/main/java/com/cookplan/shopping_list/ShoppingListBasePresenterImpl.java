package com.cookplan.shopping_list;


import android.support.annotation.NonNull;

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
 * Created by DariaEfimova on 24.03.17.
 */

public abstract class ShoppingListBasePresenterImpl implements ShoppingListBasePresenter, FirebaseAuth.AuthStateListener {

    private DatabaseReference database;

    public ShoppingListBasePresenterImpl() {
        this.database = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void getShoppingList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = null;
        if (auth != null && auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }
        if (uid != null) {
            Query items = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE)
                    .orderByChild(DatabaseConstants.DATABASE_USER_ID_FIELD).equalTo(uid);
            items.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Ingredient> userIngredients = new ArrayList<>();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        Ingredient ingredient = Ingredient.getIngredientFromDBObject(itemSnapshot);
                        userIngredients.add(ingredient);
                    }
                    sortIngredientList(userIngredients);
                }

                public void onCancelled(DatabaseError databaseError) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        setError(databaseError.getMessage());
                    }
                }
            });
        }
    }

    protected abstract void setError(String message);

    public abstract void sortIngredientList(List<Ingredient> userIngredients);

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            getShoppingList();
        }
    }
}