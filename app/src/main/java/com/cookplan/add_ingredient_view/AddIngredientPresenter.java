package com.cookplan.add_ingredient_view;

import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public interface AddIngredientPresenter {

    void getAsyncProductList();

    void saveIngredient(Product product, double amount, MeasureUnit measureUnit);
}
