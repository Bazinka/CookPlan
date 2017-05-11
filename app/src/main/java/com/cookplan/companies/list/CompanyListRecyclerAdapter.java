package com.cookplan.companies.list;

import com.cookplan.models.Company;

import java.util.List;

public class CompanyListRecyclerAdapter extends CompanyListBaseAdapter {


    public CompanyListRecyclerAdapter(List<Company> items, CompanyListEventListener listener) {
        super(items, listener);
    }

    @Override
    protected void onCompanyClick(Company company) {

    }

    @Override
    protected void setCustomFields(Company company, ViewHolder holder) {

    }

}
