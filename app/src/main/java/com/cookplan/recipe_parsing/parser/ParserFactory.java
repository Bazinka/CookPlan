package com.cookplan.recipe_parsing.parser;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public class ParserFactory {

    public static final Parser createParser(String url) {
        if (url.contains("eda.ru")) {
            return new EdaParser(url);
        }
        return null;
    }
}