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

public class IngredientListRecyclerAdapter extends RecyclerView.Adapter<IngredientListRecyclerAdapter.ViewHolder> {
    private List<Ingredient> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public View mainView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.ingredient_item_name);
            mainView = v;
        }
    }

    public IngredientListRecyclerAdapter(List<Ingredient> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public IngredientListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ingredient ingredient = mDataset.get(position);
//        holder.textView.setText();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}