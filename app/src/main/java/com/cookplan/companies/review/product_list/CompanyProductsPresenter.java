package com.cookplan.companies.review.product_list;

import com.cookplan.models.Company;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public interface CompanyProductsPresenter {

    public void getCompanyProductList(Company company);

    void onStop();
}
