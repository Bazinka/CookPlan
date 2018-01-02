package com.cookplan.recipe.import_recipe.approve_result

import com.cookplan.R
import com.cookplan.RApplication
import com.cookplan.models.*
import com.cookplan.providers.IngredientProvider
import com.cookplan.providers.ProductProvider
import com.cookplan.providers.ProviderFactory
import com.cookplan.providers.RecipeProvider
import com.cookplan.recipe.import_recipe.parser.ParserFactory
import com.cookplan.recipe.import_recipe.parser.ParserResultListener
import com.cookplan.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by DariaEfimova on 08.06.17.
 */

class ImportRecipePresenterImpl(private val mainView: ImportRecipeView?) : ImportRecipePresenter {
    private val productDataProvider: ProductProvider
    private val recipeDataProvider: RecipeProvider
    private val ingredientDataProvider: IngredientProvider
    private val disposables: CompositeDisposable
    private var allProductsList: List<Product> = listOf()
    private var recipeId: String? = null

    init {
        productDataProvider = ProviderFactory.Companion.productProvider
        recipeDataProvider = ProviderFactory.Companion.recipeProvider
        ingredientDataProvider = ProviderFactory.Companion.ingredientProvider
        disposables = CompositeDisposable()
    }

    override fun importRecipeFromUrl(uri: String) {
        getAsyncProductList()
        ParserFactory.createParser(uri)?.parceUrl(object : ParserResultListener {
            override fun onSuccess(recipe: Recipe, ingredientList: MutableMap<String, List<Ingredient>>) {
                mainView?.setImportResult(recipe, ingredientList)
            }

            override fun onError(error: String) {
                mainView?.setError(error)
            }
        }) ?: mainView?.setError(
                RApplication.appContext!!
                        .getString(R.string.error_import_from_the_wrong_site_title))
    }

    override fun getAllProductsList() = allProductsList

    private fun getAsyncProductList() {
        disposables.add(
                productDataProvider.getProductList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<List<Product>>() {
                            override fun onNext(products: List<Product>) {
                                disposables.clear()
                                allProductsList = products
                            }

                            override fun onError(e: Throwable) {
                            }

                            override fun onComplete() {

                            }
                        }))
    }

    override fun saveRecipe(recipe: Recipe) {
        recipeDataProvider.createRecipe(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Recipe> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(recipe: Recipe) {
                        recipeDataProvider.getRecipeById(recipe.id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : SingleObserver<Recipe> {
                                    override fun onSubscribe(d: Disposable) {

                                    }

                                    override fun onSuccess(recipe: Recipe) {
                                        recipeId = recipe.id
                                        mainView?.setRecipeSavedSuccessfully(recipe)
                                    }

                                    override fun onError(e: Throwable) {
                                        mainView?.setError(e.message ?: String())
                                    }
                                })
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setError(e.message ?: String())
                    }
                })
    }

    override fun saveProductAndIngredient(key: String, category: ProductCategory, name: String, amount: Double, measureUnit: MeasureUnit) {
        var map: Map<MeasureUnit, Double> = mapOf()
        var measureUnitList: MutableList<MeasureUnit> = MeasureUnit.values().toMutableList()
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
        val product = Product(category, rusName, engName, mutableListOf(measureUnit),
                measureUnitList, map, FirebaseAuth.getInstance().currentUser?.uid)
        product.increasingCount()
        productDataProvider.createProduct(product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Product> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(product: Product) {
                        //save ingredient
                        val ingredient = Ingredient(null, product.toStringName(), product, recipeId,
                                measureUnit, amount, ShopListStatus.NONE)
                        saveIngredient(key, ingredient)
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setError(e.message ?: String())
                    }
                })
    }

    override fun saveIngredient(key: String, ingredient: Ingredient) {
        ingredientDataProvider.createIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Ingredient> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(ingredient: Ingredient) {
                        mainView?.setIngredientSavedSuccessfully(key)
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setError(e.message ?: String())
                    }
                })
    }
}