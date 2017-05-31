package com.cookplan.product_list.multiselect;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MultiselectProductListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<ProductCategory, List<Product>> categoryToProductMap;
    private List<ProductCategory> categoriesList;

    private ArrayList<Product> selectedProducts;

    public MultiselectProductListAdapter(Context context,
                                         List<Product> productList, List<Product> selectedProductList) {
        this.context = context;
        categoryToProductMap = new HashMap<>();
        categoriesList = new ArrayList<>();
        selectedProducts = new ArrayList<>();
        for (Product product : productList) {
            ProductCategory category = product.getCategory();
            List<Product> localList = null;
            if (categoriesList.contains(category)) {
                localList = categoryToProductMap.get(category);
                localList.add(product);
                categoryToProductMap.put(category, localList);
            } else {
                categoriesList.add(category);
                localList = new ArrayList<>();
                localList.add(product);
                categoryToProductMap.put(category, localList);
            }
            for (Product selected : selectedProductList) {
                if (selected.getId().equals(product.getId())) {
                    selectedProducts.add(product);
                }
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return categoryToProductMap.get(categoriesList.get(groupPosition)).get(childPosititon);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryToProductMap.get(categoriesList.get(groupPosition)).size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.product_list_item_layout, null);
        }
        Product product = (Product) getChild(groupPosition, childPosition);
        TextView nameView = (TextView) convertView.findViewById(R.id.product_item_name);
        nameView.setText(product.toStringName());

        View mainView = convertView.findViewById(R.id.main_view);
        mainView.setTag(product);
        mainView.setOnClickListener(v -> {
            Product localProduct = (Product) v.getTag();
            if (selectedProducts.contains(localProduct)) {
                selectedProducts.remove(localProduct);
            } else {
                selectedProducts.add(localProduct);
            }
            notifyDataSetChanged();
        });

        TextView categoryNameView = (TextView) convertView.findViewById(R.id.category_product_item_name);
        View categoryColorView = convertView.findViewById(R.id.category_view);
        if (product.getCategory() != null) {
            categoryNameView.setVisibility(View.VISIBLE);
            categoryColorView.setVisibility(View.VISIBLE);
            categoryNameView.setText(product.getCategory().toString());
            int color = ContextCompat.getColor(RApplication.getAppContext(),
                                               product.getCategory().getColorId());
            categoryNameView.setTextColor(color);
            categoryColorView.setBackgroundResource(product.getCategory().getColorId());
        } else {
            categoryNameView.setVisibility(View.GONE);
            categoryColorView.setVisibility(View.GONE);
        }

        boolean isProductSelected = isProductSelected(product);
        if (isProductSelected) {
            nameView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                                                         R.color.white));
            nameView.setPaintFlags(nameView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (product.getCategory() != null) {
                mainView.setBackgroundResource(product.getCategory().getColorId());
            }
        } else {
            nameView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                                                         R.color.primary_text_color));
            nameView.setPaintFlags(nameView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

            mainView.setBackgroundResource(R.color.white);
        }
        return convertView;
    }

    private boolean isProductSelected(Product product) {
        for (Product selectedProduct : selectedProducts) {
            if (selectedProduct.getId().equals(product.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoriesList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return categoriesList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ProductCategory category = (ProductCategory) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.product_category_group_item_layout, null);
        }

        TextView headerTextView = (TextView) convertView.findViewById(R.id.product_category_name);
        headerTextView.setText(category.toString());
        headerTextView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                                                           category.getColorId()));
        View categoryColorView = convertView.findViewById(R.id.category_view);
        categoryColorView.setBackgroundResource(category.getColorId());

        CheckBox groupCheckBox = (CheckBox) convertView.findViewById(R.id.product_category_checkBox);
        groupCheckBox.setTag(category);
        groupCheckBox.setOnClickListener(view -> {
            ProductCategory productCategory = (ProductCategory) view.getTag();
            boolean isChecked = groupCheckBox.isChecked();
            if (isChecked) {
                for (Product product : categoryToProductMap.get(productCategory)) {
                    if (!selectedProducts.contains(product)) {
                        selectedProducts.add(product);
                    }
                }
            } else {
                selectedProducts.removeAll(categoryToProductMap.get(productCategory));
            }
            notifyDataSetChanged();
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public ArrayList<Product> getSelectedProducts() {
        return selectedProducts;
    }
}
