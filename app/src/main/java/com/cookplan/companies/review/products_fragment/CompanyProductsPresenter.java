package com.cookplan.companies.review.products_fragment;

import com.cookplan.models.Product;

import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public interface CompanyProductsPresenter {

    public void getCompanyProductList();

    void onStop();

    void deleteProductsFromCompany(List<Product> products);

    void updateProducts(List<Product> newProducts);
}
