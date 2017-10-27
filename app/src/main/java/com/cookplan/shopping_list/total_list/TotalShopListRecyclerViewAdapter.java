package com.cookplan.shopping_list.total_list;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Ingredient;
import com.cookplan.models.ShopListStatus;

import java.util.List;


public class TotalShopListRecyclerViewAdapter extends RecyclerView.Adapter<TotalShopListRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;
    private final OnItemClickListener listener;

    public TotalShopListRecyclerViewAdapter(List<Ingredient> items, OnItemClickListener listener) {
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

        if (ingredient.getShopListStatus() == ShopListStatus.ALREADY_BOUGHT) {
            holder.nameTextView.setTextColor(ContextCompat.getColor(RApplication.Companion.getAppContext(),
                                                                    R.color.white));
            holder.nameTextView.setPaintFlags(holder.nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.amountTextView.setTextColor(ContextCompat.getColor(RApplication.Companion.getAppContext(),
                                                                      R.color.white));
            holder.amountTextView.setPaintFlags(holder.amountTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (ingredient.getCategory() != null) {
                holder.mainView.setBackgroundResource(ingredient.getCategory().getColorId());
            }
        } else {
            holder.nameTextView.setTextColor(ContextCompat.getColor(RApplication.Companion.getAppContext(),
                                                                    R.color.primary_text_color));
            holder.nameTextView.setPaintFlags(holder.nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.amountTextView.setTextColor(ContextCompat.getColor(RApplication.Companion.getAppContext(),
                                                                      R.color.primary_text_color));
            holder.amountTextView.setPaintFlags(holder.amountTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.mainView.setBackgroundResource(R.color.white);
        }

        holder.nameTextView.setText(ingredient.getName());


        if (ingredient.getAmountString() != null) {
            holder.amountTextView.setVisibility(View.VISIBLE);

            holder.amountTextView.setText(ingredient.getAmountString());
        } else {
            holder.amountTextView.setVisibility(View.GONE);
        }

        if (ingredient.getCategory() != null) {
            holder.categoryView.setBackgroundResource(ingredient.getCategory().getColorId());
        }

        View.OnClickListener clickListener = view -> {
            int pos = (int) view.getTag();
            Ingredient selectIngredient = ingredients.get(pos);
            if (listener != null) {
                listener.OnClick(selectIngredient);
            }
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

    public void removeItem(Ingredient ingredient) {
        ingredients.remove(ingredient);
        notifyDataSetChanged();
    }

    public void addItem(Ingredient ingredient) {
        ingredients.add(ingredient);
        notifyDataSetChanged();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView amountTextView;
        View categoryView;
        public View mainView;

        ViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.ingredient_item_name);
            amountTextView = (TextView) v.findViewById(R.id.ingredient_item_amount);
            categoryView = v.findViewById(R.id.category_view);
            mainView = v.findViewById(R.id.main_view);
        }
    }

    public interface OnItemClickListener {
        void OnClick(Ingredient ingredient);
    }
}
