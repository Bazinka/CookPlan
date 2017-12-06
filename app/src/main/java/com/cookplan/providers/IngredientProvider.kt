package com.cookplan.providers

import com.cookplan.models.Ingredient
import com.cookplan.models.ShareUserInfo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by DariaEfimova on 24.04.17.
 */

interface IngredientProvider {

    fun getAllIngredientsSharedToUser(shareUserInfos: List<ShareUserInfo>): Observable<List<Ingredient>>

    fun getIngredientListByRecipeId(recipeId: String): Observable<List<Ingredient>>

    fun createIngredient(ingredient: Ingredient): Single<Ingredient>

    fun removeIngredient(ingredient: Ingredient): Completable

    fun removeIngredientList(ingredients: List<Ingredient>): Completable

    fun updateShopStatus(ingredient: Ingredient): Completable

    fun updateShopStatusList(ingredients: List<Ingredient>): Completable
}
