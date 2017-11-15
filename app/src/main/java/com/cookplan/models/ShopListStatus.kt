package com.cookplan.models

/**
 * Created by DariaEfimova on 24.03.17.
 */

enum class ShopListStatus constructor(val id: Int) {
    NONE(0), NEED_TO_BUY(1), ALREADY_BOUGHT(2);


    companion object {

        fun getShopListStatusName(name: String): ShopListStatus? {
            if (name == NONE.name) {
                return NONE
            }
            if (name == NEED_TO_BUY.name) {
                return NEED_TO_BUY
            }
            return if (name == ALREADY_BOUGHT.name) {
                ALREADY_BOUGHT
            } else null
        }
    }
}
