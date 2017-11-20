package com.cookplan.shopping_list.total_list


import com.cookplan.RApplication
import com.cookplan.models.Ingredient
import com.cookplan.models.MeasureUnit
import com.cookplan.models.ShopListStatus
import com.cookplan.shopping_list.ShoppingListBasePresenterImpl
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.Map.Entry

/**
 * Created by DariaEfimova on 24.03.17.
 */

class TotalShoppingListPresenterImpl(private val mainView: TotalShoppingListView?) : ShoppingListBasePresenterImpl(), TotalShoppingListPresenter {
    private var ProductToIngredientMap: HashMap<String, List<Ingredient>> = hashMapOf()

    override fun setError(message: String?) {
        mainView?.setErrorToast(message ?: "")
    }

    override fun sortIngredientList(userIngredients: List<Ingredient>) {

        //fill the map
        ProductToIngredientMap.clear()
        for (ingredient in userIngredients) {
            if (ingredient.shopListStatus !== ShopListStatus.NONE) {
                val ingredients = (ProductToIngredientMap.get(ingredient.name) ?: arrayListOf()) as ArrayList<Ingredient>

                ingredients.add(ingredient)
                ProductToIngredientMap.put(ingredient.name ?: String(), ingredients)
            }
        }

        //calculate amounts
        val needToBuyIngredients = ArrayList<Ingredient>()
        for (entry in ProductToIngredientMap.entries) {
            val needToBuyIng = getShopListIngredient(entry, ShopListStatus.NEED_TO_BUY)
            if (needToBuyIng != null) {
                needToBuyIngredients.add(needToBuyIng)
            }

            val alreadyBoughtIngred = getShopListIngredient(entry, ShopListStatus.ALREADY_BOUGHT)
            if (alreadyBoughtIngred != null) {
                needToBuyIngredients.add(alreadyBoughtIngred)
            }
        }
        val needToBuySortedIngredients = ArrayList<Ingredient>()
        for (category in RApplication.getPriorityList()) {
            for (ingredient in needToBuyIngredients) {
                if (ingredient.category === category) {
                    needToBuySortedIngredients.add(ingredient)
                }
            }
        }
        if (ProductToIngredientMap.size != 0) {
            mainView?.setIngredientLists(needToBuySortedIngredients)
        } else {
            mainView?.setEmptyView()
        }
    }

    private fun getShopListIngredient(entry: Entry<String, List<Ingredient>>, status: ShopListStatus): Ingredient? {
        val productName = entry.key
        val ingredients = entry.value
        if (!ingredients.isEmpty()) {
            val shopMap = HashMap<MeasureUnit, Double>()
            val restMap = HashMap<MeasureUnit, Double>()
            for (ingredient in ingredients) {
                if (ingredient.shopListStatus === status) {
                    for (i in 0 until ingredient.shopMeasureList.size) {
                        val shopUnit = ingredient.shopMeasureList.get(i)
                        val shopAmount = ingredient.shopAmountList.get(i)
                        if (shopAmount > 1e-8) {
                            var mapAmount = shopMap.get(shopUnit) ?: 0.toDouble()
                            var localAmount = mapAmount + shopAmount
                            shopMap.put(shopUnit, localAmount)
                        }
                    }

                    if (shopMap.isEmpty()) {
                        val restUnit = ingredient.mainMeasureUnit
                        val restAmount = ingredient.mainAmount
                        var mapAmount = restMap.get(restUnit) ?: 0.toDouble()
                        var localAmount = mapAmount + restAmount
                        restMap.put(restUnit, localAmount)
                    }
                }
            }

            if (restMap.isEmpty() && shopMap.isEmpty()) {
                return null
            }
            var shopAmountString = ""
            for ((key, value) in shopMap) {
                val string = key.toStringForShopList(value)
                if (shopAmountString.isEmpty()) {
                    shopAmountString = string
                } else {
                    shopAmountString = "$shopAmountString ($string)"
                }
            }
            var restAmountString = ""
            for ((key, value) in restMap) {
                val string = key.toStringForShopList(value)
                restAmountString = if (restAmountString.isEmpty()) string else restAmountString + " + " + string
            }
            if (!restAmountString.isEmpty()) {
                shopAmountString = shopAmountString + " + " + restAmountString
            }
            val category = ingredients.get(0).category
            return Ingredient(productName, shopAmountString, status, category)
        } else {
            return null
        }
    }


    override fun changeShopListStatus(ingredient: Ingredient, newStatus: ShopListStatus) {
        val ingredientList = ProductToIngredientMap.get(ingredient.name) ?: listOf()
        for (realIngredient in ingredientList) {
            realIngredient.shopListStatus = newStatus
            ingredientDataProvider.updateShopStatus(realIngredient)
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

    override fun deleteIngredients(ingredients: List<Ingredient>) {
        for ((_, name, _, _, _, _, _, _, _, status) in ingredients) {
            val realIngredients = ProductToIngredientMap.get(name) ?: listOf()
            for (realIngredient in realIngredients) {
                if (status === realIngredient.shopListStatus) {
                    realIngredient.shopListStatus = ShopListStatus.NONE
                    if (realIngredient.recipeId == null) {
                        ingredientDataProvider.removeIngredient(realIngredient)
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
                    } else {
                        ingredientDataProvider.updateShopStatus(realIngredient)
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
            }
        }
    }
}