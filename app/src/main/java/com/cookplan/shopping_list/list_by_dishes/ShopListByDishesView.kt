package com.cookplan.shopping_list.list_by_dishes

import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.shopping_list.ShoppingListView

/**
 * Created by DariaEfimova on 24.03.17.
 */

interface ShopListByDishesView : ShoppingListView {

    fun setIngredientListToRecipe(newGroupList: MutableList<Recipe>, newChildMap: Map<String, List<Ingredient>>)
}
