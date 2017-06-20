package com.cookplan.cooking_plan.list;


import android.support.annotation.NonNull;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.cookplan.providers.impl.RecipeProviderImpl;
import com.google.firebase.auth.FirebaseAuth;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class CookingPlanPresenterImpl implements CookingPlanPresenter, FirebaseAuth.AuthStateListener {


    private CookingPlanView mainView;
    private RecipeProvider recipeDataProvider;
    private IngredientProvider ingredientDataProvider;
    private CompositeDisposable disposables;

    public CookingPlanPresenterImpl(CookingPlanView mainView) {
        this.mainView = mainView;
        FirebaseAuth.getInstance().addAuthStateListener(this);
        recipeDataProvider = new RecipeProviderImpl();
        ingredientDataProvider = new IngredientProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getCookingPlan() {
        disposables.add(
                recipeDataProvider.getRecipeListForCooking()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Recipe>>() {

                            @Override
                            public void onNext(List<Recipe> recipeList) {
                                disposables.add(
                                        ingredientDataProvider.getIngredientListForCooking()
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeWith(new DisposableObserver<List<Ingredient>>() {

                                                    @Override
                                                    public void onNext(List<Ingredient> ingredientList) {
                                                        if (mainView != null) {
                                                            mainView.setCookingList(getResultMap(recipeList, ingredientList));
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
    public void removeRecipeFromCookingPlan(LocalDate date, Recipe recipe) {
        List<Long> cookingDateList = new ArrayList<>(recipe.getCookingDate());
        for (Long millisec : cookingDateList) {
            LocalDate recipeDate = new LocalDate(millisec);
            if (recipeDate.equals(date)) {
                recipe.getCookingDate().remove(millisec);
            }
        }
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

    @Override
    public void removeIngredientFromCookingPlan(Ingredient ingredient) {
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
                    }
                });
    }

    private Map<LocalDate, List<Object>> getResultMap(List<Recipe> recipeList, List<Ingredient> ingredientList) {
        Map<LocalDate, List<Object>> calendarListMap = new HashMap<>();
        for (Recipe recipe : recipeList) {
            for (Long cookingDate : recipe.getCookingDate()) {
                LocalDate date = getDateMillisec(cookingDate);
                if (!calendarListMap.containsKey(date)) {
                    calendarListMap.put(date, new ArrayList<>());
                }
                calendarListMap.get(date).add(recipe);
            }
        }
        for (Ingredient ingredient : ingredientList) {
            LocalDate date = getDateMillisec(ingredient.getCookingDate());
            if (!calendarListMap.containsKey(date)) {
                calendarListMap.put(date, new ArrayList<>());
            }
            calendarListMap.get(date).add(ingredient);
        }
        return calendarListMap;
    }

    private LocalDate getDateMillisec(long cookingDate) {
        LocalDate date = new LocalDate(cookingDate);
        return date;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            getCookingPlan();
        }
    }

    @Override
    public void onStop() {
        disposables.clear();
    }
}
