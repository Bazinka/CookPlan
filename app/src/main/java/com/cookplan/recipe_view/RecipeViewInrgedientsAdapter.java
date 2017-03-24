package com.cookplan.recipe_view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.ShopListStatus;

import java.util.List;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class RecipeViewInrgedientsAdapter extends RecyclerView.Adapter<RecipeViewInrgedientsAdapter.MainViewHolder> {

    private List<Ingredient> ingredients;
    private IngredientsClickListener listener;

    public RecipeViewInrgedientsAdapter(List<Ingredient> ingredients, IngredientsClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_view_ingredients_item_layout, parent, false);

        MainViewHolder vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
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

        if (ingredient.getShopListStatus() == ShopListStatus.NEED_TO_BUY) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        View.OnClickListener clickListener = view -> {
            int pos = (int) view.getTag();
            Ingredient selectIngredient = ingredients.get(pos);
            boolean isIngredientSelect = false;
            if (selectIngredient.getShopListStatus() == ShopListStatus.NEED_TO_BUY) {
                isIngredientSelect = false;
            } else {
                isIngredientSelect = true;
            }
            selectIngredient.setShopListStatus(isIngredientSelect? ShopListStatus.NEED_TO_BUY : ShopListStatus.NONE);

            if (listener != null) {
                listener.onIngredientItemSelected(selectIngredient);
            }
        };
        holder.mainView.setTag(position);
        holder.checkBox.setTag(position);
        holder.mainView.setOnClickListener(clickListener);
        holder.checkBox.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView measureTextView;
        public TextView amountTextView;
        public CheckBox checkBox;
        public View mainView;

        public MainViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.ingredient_item_name);
            measureTextView = (TextView) v.findViewById(R.id.ingredient_item_measure);
            amountTextView = (TextView) v.findViewById(R.id.ingredient_item_amount);
            checkBox = (CheckBox) v.findViewById(R.id.add_to_shop_list_checkbox);
            mainView = v.findViewById(R.id.main_view);
        }
    }

    public void updateItems(List<Ingredient> ingredientList) {
        ingredients.clear();
        ingredients.addAll(ingredientList);
        notifyDataSetChanged();
    }

    public interface IngredientsClickListener {

        public void onIngredientItemSelected(Ingredient ingredient);
    }
}