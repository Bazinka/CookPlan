package com.cookplan.add_ingredient_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.ProductCategory;

import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class ProductCategoriesSpinnerAdapter extends ArrayAdapter<ProductCategory> implements SpinnerAdapter {
    private List<ProductCategory> itemsAll;

    public ProductCategoriesSpinnerAdapter(Context context, List<ProductCategory> itemsAll) {
        super(context, R.layout.measure_spinner_item_layout, itemsAll);
        this.itemsAll = itemsAll;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.measure_spinner_item_layout, null);
        }

        ProductCategory category = itemsAll.get(position);
        if (category != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.item_name);
            if (customerNameLabel != null) {
                customerNameLabel.setText(category.toString());
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
                v = vi.inflate(R.layout.measure_spinner_item_layout, null);
            }

            TextView customerNameLabel = (TextView) v.findViewById(R.id.item_name);
            if (customerNameLabel != null) {
                customerNameLabel.setText(category.toString());
            }
        }

        return v;
    }
}