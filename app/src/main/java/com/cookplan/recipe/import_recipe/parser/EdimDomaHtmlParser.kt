package com.cookplan.recipe.import_recipe.parser

import android.content.Context
import com.cookplan.models.*
import com.cookplan.providers.ProductProvider
import com.cookplan.providers.impl.ProductProviderImpl
import com.cookplan.utils.MeasureUnitUtils
import com.cookplan.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * Created by DariaEfimova on 09.06.17.
 */

class EdimDomaHtmlParser(url: String, private val getContext: () -> Context?) : BaseParser(url) {

    private val productDataProvider: ProductProvider
    private val disposables: CompositeDisposable

    private val productNameTag: String
        get() = "div.checkbox-info__name"

    private val ingredientItemTag: String
        get() = "tr.definition-list-table__tr"

    private val amountTag: String
        get() = "td.definition-list-table__td_value"

    init {
        productDataProvider = ProductProviderImpl()
        disposables = CompositeDisposable()
    }

    override fun parseDocument(doc: Document) {
        val names = getProductsNames(doc)
        disposables.add(
                productDataProvider.getTheClosestProductsToStrings(names)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableObserver<MutableMap<String, List<Product>>>() {
                            override fun onNext(namesToProducts: MutableMap<String, List<Product>>) {
                                disposables.clear()
                                val ingredients = parceDocumentToIngredientList(doc, namesToProducts)
                                val recipe = parceDocumentToRecipe(doc)
                                onImportSuccess(recipe, ingredients)

                            }


                            override fun onError(e: Throwable) {
                                onImportError(e)
                            }

                            override fun onComplete() {

                            }
                        }))
    }

    private fun parceDocumentToRecipe(doc: Document): Recipe {
        val name = parseRecipeTitleFromDoc(doc)

        val description = parseDescriptionFromDoc(doc)

        val imageUrls = parseImageUrlsFromDoc(doc)

        return Recipe(
                id = String(),
                name = name,
                desc = description,
                descImageUrls = arrayListOf(),
                imageUrls = imageUrls,
                userId = FirebaseAuth.getInstance().currentUser?.uid,
                userName = FirebaseAuth.getInstance().currentUser?.displayName)
    }


    private fun parseDescriptionFromDoc(doc: Document): String {
        var description = String()
        val descriptionList = doc.select("div.content-box")
        descriptionList
                .asSequence()
                .filter { it.select("div.recipe-step-title").size != 0 }
                .forEach { description = description + it.select("div.plain-text").text() + "\n" }
        return description
    }

    private fun parseImageUrlsFromDoc(doc: Document): List<String> {
        val imageUrls = mutableListOf<String>()
        val imagesElements = doc.select("div.thumb-slider__slide")
        imagesElements
                .filter { it.children().size == 1 }
                .map { it.child(0).attr("src") }
                .filterNot { it.isEmpty() }
                .mapTo(imageUrls) { "https:" + it }
        return imageUrls
    }

    private fun parseRecipeTitleFromDoc(doc: Document): String {
        return doc.select("h1.recipe-header__name").text()
    }

    private fun getProductsNames(doc: Document): List<String> {
        val products = doc.select(ingredientItemTag)
        val names = products
                .map { parseStringProductName(it) }
                .filterNot { it.isEmpty() }

        return names
    }

    private fun parseStringProductName(element: Element): String {
        val nameElem = element.select(productNameTag)
        return nameElem.text() ?: String()
    }


    private fun getMeasureUnitString(amount: Double, element: Element): String {
        val amountElem = element.select(amountTag)
        var amountUnitString = String()
        if (amountElem.size == 1) {
            amountUnitString = amountElem.text()
            amountUnitString = amountUnitString.replace("½ ", "")
            amountUnitString = amountUnitString.replace("¾ ", "")
            val amountString: String
            val value = Math.abs(amount - Math.round(amount))
            if (value < 1e-8 && value > -1e-8) {
                amountString = amount.toInt().toString()
            } else {
                amountString = amount.toString()
            }
            amountUnitString = amountUnitString.replace(amountString + " ", "")
        }
        return amountUnitString
    }

    private fun parceDocumentToIngredientList(doc: Document, namesToProducts: MutableMap<String, List<Product>>): MutableMap<String, List<Ingredient>> {
        val ingredientMap = mutableMapOf<String, List<Ingredient>>()
        val ingredElements = doc.select(ingredientItemTag)
        for (element in ingredElements) {
            val name = parseStringProductName(element)
            if (!name.isEmpty()) {
                val ingredients = (namesToProducts[name] ?: listOf()).map { parseIngredient(it, element) }
                ingredientMap.put(name + ": " + element.select(amountTag).text(), ingredients)
            }
        }
        return ingredientMap
    }

    private fun parseIngredient(product: Product, element: Element): Ingredient {
        val amount = parseAmount(element)
        val unit = parseMeasureUnit(amount, element)
        return Ingredient(null,
                product.toStringName(),
                product, null,
                unit,
                amount,
                ShopListStatus.NONE)
    }

    private fun parseMeasureUnit(amount: Double, element: Element): MeasureUnit =
            MeasureUnitUtils.parseUnit(getMeasureUnitString(amount, element), getContext)

    private fun parseAmount(element: Element): Double {
        var amount = 0.toDouble()
        val amountElem = element.select(amountTag)
        if (amountElem.size == 1) {
            val amountString = amountElem.text()
            if (!amountString.contains(MeasureUnit.Companion.getByTasteString())) {
                val splited = amountString.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                amount = Utils.getDoubleFromString(splited[0])
            }
        }
        return amount
    }
}