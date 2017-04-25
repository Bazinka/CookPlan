package com.cookplan.recipe_grid;


import android.support.annotation.NonNull;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Recipe;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.providers.impl.RecipeProviderImpl;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public class RecipeGridPresenterImpl implements RecipeGridPresenter, FirebaseAuth.AuthStateListener {


    private RecipeGridView mainView;
    private RecipeProvider dataProvider;
    private CompositeDisposable disposables;

    public RecipeGridPresenterImpl(RecipeGridView mainView) {
        this.mainView = mainView;
        FirebaseAuth.getInstance().addAuthStateListener(this);
        dataProvider = new RecipeProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getRecipeList() {
        disposables.add(dataProvider.getAllRecipeList()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<List<Recipe>>() {

                                    @Override
                                    public void onNext(List<Recipe> recipeList) {
                                        if (mainView != null) {
                                            if (recipeList.isEmpty()) {
                                                mainView.setEmptyView();
                                            } else {
                                                mainView.setRecipeList(recipeList);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (mainView != null && e instanceof CookPlanError) {
                                            mainView.setErrorToast(e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                }));
    }

    @Override
    public void removeRecipe(Recipe recipe) {
        dataProvider.removeRecipe(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null && e instanceof CookPlanError) {
                            mainView.setErrorToast(e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            getRecipeList();
        }
    }

    @Override
    public void onStop() {
        disposables.clear();
    }
}
