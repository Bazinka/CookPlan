package com.cookplan.recipe.create.ingredients

import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe

/**
 * Created by DariaEfimova on 15.01.2018.
 */
interface IngredientsToRecipePresenter {

    fun saveRecipe(recipe: Recipe?, newName: String = String(), newDesc: String?)

    fun removeIngredient(ingredient: Ingredient)
}