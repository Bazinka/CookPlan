package com.cookplan.recipe.import_recipe.parser;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public class ParserFactory {

    public static final Parser createParser(String url) {
        if (url.contains("eda.ru")) {
            return new EdaHtmlParser(url);
        }
        if (url.contains("www.edimdoma.ru")) {
            return new EdimDomaHtmlParser(url);
        }
        return null;
    }
}