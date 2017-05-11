package com.cookplan.companies.list;

import com.cookplan.models.Company;

import java.util.ArrayList;
import java.util.List;

public class CompanyMultiselectRecyclerAdapter extends CompanyListBaseAdapter {

    private final List<Company> selectedValues;

    public CompanyMultiselectRecyclerAdapter(List<Company> items, CompanyListEventListener listener) {
        super(items, listener);
        selectedValues = new ArrayList<>();
    }

    @Override
    protected void onCompanyClick(Company company) {
        if (selectedValues.contains(company)) {
            selectedValues.remove(company);
        } else {
            selectedValues.add(company);
        }
    }

    @Override
    protected void setCustomFields(Company company, ViewHolder holder) {
        if (selectedValues.contains(company)) {
            holder.mainView.setSelected(true);
        } else {
            holder.mainView.setSelected(false);
        }
    }
}
