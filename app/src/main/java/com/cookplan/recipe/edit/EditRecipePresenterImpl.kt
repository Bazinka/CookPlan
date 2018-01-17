package com.cookplan.recipe.edit

import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.providers.IngredientProvider
import com.cookplan.providers.ProviderFactory
import com.cookplan.providers.RecipeProvider
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by DariaEfimova on 16.03.17.
 */

open class EditRecipePresenterImpl(private val mainView: EditRecipeView?) : EditRecipePresenter {

    private val recipeDataProvider: RecipeProvider = ProviderFactory.recipeProvider
    private val ingredientDataProvider: IngredientProvider = ProviderFactory.ingredientProvider

    override fun saveRecipe(recipe: Recipe?, newName: String, newDesc: String?) {
        mainView?.showProgressBar()

        var newRecipe = recipe
        if (newRecipe == null) {
            newRecipe = Recipe(id = String(), name = newName, desc = newDesc ?: String(), userId = FirebaseAuth.getInstance().currentUser?.uid,
                    userName = FirebaseAuth.getInstance().currentUser?.displayName)
        } else {
            newRecipe.name = if (!newName.isEmpty()) newName else newRecipe.name
            newRecipe.desc = newDesc ?: newRecipe.desc
        }
        if (newRecipe.id.isEmpty()) {
            recipeDataProvider.createRecipe(newRecipe)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<Recipe> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onSuccess(recipe: Recipe) {
                            mainView?.hideProgressBar()
                            mainView?.recipeSavedSuccessfully(recipe)
                        }

                        override fun onError(e: Throwable) {
                            mainView?.hideProgressBar()
                            mainView?.setErrorToast(e.message ?: String())
                        }
                    })
        } else {
            recipeDataProvider.update(newRecipe)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<Recipe> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onSuccess(recipe: Recipe) {
                            mainView?.hideProgressBar()
                            mainView?.recipeSavedSuccessfully(recipe)
                        }

                        override fun onError(e: Throwable) {
                            mainView?.hideProgressBar()
                            mainView?.setErrorToast(e.message ?: String())
                        }
                    })
        }
    }

    override fun removeRecipe(recipe: Recipe, ingredients: List<Ingredient>) {
        for (ingredient in ingredients) {
            removeIngredient(ingredient)
        }

        recipeDataProvider.removeRecipe(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {
                        mainView?.recipeRemovedSuccessfully()
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setErrorToast(e.message ?: String())
                    }
                })
    }


    private fun removeIngredient(ingredient: Ingredient) {
        ingredientDataProvider.removeIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {

                    }

                    override fun onError(e: Throwable) {
                        mainView?.setErrorToast(e.message ?: String())
                    }
                })
    }
}