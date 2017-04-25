package com.cookplan.recipe_view;

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
 * Created by DariaEfimova on 23.03.17.
 */

public class RecipeViewPresenterImpl implements RecipeViewPresenter {

    private RecipeView mainView;
    private IngredientProvider dataProvider;
    private Recipe recipe;

    public RecipeViewPresenterImpl(RecipeView mainView, Recipe recipe) {
        this.mainView = mainView;
        this.recipe = recipe;
        this.dataProvider = new IngredientProviderImpl();
    }

    @Override
    public void getIngredientList() {
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
    public void addIngredientToShoppingList(Ingredient ingredient) {
        dataProvider.updateShopStatus(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if (mainView != null) {
                            mainView.setIngredientSuccessfulUpdate(ingredient);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null) {
                            mainView.setErrorToast(e.getMessage());
                        }
                    }
                });
    }


    @Override
    public void addAllIngredientToShoppingList(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            addIngredientToShoppingList(ingredient);
        }
    }
}
