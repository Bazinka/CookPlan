package com.cookplan.recipe.view_item

import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.models.ShopListStatus
import com.cookplan.providers.IngredientProvider
import com.cookplan.providers.ProviderFactory
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by DariaEfimova on 23.03.17.
 */

class RecipeViewPresenterImpl(private val mainView: RecipeView?, private var recipe: Recipe) : RecipeViewPresenter {

    private val dataProvider: IngredientProvider
    private val disposables: CompositeDisposable

    init {
        this.dataProvider = ProviderFactory.Companion.ingredientProvider
        disposables = CompositeDisposable()
    }

    override fun getIngredientList() {
        if (recipe.id != null) {
            disposables.add(dataProvider.getIngredientListByRecipeId(recipe.id ?: String())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<List<Ingredient>>() {
                        override fun onNext(ingredients: List<Ingredient>) {
                            mainView?.setIngredientList(ingredients)
                        }

                        override fun onError(e: Throwable) {
                            mainView?.setErrorToast(e.message ?: String())
                        }

                        override fun onComplete() {

                        }
                    }))
        }
    }

    override fun addIngredientToShoppingList(ingredient: Ingredient) {
        dataProvider.updateShopStatus(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {
                        //                        if (mainView != null) {
                        //                            mainView.setIngredientSuccessfulUpdate(ingredient);
                        //                        }
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setErrorToast(e.message ?: String())
                    }
                })
        //        if (mainView != null) {
        //            mainView.setIngredientSuccessfulUpdate(ingredient);
        //        }
    }


    override fun changeIngredListShopStatus(ingredients: List<Ingredient>, status: ShopListStatus) {
        for (ingredient in ingredients) {
            ingredient.shopListStatus = status
        }
        dataProvider.updateShopStatusList(ingredients)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {
                        mainView?.ingredListChangedShoplistStatus(status === ShopListStatus.NONE)
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setErrorToast(e.message ?: String())
                    }
                })
    }

    override fun onStop() {
        disposables.clear()
    }

    override fun getRecipe(): Recipe {
        return recipe
    }
}
