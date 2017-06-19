package com.cookplan.recipe.steps_mode;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class RecipeStepsInrgedientsAdapter extends RecyclerView.Adapter<RecipeStepsInrgedientsAdapter.MainViewHolder> {

    private List<Ingredient> ingredients;
    private List<Ingredient> selectedIngredients;

    public RecipeStepsInrgedientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        selectedIngredients = new ArrayList<>();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_ingredients_item_layout, parent, false);

        MainViewHolder vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.nameTextView.setText(ingredient.getName());

        if (ingredient.getMainAmount() != null && ingredient.getMainMeasureUnit() != null) {
            holder.amountTextView.setVisibility(View.VISIBLE);

            holder.amountTextView.setText(ingredient.getMainMeasureUnit().toValueString(ingredient.getMainAmount()));

        } else {
            holder.amountTextView.setVisibility(View.GONE);
        }

        if (selectedIngredients.contains(ingredient)) {
            holder.mainView.setBackgroundResource(R.color.accent_color_light);
        } else {
            holder.mainView.setBackgroundResource(R.color.white);
        }

        View.OnClickListener clickListener = view -> {
            Ingredient selectIngredient = (Ingredient) view.getTag();
            if (selectedIngredients.contains(selectIngredient)) {
                selectedIngredients.remove(selectIngredient);
            } else {
                selectedIngredients.add(selectIngredient);
            }
            notifyDataSetChanged();
        };
        holder.mainView.setTag(ingredient);
        holder.mainView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView amountTextView;
        public View mainView;

        public MainViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.ingredient_item_name);
            amountTextView = (TextView) v.findViewById(R.id.ingredient_item_amount);
            mainView = v.findViewById(R.id.main_view);
        }
    }
}