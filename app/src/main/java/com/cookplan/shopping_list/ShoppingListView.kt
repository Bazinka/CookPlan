package com.cookplan.shopping_list

import com.cookplan.BaseView

/**
 * Created by DariaEfimova on 24.03.17.
 */

interface ShoppingListView : BaseView {
    fun setErrorToast(errorString: String)

    fun setEmptyView()

}
