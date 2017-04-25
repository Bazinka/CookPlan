package com.cookplan.recipe_new.add_ingredients;


import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public class EditRecipeIngredientsPresenterImpl implements EditRecipeIngredientsPresenter {

    private EditRecipeIngredientsView mainView;
    private IngredientProvider dataProvider;
    private Recipe recipe;

    EditRecipeIngredientsPresenterImpl(EditRecipeIngredientsView mainView, Recipe recipe) {
        this.mainView = mainView;
        this.recipe = recipe;
        this.dataProvider = new IngredientProviderImpl();
    }

    @Override
    public void getAsyncIngredientList() {
        dataProvider.getIngredientListByRecipeId(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Ingredient>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Ingredient> ingredients) {
                        if (mainView != null) {
                            mainView.setIngredientList(ingredients);
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
                });
    }

    @Override
    public void removeIngredient(Ingredient ingredient) {
        dataProvider.removeIngredient(ingredient)
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
}