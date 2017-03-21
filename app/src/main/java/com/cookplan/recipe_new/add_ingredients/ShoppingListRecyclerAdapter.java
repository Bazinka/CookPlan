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

public class ShoppingListRecyclerAdapter extends RecyclerView.Adapter<ShoppingListRecyclerAdapter.MainViewHolder> {


    private List<Ingredient> mDataset;

    public ShoppingListRecyclerAdapter(List<Ingredient> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item_layout, parent, false);

        MainViewHolder vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Ingredient ingredient = mDataset.get(position);
        holder.textView.setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public View mainView;

        public MainViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.ingredient_item_name);
            mainView = v;
        }
    }

    public void addItem(Ingredient ingredient) {
        mDataset.add(ingredient);
        notifyDataSetChanged();
    }

//    public void addItemList(List<Ingredient> ingredientList) {
//        mDataset.addAll(ingredientList);
//        notifyDataSetChanged();
//    }

    public void updateItems(List<Ingredient> ingredientList) {
        mDataset.clear();
        mDataset.addAll(ingredientList);
        notifyDataSetChanged();
    }

    public interface IngredientsClickListener {

        public void onIngredientItemClick(Ingredient ingredient);
    }
}