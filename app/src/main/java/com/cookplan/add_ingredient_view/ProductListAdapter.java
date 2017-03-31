package com.cookplan.add_ingredient_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {
    private List<Product> items;
    private List<Product> itemsAll;
    private List<Product> suggestions;

    public ProductListAdapter(Context context, List<Product> items) {
        super(context, R.layout.product_autocomplete_item_layout, items);
        this.items = items;
        this.itemsAll = new ArrayList<>(items);
        this.suggestions = new ArrayList<>();
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.product_autocomplete_item_layout, null);
        }
        Product product = items.get(position);
        if (product != null) {
            TextView productNameTextView = (TextView) v.findViewById(R.id.item_name);
            if (productNameTextView != null) {
//              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                productNameTextView.setText(product.getName());
            }
            if (product.getCategory() != null) {
                TextView categoryNameTextView = (TextView) v.findViewById(R.id.category_item_name);
                if (categoryNameTextView != null) {
                    categoryNameTextView.setText(product.getCategory().toString());
                    categoryNameTextView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                            product.getCategory().getColorId()));
                }
                View categoryView = v.findViewById(R.id.category_view);
                if (categoryView != null) {
                    categoryView.setBackgroundColor(ContextCompat.getColor(RApplication.getAppContext(),
                            product.getCategory().getColorId()));
                }
            }

        }
        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Product) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Product product : itemsAll) {
                    if (product.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(product);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Product> filteredList = (ArrayList<Product>) results.values;
            if (results.count > 0) {
                clear();
                for (Product c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}