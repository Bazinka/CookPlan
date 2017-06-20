package com.cookplan.cooking_plan.list;


import android.support.annotation.NonNull;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.providers.FamilyModeProvider;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.providers.impl.FamilyModeProviderImpl;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.cookplan.providers.impl.RecipeProviderImpl;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public class CookingPlanPresenterImpl implements CookingPlanPresenter, FirebaseAuth.AuthStateListener {


    private CookingPlanView mainView;
    private RecipeProvider recipeDataProvider;
    private IngredientProvider ingredientDataProvider;
    private FamilyModeProvider familyModeProvider;
    private CompositeDisposable disposables;

    public CookingPlanPresenterImpl(CookingPlanView mainView) {
        this.mainView = mainView;
        FirebaseAuth.getInstance().addAuthStateListener(this);
        recipeDataProvider = new RecipeProviderImpl();
        ingredientDataProvider = new IngredientProviderImpl();
        familyModeProvider = new FamilyModeProviderImpl();
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
                                                            if (ingredientList.isEmpty()) {
                                                                mainView.setEmptyView();
                                                            } else {
                                                                mainView.setCookingList(getResultMap(recipeList, ingredientList));
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

    private Map<Long, List<Object>> getResultMap(List<Recipe> recipeList, List<Ingredient> ingredientList) {
        Map<Long, List<Object>> calendarListMap = new HashMap<>();
        for (Recipe recipe : recipeList) {
            long date = getDateMillisec(recipe.getCookingDate());
            if (!calendarListMap.containsKey(date)) {
                calendarListMap.put(date, new ArrayList<>());
            }
            calendarListMap.get(date).add(recipe);
        }
        for (Ingredient ingredient : ingredientList) {
            long date = getDateMillisec(ingredient.getCookingDate());
            if (!calendarListMap.containsKey(date)) {
                calendarListMap.put(date, new ArrayList<>());
            }
            calendarListMap.get(date).add(ingredient);
        }
        return calendarListMap;
    }

    private long getDateMillisec(long cookingDate) {
        Calendar originCalendar = Calendar.getInstance();
        originCalendar.setTimeInMillis(cookingDate);

        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, originCalendar.get(Calendar.DAY_OF_MONTH));
        date.set(Calendar.MONTH, originCalendar.get(Calendar.MONTH));
        date.set(Calendar.YEAR, originCalendar.get(Calendar.YEAR));
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        return date.getTimeInMillis();
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
