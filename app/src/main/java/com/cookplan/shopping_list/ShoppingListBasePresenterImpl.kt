package com.cookplan.shopping_list


import com.cookplan.models.Ingredient
import com.cookplan.models.ShareUserInfo
import com.cookplan.providers.FamilyModeProvider
import com.cookplan.providers.IngredientProvider
import com.cookplan.providers.impl.FamilyModeProviderImpl
import com.cookplan.providers.impl.IngredientProviderImpl
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by DariaEfimova on 24.03.17.
 */

abstract class ShoppingListBasePresenterImpl : ShoppingListBasePresenter, FirebaseAuth.AuthStateListener {

    protected var ingredientDataProvider: IngredientProvider
    private val familyModeProvider: FamilyModeProvider
    private val disposables: CompositeDisposable

    init {
        this.ingredientDataProvider = IngredientProviderImpl()
        FirebaseAuth.getInstance().addAuthStateListener(this)
        familyModeProvider = FamilyModeProviderImpl()
        disposables = CompositeDisposable()
    }

    override fun onStop() {
        disposables.clear()
    }

    override fun getShoppingList() {
        disposables.add(
                familyModeProvider.getInfoSharedToMe()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<List<ShareUserInfo>>() {
                            override fun onNext(shareUserInfos: List<ShareUserInfo>) {
                                disposables.add(
                                        ingredientDataProvider.getAllIngredientsSharedToUser(shareUserInfos)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeWith(object : DisposableObserver<List<Ingredient>>() {

                                                    override fun onNext(ingredients: List<Ingredient>) {
                                                        sortIngredientList(ingredients)
                                                    }

                                                    override fun onError(e: Throwable) {
                                                        setError(e.message)
                                                    }

                                                    override fun onComplete() {

                                                    }
                                                }))
                            }

                            override fun onError(e: Throwable) {
                                setError(e.message)
                            }

                            override fun onComplete() {

                            }
                        }))
    }

    protected abstract fun setError(message: String?)

    abstract fun sortIngredientList(userIngredients: List<Ingredient>)

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        getShoppingList()
    }
}