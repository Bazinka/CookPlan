package com.cookplan.recipe.edit

import com.cookplan.models.Recipe
import com.cookplan.providers.RecipeProvider
import com.cookplan.providers.impl.RecipeProviderImpl
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by DariaEfimova on 16.03.17.
 */

open class EditRecipePresenterImpl(private val mainView: EditRecipeView?) : EditRecipePresenter {

    private val dataProvider: RecipeProvider = RecipeProviderImpl()

    override fun saveRecipe(recipe: Recipe?, newName: String, newDesc: String) {
        mainView?.showProgressBar()

        var newRecipe = recipe
        if (newRecipe == null) {
            newRecipe = Recipe(id = String(), name = newName, desc = newDesc, userId = FirebaseAuth.getInstance().currentUser?.uid,
                    userName = FirebaseAuth.getInstance().currentUser?.displayName)
        } else {
            newRecipe.name = if (!newName.isEmpty()) newName else newRecipe.name
            newRecipe.desc = newDesc
        }
        if (newRecipe.id.isEmpty()) {
            dataProvider.createRecipe(newRecipe)
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
            dataProvider.update(newRecipe)
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
}