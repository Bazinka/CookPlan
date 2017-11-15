package com.cookplan.models

import com.cookplan.R

/**
 * Created by DariaEfimova on 29.03.17.
 */

enum class ToDoCategoryColor constructor(val colorId: Int) {
    BLACK(R.color.black),
    GREY(R.color.category_blue_grey),
    BROWN(R.color.category_brown),
    RED(R.color.category_dark_red),
    ORANGE(R.color.category_dark_orange),
    YELLOW(R.color.category_amber),
    GREEN(R.color.category_green),
    TEAL(R.color.category_teal),
    CYAN(R.color.category_cyan),
    BLUE(R.color.category_blue),
    DARK_BLUE(R.color.category_indigo),
    PURPLE(R.color.category_purple);

}
