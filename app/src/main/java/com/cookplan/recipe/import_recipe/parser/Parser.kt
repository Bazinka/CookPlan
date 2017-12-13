package com.cookplan.recipe.import_recipe.parser

/**
 * Created by DariaEfimova on 09.06.17.
 */

interface Parser {

    fun parceUrl(resultListener: ParserResultListener)

    companion object {

        val SEPARATOR_IN_TEXT = "\n"//[\r\n]+
    }
}
