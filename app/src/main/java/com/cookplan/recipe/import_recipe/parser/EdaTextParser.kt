package com.cookplan.recipe.import_recipe.parser

import android.util.Log
import com.cookplan.models.*
import com.cookplan.providers.ProductProvider
import com.cookplan.providers.impl.ProductProviderImpl
import com.cookplan.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.internal.Util
import org.jsoup.nodes.Document
import java.util.regex.Pattern

/**
 * Created by DariaEfimova on 09.06.17.
 */

class EdaTextParser(url: String) : BaseParser(url) {

    private val productDataProvider: ProductProvider
    private val disposables: CompositeDisposable

    init {
        productDataProvider = ProductProviderImpl()
        disposables = CompositeDisposable()
    }

    override fun parseDocument(doc: Document) {
        val recipeText = doc.text()

        val names = mutableListOf<String>()
        val ingredientsStringList = mutableListOf<String>()
        val ingredListMatcher = Pattern.compile(INGRIDIENTS_LIST_PATTERN).matcher(recipeText)
        if (ingredListMatcher.find()) {
            val ingredientsListString = ingredListMatcher.group().toString()

            val ingredMatcher = Pattern.compile(INGRIDIENT_PATTERN).matcher(ingredientsListString)
            var start = 0
            while (ingredMatcher.find(start)) {
                val ingredient = ingredMatcher.group().toString()
                Log.d("PARSING", "ingredient: " + ingredient)
                ingredientsStringList.add(ingredient)

                names.add(parseStringProductName(ingredient))

                start = ingredMatcher.start() + 1
            }
        } else {
            Log.d("PARSING", "didn't found ingredients.")
        }

        if (!names.isEmpty()) {
            disposables.add(
                    productDataProvider.getTheClosestProductsToStrings(names)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(object : DisposableObserver<MutableMap<String, List<Product>>>() {
                                override fun onNext(namesToProducts: MutableMap<String, List<Product>>) {
                                    disposables.clear()
                                    val ingredients = parceStringToIngredientList(ingredientsStringList, namesToProducts)
                                    val recipe = getRecipeObject(recipeText)
                                    recipe.imageUrls = parseImageUrlsFromDoc(doc)
                                    onImportSuccess(recipe, ingredients)
                                }


                                override fun onError(e: Throwable) {
                                    onImportError(e)
                                }

                                override fun onComplete() {

                                }
                            }))
        }
    }

    private fun getRecipeObject(allRecipeText: String): Recipe {
        val recipeNameMatcher = Pattern.compile(RECIPE_NAME_PATTERN).matcher(allRecipeText)
        var name = String()
        if (recipeNameMatcher.find()) {
            name = recipeNameMatcher.group().toString()
        }

        val recipeDescMatcher = Pattern.compile(RECIPE_DESCRIPTION_PATTERN).matcher(allRecipeText)
        var recipeDescription = String()
        var start = 0
        while (recipeDescMatcher.find(start)) {
            if (!recipeDescription.contains(recipeDescMatcher.group().toString())) {
                recipeDescription += recipeDescMatcher.group().toString() + Utils.SEPARATOR_IN_TEXT
            }
            Log.d("PARSING", "recipe desc: " + recipeDescMatcher.group().toString())
            start = recipeDescMatcher.start() + 1
        }

        return Recipe(String(), name, recipeDescription,
                listOf(), FirebaseAuth.getInstance().currentUser?.uid,
                FirebaseAuth.getInstance().currentUser?.displayName)
    }

    private fun parseImageUrlsFromDoc(doc: Document): List<String> {
        val imageUrls = mutableListOf<String>()

        var imagesElements = doc.select("img")
        imagesElements = if (imagesElements.isEmpty()) doc.select("amp-img") else imagesElements
        imagesElements
                .map { it.absUrl("src") }  //absolute URL on src
                .filterTo(imageUrls) { it.contains("eda.ru") }
        return imageUrls
    }

    private fun parseStringProductName(ingredientString: String): String {
        val ingredNameMatcher = Pattern.compile(INGRIDIENT_NAME_PATTERN).matcher(ingredientString)
        var name = String()
        if (ingredNameMatcher.find()) {
            name = ingredNameMatcher.group().toString()
            Log.d("PARSING", "ingredient name: " + name)
        }
        return name
    }

    private fun parceStringToIngredientList(ingredientListString: List<String>, namesToProducts: MutableMap<String, List<Product>>): MutableMap<String, List<Ingredient>> {
        val ingredientMap = mutableMapOf<String, List<Ingredient>>()
        for (element in ingredientListString) {
            val name = parseStringProductName(element)
            if (!name.isEmpty()) {
                val ingredients = (namesToProducts[name] ?: listOf()).map { parseIngredient(it, element) }
                ingredientMap.put(element, ingredients)
            }
        }
        return ingredientMap
    }

    private fun parseIngredient(product: Product, ingredientAllText: String): Ingredient {
        val amountMatcher = Pattern.compile(RECIPE_AMOUNT_PATTERN).matcher(ingredientAllText)
        var amount = 0.toDouble()
        if (amountMatcher.find()) {
            amount = Utils.getDoubleFromString(amountMatcher.group().toString())
            Log.d("PARSING", "amount: " + amount)
        }

        val measureMatcher = Pattern.compile(RECIPE_MEASURE_PATTERN).matcher(ingredientAllText)
        var unit = MeasureUnit.UNITS
        if (measureMatcher.find()) {
            unit = MeasureUnit.parseUnit(measureMatcher.group().toString())
            Log.d("PARSING", "amount: " + amount!!)
        }

        return Ingredient(id = null,
                name = product.toStringName(),
                product = product,
                recipeId = null,
                localMeasureUnit = unit,
                mainAmount = amount,
                shopListStatus = ShopListStatus.NONE)
    }

    companion object {

        private val INGRIDIENTS_LIST_PATTERN = "Ингредиенты" + "(\\s[\\p{InCyrillic}\\d]+)+(\\s\\d+)(\\s[\\p{InCyrillic}\\d]+)"

        private val INGRIDIENT_PATTERN = "(\\p{Lu}\\p{InCyrillic}+)(\\s\\p{InCyrillic}+)?(\\s\\d+)(\\s[\\p{InCyrillic}\\d]+)"

        private val INGRIDIENT_NAME_PATTERN = "(\\p{Lu}\\p{InCyrillic}+)(\\s\\p{InCyrillic}+)?"

        private val RECIPE_AMOUNT_PATTERN = "([\\d½¾¼]+)"

        private val RECIPE_MEASURE_PATTERN = "(\\s[\\p{InCyrillic}.]+)+$"

        private val RECIPE_DESCRIPTION_PATTERN = "(\\s\\d+[.]((\\s[\\p{InCyrillic},]+)+[.])+)+"

        private val RECIPE_NAME_PATTERN = "^\\p{InCyrillic}+(\\s[\\p{InCyrillic},]+)+"
    }

}