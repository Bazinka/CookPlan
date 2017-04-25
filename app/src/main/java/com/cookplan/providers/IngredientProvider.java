package com.cookplan.providers;

import com.cookplan.models.Ingredient;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public interface IngredientProvider {

    Observable<List<Ingredient>> getAllIngredientList();

    Observable<List<Ingredient>> getIngredientListByRecipeId(String recipeId);

    Single<Ingredient> createIngredient(Ingredient ingredient);

    Completable removeIngredient(Ingredient ingredient);

    Completable updateShopStatus(Ingredient ingredient);
}
