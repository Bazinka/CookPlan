package com.cookplan.shopping_list.add_from_notify;

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

public class ToShopListIngredientsRecyclerAdapter extends RecyclerView.Adapter<ToShopListIngredientsRecyclerAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;

    public ToShopListIngredientsRecyclerAdapter(List<Ingredient> items) {
        ingredients = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_ingred_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        if (ingredient.getShopListStatus() == ShopListStatus.NEED_TO_BUY) {
            holder.nameTextView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                                                                    R.color.white));
            holder.amountTextView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                                                                      R.color.white));
            if (ingredient.getCategory() != null) {
                holder.mainView.setBackgroundResource(ingredient.getCategory().getColorId());
            }
        } else if (ingredient.getShopListStatus() == ShopListStatus.NONE) {
            holder.nameTextView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                                                                    R.color.primary_text_color));
            holder.amountTextView.setTextColor(ContextCompat.getColor(RApplication.getAppContext(),
                                                                      R.color.primary_text_color));
            holder.mainView.setBackgroundResource(R.color.white);
        }

        holder.nameTextView.setText(ingredient.getName());

        String amount = ingredient.getMainMeasureUnit().toValueString(ingredient.getMainAmount());

        if (!amount.isEmpty()) {
            holder.amountTextView.setVisibility(View.VISIBLE);

            holder.amountTextView.setText(amount);
        } else {
            holder.amountTextView.setVisibility(View.GONE);
        }

        if (ingredient.getCategory() != null) {
            holder.categoryView.setBackgroundResource(ingredient.getCategory().getColorId());
        }

        holder.mainView.setTag(position);
        holder.mainView.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            Ingredient selectIngredient = ingredients.get(pos);
            if (ingredient.getShopListStatus() == ShopListStatus.NEED_TO_BUY) {
                ingredient.setShopListStatus(ShopListStatus.NONE);
            } else {
                ingredient.setShopListStatus(ShopListStatus.NEED_TO_BUY);
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void updateItems(boolean isChecked) {
        for (Ingredient ingredient : ingredients) {
            if (isChecked) {
                ingredient.setShopListStatus(ShopListStatus.NEED_TO_BUY);
            } else {
                ingredient.setShopListStatus(ShopListStatus.NONE);
            }
        }
        notifyDataSetChanged();
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
}
