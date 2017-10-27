package com.cookplan.add_ingredient_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.ProductCategory;

import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class ProductCategoriesSpinnerAdapter extends ArrayAdapter<ProductCategory> implements SpinnerAdapter {
    private List<ProductCategory> itemsAll;

    public ProductCategoriesSpinnerAdapter(Context context, List<ProductCategory> itemsAll) {
        super(context, R.layout.product_category_spinner_layout, itemsAll);
        this.itemsAll = itemsAll;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.product_category_dropdown_layout, null);
        }

        ProductCategory category = itemsAll.get(position);
        if (category != null) {
            TextView categoryNameTextView = (TextView) v.findViewById(R.id.category_product_item_name);
            if (categoryNameTextView != null) {
                categoryNameTextView.setText(category.toString());
                categoryNameTextView.setTextColor(ContextCompat.getColor(RApplication.Companion.getAppContext(),
                                                                         category.getColorId()));
            }
        }
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ProductCategory category = itemsAll.get(position);

        if (category != null) {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.product_category_spinner_layout, null);
            }
            TextView categoryNameTextView = (TextView) v.findViewById(R.id.category_product_item_name);
            if (categoryNameTextView != null) {
                categoryNameTextView.setText(category.toString());
            }
            View categoryView = v.findViewById(R.id.main_view);
            if (categoryView != null) {
                categoryView.setBackgroundResource(category.getColorId());
            }
        }

        return v;
    }
}