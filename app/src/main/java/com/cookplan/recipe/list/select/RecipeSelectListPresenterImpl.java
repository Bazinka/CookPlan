package com.cookplan.recipe.list.select;


import com.cookplan.models.CookPlanError;
import com.cookplan.models.Recipe;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.providers.impl.RecipeProviderImpl;
import com.cookplan.recipe.list.RecipeListPresenter;
import com.cookplan.recipe.list.RecipeListView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public class RecipeSelectListPresenterImpl implements RecipeListPresenter {


    private RecipeListView mainView;
    private RecipeProvider recipeDataProvider;
    private CompositeDisposable disposables;

    public RecipeSelectListPresenterImpl(RecipeListView mainView) {
        this.mainView = mainView;
        recipeDataProvider = new RecipeProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getRecipeList() {
        disposables.add(
                recipeDataProvider.getUserRecipeList()
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
    }

    @Override
    public void onStop() {
        disposables.clear();
    }
}
