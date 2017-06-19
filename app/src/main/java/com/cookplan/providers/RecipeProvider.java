package com.cookplan.providers;

import com.cookplan.models.Recipe;
import com.cookplan.models.ShareUserInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by DariaEfimova on 24.04.17.
 */

public interface RecipeProvider {

    Observable<List<Recipe>> getSharedToMeRecipeList(List<ShareUserInfo> sharedInfoList);

    Observable<List<Recipe>> getUserRecipeList();

    Single<Recipe> createRecipe(Recipe recipe);

    Single<Recipe> update(Recipe recipe);

    Single<Recipe> getRecipeById(String recipeId);

    Completable removeRecipe(Recipe recipe);
}
