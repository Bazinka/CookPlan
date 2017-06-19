package com.cookplan.recipe.import_recipe.approve_result;

import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;
import com.cookplan.models.Recipe;

import java.util.List;

/**
 * Created by DariaEfimova on 08.06.17.
 */

public interface ImportRecipePresenter {

    void importRecipeFromUrl(String uri);

    List<Product> getAllProductsList();

    void saveRecipe(Recipe recipe);

    void saveProductAndIngredient(String key, ProductCategory category, String name, double amount, MeasureUnit measureUnit);

    void saveIngredient(String key, Ingredient ingredient);
}
