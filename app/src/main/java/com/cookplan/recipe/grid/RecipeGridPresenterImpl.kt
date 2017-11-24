package com.cookplan.recipe.grid


import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.models.ShareUserInfo
import com.cookplan.providers.FamilyModeProvider
import com.cookplan.providers.IngredientProvider
import com.cookplan.providers.RecipeProvider
import com.cookplan.providers.impl.FamilyModeProviderImpl
import com.cookplan.providers.impl.IngredientProviderImpl
import com.cookplan.providers.impl.RecipeProviderImpl
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by DariaEfimova on 21.03.17.
 */

class RecipeGridPresenterImpl(private val mainView: RecipeGridView) : RecipeGridPresenter, FirebaseAuth.AuthStateListener {
    private val recipeDataProvider: RecipeProvider
    private val ingredientDataProvider: IngredientProvider
    private val familyModeProvider: FamilyModeProvider
    private val disposables: CompositeDisposable

    init {
        FirebaseAuth.getInstance().addAuthStateListener(this)
        recipeDataProvider = RecipeProviderImpl()
        ingredientDataProvider = IngredientProviderImpl()
        familyModeProvider = FamilyModeProviderImpl()
        disposables = CompositeDisposable()
    }

    override fun getRecipeList() {
        disposables.add(familyModeProvider.infoSharedToMe
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<ShareUserInfo>>() {
                    override fun onNext(shareUserInfos: List<ShareUserInfo>) {
                        disposables.add(
                                recipeDataProvider.getSharedToMeRecipeList(shareUserInfos)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(object : DisposableObserver<List<Recipe>>() {

                                            override fun onNext(recipeList: List<Recipe>) {
                                                if (recipeList.isEmpty()) {
                                                    mainView.setEmptyView()
                                                } else {
                                                    mainView.setRecipeList(recipeList)
                                                }
                                            }

                                            override fun onError(e: Throwable) {
                                                mainView.setErrorToast(e.message ?: String())
                                            }

                                            override fun onComplete() {}
                                        }))
                    }

                    override fun onError(e: Throwable) {
                        mainView.setErrorToast(e.message ?: String())
                    }

                    override fun onComplete() {

                    }
                }))
    }

    override fun removeRecipe(recipe: Recipe) {
        disposables.add(ingredientDataProvider.getIngredientListByRecipeId(recipe.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Ingredient>>() {

                    override fun onNext(ingredients: List<Ingredient>) {
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
                                    }

                                    override fun onError(e: Throwable) {
                                        mainView.setErrorToast(e.message ?: String())
                                    }
                                })
                    }

                    override fun onError(e: Throwable) {
                        mainView.setErrorToast(e.message ?: String())
                    }

                    override fun onComplete() {

                    }
                }))
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
                        mainView.setErrorToast(e.message ?: String())
                    }
                })
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser != null) {
            getRecipeList()
        }
    }

    override fun onStop() {
        disposables.clear()
    }
}
