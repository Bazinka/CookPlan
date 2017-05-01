package com.cookplan.shopping_list;


import android.support.annotation.NonNull;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 24.03.17.
 */

public abstract class ShoppingListBasePresenterImpl implements ShoppingListBasePresenter, FirebaseAuth.AuthStateListener {

    private DatabaseReference database;


    protected IngredientProvider ingredientDataProvider;
    private CompositeDisposable disposables;

    public ShoppingListBasePresenterImpl() {
        this.database = FirebaseDatabase.getInstance().getReference();
        this.ingredientDataProvider = new IngredientProviderImpl();
        FirebaseAuth.getInstance().addAuthStateListener(this);
        disposables = new CompositeDisposable();
    }

    @Override
    public void onStop() {
        disposables.clear();
    }

    @Override
    public void getShoppingList() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = null;
        if (auth != null && auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }
        if (uid != null) {
            disposables.add(ingredientDataProvider.getAllIngredientList()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableObserver<List<Ingredient>>() {

                                        @Override
                                        public void onNext(List<Ingredient> ingredients) {
                                            checkSharedIngredients(ingredients);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if (FirebaseAuth.getInstance().getCurrentUser() != null
                                                    && e instanceof CookPlanError) {
                                                setError(e.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    }));
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
                        userIdList.add(userInfo.getOwnerUserId());
                        userIdToInfo.put(userInfo.getOwnerUserId(), userInfo);
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