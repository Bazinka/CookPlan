package com.cookplan.companies.list;

import com.cookplan.models.Company;

import java.util.ArrayList;
import java.util.List;

public class CompanyMultiselectRecyclerAdapter extends CompanyListBaseAdapter {

    private final List<Company> selectedValues;

    public CompanyMultiselectRecyclerAdapter(List<Company> items, CompanyListEventListener listener) {
        super(items, listener);
        selectedValues = new ArrayList<>();
        getSelectedItems(items);
    }

    private void getSelectedItems(List<Company> companies) {
        for (Company company : companies) {
            if (company.isAddedToGeoFence()) {
                selectedValues.add(company);
            }
        }
    }

    public void updateItems(List<Company> companies) {
        super.updateItems(companies);
        selectedValues.clear();
        getSelectedItems(companies);
    }

    public List<Company> getSelectedValues() {
        return selectedValues;
    }

    @Override
    protected void onCompanyClick(Company company) {
        if (selectedValues.contains(company)) {
            selectedValues.remove(company);
        } else {
            selectedValues.add(company);
        }
        notifyDataSetChanged();
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
