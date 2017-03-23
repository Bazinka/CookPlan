package com.cookplan.recipe_view;

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

public class RecipeViewInrgedientsAdapter extends RecyclerView.Adapter<RecipeViewInrgedientsAdapter.MainViewHolder> {


    private List<Ingredient> mDataset;

    public RecipeViewInrgedientsAdapter(List<Ingredient> myDataset) {
        mDataset = myDataset;
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
        Ingredient ingredient = mDataset.get(position);
        holder.nameTextView.setText(ingredient.getName());

        if (ingredient.getAmount() != null) {
            holder.amountTextView.setVisibility(View.VISIBLE);
            holder.amountTextView.setText(ingredient.getAmount().toString());

            holder.measureTextView.setVisibility(View.VISIBLE);
            holder.measureTextView.setText(ingredient.getMeasureUnit().toString());

        } else {
            holder.amountTextView.setVisibility(View.GONE);
            holder.measureTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView measureTextView;
        public TextView amountTextView;
        public View mainView;

        public MainViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.ingredient_item_name);
            measureTextView = (TextView) v.findViewById(R.id.ingredient_item_measure);
            amountTextView = (TextView) v.findViewById(R.id.ingredient_item_amount);
            mainView = v;
        }
    }

    public void updateItems(List<Ingredient> ingredientList) {
        mDataset.clear();
        mDataset.addAll(ingredientList);
        notifyDataSetChanged();
    }

    public interface IngredientsClickListener {

        public void onIngredientItemClick(Ingredient ingredient);
    }
}