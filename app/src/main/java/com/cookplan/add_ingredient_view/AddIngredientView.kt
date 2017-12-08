package com.cookplan.add_ingredient_view

import com.cookplan.models.MeasureUnit
import com.cookplan.models.Product

/**
 * Created by DariaEfimova on 20.03.17.
 */

interface AddIngredientView {

    val isAddedToActivity: Boolean

    fun setErrorToast(error: String)

    fun setErrorToast(errorId: Int)

    fun setProductsList(productsList: List<Product>)

    fun setSuccessSaveIngredient()
}
