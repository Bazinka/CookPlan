package com.cookplan.shopping_list.list_by_dishes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.shopping_list.list_by_dishes.ShopListByDishesFragment.OnListFragmentInteractionListener;

import java.util.List;


public class ShopListByDishesRecyclerViewAdapter extends RecyclerView.Adapter<ShopListByDishesRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;
    private final OnListFragmentInteractionListener listener;

    public ShopListByDishesRecyclerViewAdapter(List<Ingredient> items, OnListFragmentInteractionListener listener) {
        ingredients = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_list_by_dish_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.mIdView.setText(ingredient.getId());
        holder.mContentView.setText(ingredient.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
//                    listener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
