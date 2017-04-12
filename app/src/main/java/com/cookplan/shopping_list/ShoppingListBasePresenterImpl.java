package com.cookplan.shopping_list;


import android.support.annotation.NonNull;

import com.cookplan.models.Ingredient;
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
            Query items = database.child(DatabaseConstants.DATABASE_INRGEDIENT_TABLE);
            items.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Ingredient> allIngredients = new ArrayList<>();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        Ingredient ingredient = Ingredient.getIngredientFromDBObject(itemSnapshot);
                        allIngredients.add(ingredient);
                    }
                    checkSharedIngredients(allIngredients);
                }

                public void onCancelled(DatabaseError databaseError) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        setError(databaseError.getMessage());
                    }
                }
            });
        }
    }

    private void checkSharedIngredients(List<Ingredient> ingredients) {
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
                        if (userInfo.getSharedData() == SharedData.INGREDIENTS) {
                            userIdList.add(userInfo.getOwnerUserId());
                            userIdToInfo.put(userInfo.getOwnerUserId(), userInfo);
                        }
                    }
                }
                List<Ingredient> resultIngredients = new ArrayList<>();
                for (String userId : userIdList) {
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.getUserId().equals(userId)) {
                            if (!userId.equals(uid)) {
                                ingredient.setUserName(userIdToInfo.get(userId).getOwnerUserName());
                            }
                            resultIngredients.add(ingredient);
                        }
                    }
                }
                sortIngredientList(resultIngredients);
            }

            public void onCancelled(DatabaseError databaseError) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    setError(databaseError.getMessage());
                }
            }
        });
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