package com.cookplan.shopping_list.total_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.shopping_list.total_list.TotalShoppingListFragment.OnListFragmentInteractionListener;

import java.util.List;


public class TotalShopListRecyclerViewAdapter extends RecyclerView.Adapter<TotalShopListRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;
    private final OnListFragmentInteractionListener listener;

    public TotalShopListRecyclerViewAdapter(List<Ingredient> items, OnListFragmentInteractionListener listener) {
        ingredients = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.total_shop_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.nameTextView.setText(ingredient.getName());

        if (ingredient.getAmount() != null && ingredient.getMeasureUnit() != null) {
            holder.amountTextView.setVisibility(View.VISIBLE);

            double amount = ingredient.getAmount();
            if (ingredient.getMeasureUnit().isItIntValue()) {
                holder.amountTextView.setText(String.valueOf((int) amount));
            } else {
                holder.amountTextView.setText(String.valueOf(amount));
            }

            holder.measureTextView.setVisibility(View.VISIBLE);
            holder.measureTextView.setText(ingredient.getMeasureUnit().toString());

        } else {
            holder.amountTextView.setVisibility(View.GONE);
            holder.measureTextView.setVisibility(View.GONE);
        }

        View.OnClickListener clickListener = view -> {
            int pos = (int) view.getTag();
            Ingredient selectIngredient = ingredients.get(pos);
            boolean isIngredientSelect = false;
            if (selectIngredient.isNeedToBuy()) {
                isIngredientSelect = false;
            } else {
                isIngredientSelect = true;
            }
            selectIngredient.setIsNeedToBuy(isIngredientSelect);

//            if (listener != null) {
//                listener.onIngredientItemSelected(selectIngredient);
//            }
        };
        holder.mainView.setTag(position);
        holder.mainView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void update(List<Ingredient> ingredientList) {
        ingredients.clear();
        ingredients.addAll(ingredientList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView measureTextView;
        public TextView amountTextView;
        public View mainView;

        public ViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.ingredient_item_name);
            measureTextView = (TextView) v.findViewById(R.id.ingredient_item_measure);
            amountTextView = (TextView) v.findViewById(R.id.ingredient_item_amount);
            mainView = v.findViewById(R.id.main_view);
        }
    }
}
