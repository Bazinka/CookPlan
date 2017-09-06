package com.cookplan.recipe.list.main;


import android.support.annotation.NonNull;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.providers.FamilyModeProvider;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.providers.impl.FamilyModeProviderImpl;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.cookplan.providers.impl.RecipeProviderImpl;
import com.cookplan.recipe.list.BaseRecipeListView;
import com.google.firebase.auth.FirebaseAuth;

import org.joda.time.DateTime;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public class MainRecipeListPresenterImpl implements MainRecipeListPresenter, FirebaseAuth.AuthStateListener {

    private BaseRecipeListView mainView;
    private RecipeProvider recipeDataProvider;
    private IngredientProvider ingredientDataProvider;
    private FamilyModeProvider familyModeProvider;
    private CompositeDisposable disposables;

    public MainRecipeListPresenterImpl(BaseRecipeListView mainView) {
        this.mainView = mainView;
        FirebaseAuth.getInstance().addAuthStateListener(this);
        recipeDataProvider = new RecipeProviderImpl();
        ingredientDataProvider = new IngredientProviderImpl();
        familyModeProvider = new FamilyModeProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getRecipeList() {
        disposables.add(familyModeProvider.getInfoSharedToMe()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<List<ShareUserInfo>>() {
                                    @Override
                                    public void onNext(List<ShareUserInfo> shareUserInfos) {
                                        disposables.add(
                                                recipeDataProvider.getSharedToMeRecipeList(shareUserInfos)
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
                                                                    mainView.setError(e.getMessage());
                                                                }
                                                            }

                                                            @Override
                                                            public void onComplete() {
                                                            }
                                                        }));
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (mainView != null && e instanceof CookPlanError) {
                                            mainView.setError(e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                }));
    }

    @Override
    public void removeRecipe(Recipe recipe) {
        disposables.add(ingredientDataProvider.getIngredientListByRecipeId(recipe.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableObserver<List<Ingredient>>() {

                                    @Override
                                    public void onNext(List<Ingredient> ingredients) {
                                        for (Ingredient ingredient : ingredients) {
                                            removeIngredient(ingredient);
                                        }
                                        recipeDataProvider.removeRecipe(recipe)
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
                                                            mainView.setError(e.getMessage());
                                                        }
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (mainView != null && e instanceof CookPlanError) {
                                            mainView.setError(e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                }));
    }

    private void removeIngredient(Ingredient ingredient) {
        ingredientDataProvider.removeIngredient(ingredient)
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
                            mainView.setError(e.getMessage());
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

    @Override
    public void addRecipeToCookPlan(Recipe recipe, DateTime date) {
        recipe.addCookingDate(date.getMillis());
        updateRecipe(recipe);
    }

    @Override
    public void removeRecipeFromCookPlan(Recipe recipe) {
        recipe.clearCookingDateList();
        updateRecipe(recipe);
    }

    private void updateRecipe(Recipe recipe) {
        recipeDataProvider.update(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Recipe>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Recipe recipe) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }
}