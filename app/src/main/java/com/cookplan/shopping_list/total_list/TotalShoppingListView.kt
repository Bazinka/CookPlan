package com.cookplan.shopping_list.total_list

import com.cookplan.models.Ingredient
import com.cookplan.shopping_list.ShoppingListView

/**
 * Created by DariaEfimova on 24.03.17.
 */

interface TotalShoppingListView : ShoppingListView {

    fun setIngredientLists(AllIngredientsList: List<Ingredient>)
}
