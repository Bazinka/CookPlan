package com.cookplan.recipe.grid


import android.util.Log
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.models.ShareUserInfo
import com.cookplan.providers.FamilyModeProvider
import com.cookplan.providers.IngredientProvider
import com.cookplan.providers.ProviderFactory
import com.cookplan.providers.RecipeProvider
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
        recipeDataProvider = ProviderFactory.Companion.recipeProvider
        ingredientDataProvider = ProviderFactory.Companion.ingredientProvider
        familyModeProvider = ProviderFactory.Companion.familyModeProvider
        disposables = CompositeDisposable()
    }

    override fun getRecipeList() {
        disposables.add(familyModeProvider.getInfoSharedToMe()
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

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
    }

    override fun onStop() {
        disposables.clear()
    }
}
