package com.cookplan.product_list;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Product;

import java.util.List;

public class ProductListRecyclerAdapter extends RecyclerView.Adapter<ProductListRecyclerAdapter.ViewHolder> {

    private final List<Product> mValues;
    private ProductListEventListener listener;

    public ProductListRecyclerAdapter(List<Product> items, ProductListEventListener listener) {
        mValues = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Product product = mValues.get(position);
        holder.nameView.setText(product.getName());

        holder.mainView.setTag(product);
        holder.mainView.setOnClickListener(v -> {
            Product localProduct = (Product) v.getTag();
            if (listener != null && localProduct != null) {
                listener.onProductClick(localProduct);
            }
        });

        if (product.getCategory() != null) {
            holder.categoryNameView.setVisibility(View.VISIBLE);
            holder.categoryColorView.setVisibility(View.VISIBLE);
            holder.categoryNameView.setText(product.getCategory().toString());
            int color = ContextCompat.getColor(RApplication.getAppContext(),
                                               product.getCategory().getColorId());
            holder.categoryNameView.setTextColor(color);
            holder.categoryColorView.setBackgroundResource(product.getCategory().getColorId());
        } else {
            holder.categoryNameView.setVisibility(View.GONE);
            holder.categoryColorView.setVisibility(View.GONE);
        }
    }

    public void updateItems(List<Product> products) {
        mValues.clear();
        mValues.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        public final TextView nameView;
        public final TextView categoryNameView;
        public final View categoryColorView;

        public ViewHolder(View view) {
            super(view);
            mainView = view;
            nameView = (TextView) view.findViewById(R.id.product_item_name);
            categoryNameView = (TextView) view.findViewById(R.id.category_product_item_name);
            categoryColorView = view.findViewById(R.id.category_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }

    public interface ProductListEventListener {
        public void onProductClick(Product product);
    }
}
