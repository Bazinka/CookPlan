package com.cookplan.add_ingredient_view

import com.cookplan.RApplication
import com.cookplan.models.*
import com.cookplan.providers.IngredientProvider
import com.cookplan.providers.ProductProvider
import com.cookplan.providers.ProviderFactory
import com.cookplan.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by DariaEfimova on 20.03.17.
 */

class ProductForIngredientPresenterImpl(private val mainView: ProductForIngredientView?) : ProductForIngredientPresenter {

    override var productList: List<Product> = listOf()

    private var recipeId: String? = null
    override var isNeedToBuy: Boolean = false

    private val productDataProvider: ProductProvider = ProviderFactory.Companion.productProvider
    private val ingredientDataProvider: IngredientProvider = ProviderFactory.Companion.ingredientProvider
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun getAsyncProductList() {
        disposables.add(
                productDataProvider.getProductList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<List<Product>>() {
                            override fun onNext(products: List<Product>) {
                                productList = products.toList()
                                mainView?.setProductsList(products)
                            }

                            override fun onError(e: Throwable) {
                                mainView?.setErrorToast(e.message ?: String())
                            }

                            override fun onComplete() {

                            }
                        }))
    }

    override fun saveIngredient(product: Product?, amount: Double, measureUnit: MeasureUnit) {
        if (product != null) {
            productDataProvider.increaseCountUsages(product)
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
            //save ingredient
            val ingredient = Ingredient(null,
                    product.toStringName(),
                    product,
                    recipeId,
                    measureUnit,
                    amount,
                    if (isNeedToBuy) ShopListStatus.NEED_TO_BUY else ShopListStatus.NONE)

            ingredientDataProvider.createIngredient(ingredient)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<Ingredient> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onSuccess(ingredient: Ingredient) {
                            mainView?.setSuccessSaveIngredient()
                        }

                        override fun onError(e: Throwable) {
                            mainView?.setErrorToast(e.message ?: String())
                        }
                    })
        }
    }

    override fun saveProductAndIngredient(category: ProductCategory, name: String, amount: Double, measureUnit: MeasureUnit) {
        var map: Map<MeasureUnit, Double>? = null
        var measureUnitList: MutableList<MeasureUnit> = Arrays.asList(*MeasureUnit.values())
        if (measureUnit === MeasureUnit.KILOGRAMM) {
            map = Utils.kilogramUnitMap
            measureUnitList = Utils.weightUnitList
        }
        if (measureUnit === MeasureUnit.LITRE) {
            map = Utils.litreUnitMap
            measureUnitList = Utils.volumeUnitList
        }

        var rusName: String? = null
        var engName: String? = null
        if (RApplication.isCurrentLocaleRus) {
            rusName = name
        } else {
            engName = name
        }
        val product = Product(category, rusName, engName, mutableListOf<MeasureUnit>(measureUnit),
                measureUnitList, map, FirebaseAuth.getInstance().currentUser?.uid)
        productDataProvider.createProduct(product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Product> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(product: Product) {
                        saveIngredient(product, amount, measureUnit)
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setErrorToast(e.message ?: String())
                    }
                })
    }

    override fun onStop() {
        disposables.clear()
    }

    override fun setRecipeId(recipeId: String?) {
        this.recipeId = recipeId
    }
}
