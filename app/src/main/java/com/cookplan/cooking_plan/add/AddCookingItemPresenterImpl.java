package com.cookplan.cooking_plan.add;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.cookplan.providers.impl.RecipeProviderImpl;

import org.joda.time.DateTime;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 19.06.17.
 */

public class AddCookingItemPresenterImpl implements AddCookingItemPresenter {

    private AddCookingItemView mainView;
    private RecipeProvider recipeDataProvider;
    private IngredientProvider ingredientDataProvider;

    public AddCookingItemPresenterImpl(AddCookingItemView mainView) {
        this.mainView = mainView;
        recipeDataProvider = new RecipeProviderImpl();
        ingredientDataProvider = new IngredientProviderImpl();

    }

    @Override
    public void saveRecipeToCookingPlan(Recipe recipe, DateTime date) {
        recipe.addCookingDate(date.getMillis());
        recipeDataProvider.update(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Recipe>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Recipe recipe) {
                        if (mainView != null) {
                            mainView.setSuccessfullItemSaving();
                        }
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
    public void saveIngredientToCookingPlan(Ingredient ingredient, DateTime date) {
        ingredient.setCookingDate(date.getMillis());
        ingredientDataProvider.updateCookingTime(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if (mainView != null) {
                            mainView.setSuccessfullItemSaving();
                        }
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
