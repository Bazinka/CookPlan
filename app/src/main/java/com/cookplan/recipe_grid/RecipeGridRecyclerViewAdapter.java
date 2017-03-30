package com.cookplan.recipe_grid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Recipe;

import java.util.List;

public class RecipeGridRecyclerViewAdapter extends RecyclerView.Adapter<RecipeGridRecyclerViewAdapter.ViewHolder> {

    private final List<Recipe> mValues;
    private RecipeListClickListener listener;

    public RecipeGridRecyclerViewAdapter(List<Recipe> items, RecipeListClickListener listener) {
        mValues = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Recipe recipe = mValues.get(position);
        holder.nameView.setText(mValues.get(position).getName());

        holder.mainView.setTag(recipe);
        holder.mainView.setOnClickListener(v -> {
            Recipe recipe1 = (Recipe) v.getTag();
            if (listener != null && recipe1 != null) {
                listener.onRecipeClick(recipe1);
            }
        });
        holder.mainView.setOnLongClickListener(v -> {
            Recipe recipe1 = (Recipe) v.getTag();
            if (listener != null && recipe1 != null) {
                listener.onRecipeLongClick(recipe1);
            }
            return true;
        });
    }

    public void updateItems(List<Recipe> recipeList) {
        mValues.clear();
        mValues.addAll(recipeList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        public final TextView nameView;

        public ViewHolder(View view) {
            super(view);
            mainView = view;
            nameView = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }

    public interface RecipeListClickListener {
        public void onRecipeClick(Recipe recipe);

        public void onRecipeLongClick(Recipe recipe);
    }
}
