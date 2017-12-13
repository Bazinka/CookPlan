package com.cookplan.recipe.import_recipe.parser

import com.cookplan.R
import com.cookplan.RApplication
import com.cookplan.models.*
import com.cookplan.providers.ProductProvider
import com.cookplan.providers.impl.ProductProviderImpl
import com.cookplan.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Created by DariaEfimova on 09.06.17.
 */

class EdaHtmlParser(url: String) : BaseParser(url) {


    private val productDataProvider: ProductProvider
    private val disposables: CompositeDisposable

    private val productNameTag: String
        get() = "span.content-item__name"

    private val ingredientItemTag: String
        get() = "p.ingredients-list__content-item"

    private val amountTag: String
        get() = "span.content-item__measure"

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

        return Recipe(String(), name, description, imageUrls, FirebaseAuth.getInstance().currentUser?.uid,
                FirebaseAuth.getInstance().currentUser?.displayName)
    }

    private fun parseDescriptionFromDoc(doc: Document): String {
        var description = String()
        val descriptionList = doc.select("div.step-description")
        for (desc in descriptionList) {
            description = description + desc.text() + "\n"
        }
        return description
    }

    private fun parseRecipeTitleFromDoc(doc: Document): String {
        return doc.title()
    }

    private fun parseImageUrlsFromDoc(doc: Document): List<String> {
        val imageUrls = mutableListOf<String>()
        val imagesElements = doc.select("div.g-first-page-block").select("div.s-photo-gall__trigger")
        val imageUrlsString = imagesElements.attr("data-gall-photos-urls")
        val splits = imageUrlsString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        splits
                .filterNot { it.isEmpty() }
                .mapTo(imageUrls) { "https:" + it.replace("'", "") }
        return imageUrls
    }

    private fun getIngredientsElementList(doc: Document): Elements {
        val ingredients = doc.select("div.ingredients-list").first()
        return ingredients.select(ingredientItemTag)
    }


    private fun getProductsNames(doc: Document): List<String> {
        val names = mutableListOf<String>()
        val products = getIngredientsElementList(doc)
        products
                .map { parseStringProductName(it) }
                .filterNotTo(names) { it.isEmpty() }
        return names
    }

    private fun parseStringProductName(element: Element): String {
        var nameElem = element.select(productNameTag)
        if (nameElem.isEmpty()) {
            nameElem = element.select("span.name")//if product doesn't have category on the website.
        }
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
        val ingredElements = getIngredientsElementList(doc)
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
            MeasureUnit.parseUnit(getMeasureUnitString(amount, element))

    private fun parseAmount(element: Element): Double {
        var amount = 0.toDouble()
        val amountElem = element.select(amountTag)
        if (amountElem.size == 1) {
            val amountString = amountElem.text()
            if (!amountString.contains(RApplication.appContext!!.getString(R.string.by_the_taste))) {
                val splited = amountString.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                amount = Utils.getDoubleFromString(splited[0])
            }
//            if (!amountString.contains(RApplication.Companion.getAppContext().getString(R.string.by_the_taste))) {
//                String[] splited = amountString.split("\\s+");
//                amount = Utils.getDoubleFromString(splited[0]);
//            }
        }
        return amount
    }
}