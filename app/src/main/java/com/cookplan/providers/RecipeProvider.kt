package com.cookplan.providers

import com.cookplan.models.Recipe
import com.cookplan.models.ShareUserInfo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by DariaEfimova on 24.04.17.
 */

interface RecipeProvider {

    fun getSharedToMeRecipeList(sharedInfoList: List<ShareUserInfo>): Observable<List<Recipe>>

    fun createRecipe(recipe: Recipe): Single<Recipe>

    fun update(recipe: Recipe): Single<Recipe>

    fun getRecipeById(recipeId: String?): Single<Recipe>

    fun removeRecipe(recipe: Recipe): Completable
}
