package com.cookplan.recipe.import_recipe.parser;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 09.06.17.
 */

public interface ParserResultListener {

    void onSuccess(Recipe recipe, Map<String, List<Ingredient>> ingredientList);

    void onError(String error);
}