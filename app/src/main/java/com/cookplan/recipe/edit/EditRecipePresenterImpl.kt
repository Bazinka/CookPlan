package com.cookplan.recipe.edit

import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.providers.IngredientProvider
import com.cookplan.providers.ProviderFactory
import com.cookplan.providers.RecipeProvider
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

    override fun saveRecipe(recipe: Recipe) {
        mainView?.showProgressBar()

//        var newRecipe = recipe
//        if (newRecipe == null) {
//            newRecipe = Recipe(id = String(), name = newName, desc = newDesc ?: String(), userId = FirebaseAuth.getInstance().currentUser?.uid,
//                    userName = FirebaseAuth.getInstance().currentUser?.displayName)
//        } else {
//            newRecipe.name = if (!newName.isEmpty()) newName else newRecipe.name
//            newRecipe.desc = newDesc ?: newRecipe.desc
//        }
        if (recipe.id.isNullOrEmpty()) {
            recipeDataProvider.createRecipe(recipe)
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
            recipeDataProvider.update(recipe)
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

        if (!recipe.id.isNullOrEmpty()) {
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
        } else {
            mainView?.setErrorToast(R.string.recipe_doesnt_exist)
        }
    }


    private fun removeIngredient(ingredient: Ingredient) {
        if (ingredient.id != null) {
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
        } else {
            mainView?.setErrorToast(R.string.ingred_remove_error)
        }
    }
}