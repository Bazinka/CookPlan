package com.cookplan.recipe.import_recipe.parser

import android.content.Context

/**
 * Created by DariaEfimova on 09.06.17.
 */

object ParserFactory {

    fun createParser(url: String, getContext: () -> Context?): Parser? {
        if (url.contains("eda.ru")) {
            return EdaTextParser(url, getContext)
        }
        return if (url.contains("www.edimdoma.ru")) {
            EdimDomaHtmlParser(url, getContext)
        } else null
    }
}