package com.cookplan.recipe.import_recipe.approve_result

import com.cookplan.models.Ingredient
import com.cookplan.models.MeasureUnit
import com.cookplan.models.Product
import com.cookplan.models.ProductCategory
import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 08.06.17.
 */

interface ImportRecipePresenter {

    fun getAllProductsList(): List<Product>

    fun importRecipeFromUrl(uri: String)

    fun saveRecipe(recipe: Recipe)

    fun saveProductAndIngredient(key: String, category: ProductCategory, name: String, amount: Double, measureUnit: MeasureUnit)

    fun saveIngredient(key: String, ingredient: Ingredient)
}
