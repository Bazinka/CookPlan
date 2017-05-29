package com.cookplan.companies.list;

import com.cookplan.models.Company;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public interface CompanyListPresenter {

    void onStop();

    void getUsersCompanyList();

    void removeCompany(Company company);
}
