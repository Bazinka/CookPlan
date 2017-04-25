package com.cookplan.providers;

import com.cookplan.models.Recipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public interface RecipeProvider {

    Observable<List<Recipe>> getSharedRecipeList();

    Single<Recipe> createRecipe(Recipe recipe);

    Single<Recipe> update(Recipe recipe);

    Single<Recipe> getRecipeById(String recipeId);

    Completable removeRecipe(Recipe recipe);
}
