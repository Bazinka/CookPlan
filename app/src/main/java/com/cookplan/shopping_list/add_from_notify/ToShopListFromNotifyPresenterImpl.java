package com.cookplan.shopping_list.add_from_notify;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.cookplan.providers.impl.RecipeProviderImpl;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 11.07.17.
 */

public class ToShopListFromNotifyPresenterImpl implements ToShopListFromNotifyPresenter {

    private ToShopListFromNotifyView mainView;
    private RecipeProvider recipeDataProvider;
    private IngredientProvider ingredientDataProvider;
    private CompositeDisposable disposables;

    public ToShopListFromNotifyPresenterImpl(ToShopListFromNotifyView mainView) {
        this.mainView = mainView;
        recipeDataProvider = new RecipeProviderImpl();
        ingredientDataProvider = new IngredientProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getItems(List<String> recipeIds, List<String> ingredientsIds) {
        getIngredients(ingredientsIds);
        for (String recipeId : recipeIds) {
            getRecipeAndIngredients(recipeId);
        }
    }

    @Override
    public void onStop() {
        if (disposables != null) {
            disposables.clear();
        }
    }

    private void getRecipeAndIngredients(String recipeId) {
        recipeDataProvider.getRecipeById(recipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Recipe>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Recipe recipe) {
                        disposables.add(
                                ingredientDataProvider.getIngredientListByRecipeId(recipe.getId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(new DisposableObserver<List<Ingredient>>() {

                                            @Override
                                            public void onNext(List<Ingredient> ingredientList) {
                                                if (mainView != null) {
                                                    mainView.setResults(recipe, ingredientList);
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
                });
    }

    private void getIngredients(List<String> ingredientsIds) {
        disposables.add(
                ingredientDataProvider.getIngredientListByIdList(ingredientsIds)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Ingredient>>() {

                            @Override
                            public void onNext(List<Ingredient> ingredientList) {
                                if (mainView != null) {
                                    mainView.setResults(null, ingredientList);
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
    public void setIngredientsNeedToBuy(List<Ingredient> ingredientList) {
        for (Ingredient ingredient : ingredientList) {
            ingredient.setShopListStatus(ShopListStatus.NEED_TO_BUY);
            ingredientDataProvider.updateShopStatus(ingredient)
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
                            if (mainView != null) {
                                mainView.setError(e.getMessage());
                            }
                        }
                    });
        }
    }
}
