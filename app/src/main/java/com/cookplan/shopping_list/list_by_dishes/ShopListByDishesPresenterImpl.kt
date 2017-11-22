package com.cookplan.shopping_list.list_by_dishes


import android.content.Context
import com.cookplan.R
import com.cookplan.models.CookPlanError
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.models.ShopListStatus
import com.cookplan.providers.RecipeProvider
import com.cookplan.providers.impl.RecipeProviderImpl
import com.cookplan.shopping_list.ShoppingListBasePresenterImpl
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by DariaEfimova on 24.03.17.
 */

class ShopListByDishesPresenterImpl(private val mainView: ShopListByDishesView?, private val context: Context) : ShoppingListBasePresenterImpl(), ShopListByDishPresenter {
    private var recipeIdToIngredientMap: MutableMap<String, List<Ingredient>> = mutableMapOf()

    private val recipeDataProvider: RecipeProvider

    init {
        this.recipeDataProvider = RecipeProviderImpl()
    }

    override fun setError(message: String?) {
        mainView?.setErrorToast(message!!)
    }

    override fun sortIngredientList(userIngredients: List<Ingredient>) {

        //fill the map
        recipeIdToIngredientMap.clear()
        for (ingredient in userIngredients) {
            if (ingredient.shopListStatus != ShopListStatus.NONE) {
                val key = ingredient.recipeId ?: WITHOUT_RECIPE_KEY
                val ingredients = (recipeIdToIngredientMap[key] ?: arrayListOf()) as ArrayList<Ingredient>
                ingredients.add(ingredient)
                recipeIdToIngredientMap.put(key, ingredients)
            }
        }
        if (recipeIdToIngredientMap.isEmpty()) {
            mainView?.setEmptyView()
        } else {
            val recipeToIngredientsMap = sortedMapOf<Recipe, List<Ingredient>>()
            for ((key) in recipeIdToIngredientMap) {
                recipeDataProvider.getRecipeById(key)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : SingleObserver<Recipe> {
                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onSuccess(recipe: Recipe) {
                                var keyRecipe = recipe
                                if (recipe.id == null) {
                                    keyRecipe = Recipe(context.getString(R.string.without_recipe_title),
                                            context.getString(R.string.recipe_desc_is_not_needed_title))
                                }
                                recipeToIngredientsMap.put(keyRecipe,
                                        recipeIdToIngredientMap[recipe.id ?: WITHOUT_RECIPE_KEY] ?: listOf())

                                if (recipeToIngredientsMap.keys.size == recipeIdToIngredientMap.keys.size) {
                                    makeValidDataForTheView(recipeToIngredientsMap)
                                }
                            }

                            override fun onError(e: Throwable) {
                                if (mainView != null && e is CookPlanError) {
                                    setError(e.message)
                                }
                            }
                        })
            }
        }
    }

    private fun makeValidDataForTheView(recipeToIngredientsMap: SortedMap<Recipe, List<Ingredient>>) {
        val recipeIdsToIngredientMap: SortedMap<String, List<Ingredient>> = sortedMapOf()
        val recipeList: ArrayList<Recipe> = arrayListOf()

        for (key in recipeToIngredientsMap.keys) {
            var recipeKey = key
            if (recipeKey.id == null) {
                recipeKey = Recipe(context.getString(R.string.without_recipe_title),
                        context.getString(R.string.recipe_desc_is_not_needed_title))
            }
            recipeList.add(recipeKey)
            recipeIdsToIngredientMap.put(key.id ?: WITHOUT_RECIPE_KEY, recipeToIngredientsMap[key])
        }

        mainView?.setIngredientListToRecipe(recipeList, recipeIdsToIngredientMap)
    }

    override fun changeIngredientStatus(ingredient: Ingredient, newStatus: ShopListStatus) {
        if (newStatus !== ShopListStatus.NONE) {
            ingredient.shopListStatus = newStatus
            ingredientDataProvider.updateShopStatus(ingredient)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onComplete() {

                        }

                        override fun onError(e: Throwable) {
                            mainView?.setErrorToast(e.message ?: "")
                        }
                    })
        }
    }

    override fun recipeIngredBought(recipe: Recipe, ingredientList: List<Ingredient>) {
        val isNeedToRemove = recipe.id == null
        if (isNeedToRemove) {
            ingredientDataProvider.removeIngredientList(ingredientList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onComplete() {

                        }

                        override fun onError(e: Throwable) {
                            if (mainView != null && e is CookPlanError) {
                                mainView.setErrorToast(e.message ?: "")
                            }
                        }
                    })
        } else {
            for (ingred in ingredientList) {
                ingred.shopListStatus = ShopListStatus.NONE
            }
            ingredientDataProvider.updateShopStatusList(ingredientList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onComplete() {

                        }

                        override fun onError(e: Throwable) {
                            mainView?.setErrorToast(e.message ?: "")
                        }
                    })
        }
    }

    companion object {

        private val WITHOUT_RECIPE_KEY = "without_recipe"
    }
}