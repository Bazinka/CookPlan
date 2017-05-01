package com.cookplan.shopping_list;


import android.support.annotation.NonNull;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

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
            disposables.add(ingredientDataProvider.getAllIngredientsForUser()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableObserver<List<Ingredient>>() {

                                        @Override
                                        public void onNext(List<Ingredient> ingredients) {
                                            sortIngredientList(ingredients);
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

    protected abstract void setError(String message);

    public abstract void sortIngredientList(List<Ingredient> userIngredients);

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            getShoppingList();
        }
    }
}