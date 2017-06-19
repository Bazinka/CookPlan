package com.cookplan.recipe.import_recipe.approve_result;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 08.06.17.
 */

public interface ImportRecipeView {

    void setImportResult(Recipe recipe, Map<String, List<Ingredient>> ingredientList);

    void setError(String s);

    void setRecipeSavedSuccessfully(Recipe recipe);

    void setIngredientSavedSuccessfully(String key);
}
