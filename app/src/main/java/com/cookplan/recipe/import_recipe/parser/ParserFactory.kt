package com.cookplan.recipe.import_recipe.parser

/**
 * Created by DariaEfimova on 09.06.17.
 */

object ParserFactory {

    fun createParser(url: String): Parser? {
        if (url.contains("eda.ru")) {
            return EdaTextParser(url)
        }
        return if (url.contains("www.edimdoma.ru")) {
            EdimDomaHtmlParser(url)
        } else null
    }
}