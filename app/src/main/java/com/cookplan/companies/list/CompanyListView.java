package com.cookplan.companies.list;

import com.cookplan.models.Company;

import java.util.List;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public interface CompanyListView {

    void setErrorToast(String error);

    void setCompanyList(List<Company> companyList);

    void setEmptyView();
}
