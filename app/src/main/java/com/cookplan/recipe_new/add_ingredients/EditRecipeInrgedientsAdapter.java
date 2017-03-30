package com.cookplan.recipe_new.add_ingredients;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;

import java.util.List;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class EditRecipeInrgedientsAdapter extends RecyclerView.Adapter<EditRecipeInrgedientsAdapter.MainViewHolder> {


    private List<Ingredient> ingredients;

    public EditRecipeInrgedientsAdapter(List<Ingredient> myDataset) {
        ingredients = myDataset;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_ingredients_item_layout, parent, false);

        MainViewHolder vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.nameTextView.setText(ingredient.getName());

        if (ingredient.getAmount() != null && ingredient.getMeasureUnit() != null
                && ingredient.getAmount() > 1e-8) {
            holder.amountTextView.setVisibility(View.VISIBLE);
            holder.amountTextView.setText(ingredient.getMeasureUnit().toValueString(ingredient.getAmount()));
        } else {
            holder.amountTextView.setVisibility(View.GONE);
        }

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

    public void addItem(Ingredient ingredient) {
        ingredients.add(ingredient);
        notifyDataSetChanged();
    }

//    public void addItemList(List<Ingredient> ingredientList) {
//        ingredients.addAll(ingredientList);
//        notifyDataSetChanged();
//    }

    public void updateItems(List<Ingredient> ingredientList) {
        ingredients.clear();
        ingredients.addAll(ingredientList);
        notifyDataSetChanged();
    }

    public interface IngredientsClickListener {

        public void onIngredientItemClick(Ingredient ingredient);
    }
}