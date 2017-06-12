package com.cookplan.recipe_import.approve_result;

import com.cookplan.models.Product;
import com.cookplan.models.Recipe;

import java.util.List;

/**
 * Created by DariaEfimova on 08.06.17.
 */

public interface ImportRecipePresenter {

    void importRecipeFromUrl(String uri);

    List<Product> getAllProductsList();

    void saveRecipe(Recipe recipe);
}
