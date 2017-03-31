package com.cookplan.add_ingredient_view;

import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;

import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public interface AddIngredientView {

    void setErrorToast(String error);

    void setErrorToast(int errorId);

    void setProductsList(List<Product> productsList);

    void setSuccessSaveIngredient();

    void needMoreDataAboutProduct(Product product, MeasureUnit unit);

    boolean isAddedToActivity();
}
