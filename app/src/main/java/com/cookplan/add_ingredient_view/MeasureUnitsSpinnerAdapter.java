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
import com.cookplan.models.MeasureUnit;

import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class MeasureUnitsSpinnerAdapter extends ArrayAdapter<MeasureUnit> implements SpinnerAdapter {
    private List<MeasureUnit> selectedItems;
    private List<MeasureUnit> itemsAll;

    public MeasureUnitsSpinnerAdapter(Context context, List<MeasureUnit> itemsAll, List<MeasureUnit> selectedItems) {
        super(context, R.layout.measure_spinner_item_layout, itemsAll);
        this.selectedItems = selectedItems;
        this.itemsAll = itemsAll;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.measure_spinner_item_layout, null);
        }

        MeasureUnit unit = itemsAll.get(position);
        if (unit != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.item_name);
            if (customerNameLabel != null) {
                customerNameLabel.setText(unit.toString());
            }
        }
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        MeasureUnit unit = itemsAll.get(position);

        if (unit != null) {
            if (selectedItems.contains(unit)) {
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.measure_selected_spinner_item_layout, null);
                }
            } else {
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.measure_spinner_item_layout, null);
                }
            }

            TextView customerNameLabel = (TextView) v.findViewById(R.id.item_name);
            if (customerNameLabel != null) {
                customerNameLabel.setText(unit.toString());
            }
        }

        return v;
    }
}