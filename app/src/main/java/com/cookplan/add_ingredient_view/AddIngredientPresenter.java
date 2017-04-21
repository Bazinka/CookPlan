package com.cookplan.add_ingredient_view;

import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;
import com.cookplan.models.Recipe;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public interface AddIngredientPresenter {

    public void setRecipe(Recipe recipe);

    public void setIsNeedToBuy(boolean isNeedToBuy);

    public boolean isNeedToBuy();

    public void getAsyncProductList();

    public void saveIngredient(Product product, double amount, MeasureUnit measureUnit);

    public void saveProductAndIngredient(ProductCategory category, String name,
                                         double amount, MeasureUnit measureUnit);
}
