package com.cookplan.companies.review.products_fragment;

import com.cookplan.models.Product;

import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public interface CompanyProductsView {

    void setErrorToast(String error);

    void setProductList(List<Product> productList);

    void setEmptyView();
}
