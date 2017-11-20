package com.cookplan.recipe.edit.add_ingredients;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class EditRecipeInrgedientsAdapter extends RecyclerView.Adapter<EditRecipeInrgedientsAdapter.MainViewHolder> {

    private List<Ingredient> ingredients;
    private IngredientsEventListener listener;
    private Context context;

    public EditRecipeInrgedientsAdapter(Context context, List<Ingredient> ingredientsList, IngredientsEventListener listener) {
        this.listener = listener;
        this.context = context;
        ingredients = ingredientsList;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_recipe_ingredients_item_layout, parent, false);

        MainViewHolder vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.nameTextView.setText(ingredient.getName());

        if (ingredient.getMainAmount() > 0 && ingredient.getMainMeasureUnit() != null
                && ingredient.getMainAmount() > 1e-8) {
            holder.amountTextView.setVisibility(View.VISIBLE);
            holder.amountTextView.setText(ingredient.getMainMeasureUnit().toValueString(ingredient.getMainAmount()));
        } else {
            holder.amountTextView.setVisibility(View.GONE);
        }

        if (ingredient.getCategory() != null) {
            holder.removeButtonLayout.setBackgroundResource(ingredient.getCategory().getColorId());
            holder.categoryView.setBackgroundResource(ingredient.getCategory().getColorId());
        }

        //set show mode.
        holder.mainView.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        holder.mainView.addDrag(SwipeLayout.DragEdge.Left, holder.mainRemoveLayout);
        holder.mainView.addDrag(SwipeLayout.DragEdge.Right, null);


        holder.mainRemoveLayout.setTag(ingredient);
        holder.mainRemoveLayout.setOnClickListener(v -> {
            Ingredient ingred = (Ingredient) v.getTag();
            if (listener != null && ingred != null) {
                listener.onRemoveIngredientEvent(ingred);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView amountTextView;
        ViewGroup mainRemoveLayout;
        ViewGroup removeButtonLayout;
        View categoryView;
        //        ViewGroup contentItemLayout;
        SwipeLayout mainView;

        MainViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.ingredient_item_name);
            amountTextView = (TextView) v.findViewById(R.id.ingredient_item_amount);
            mainRemoveLayout = (ViewGroup) v.findViewById(R.id.ingredient_remove_layout);
            removeButtonLayout = (ViewGroup) v.findViewById(R.id.ingredient_remove_button_layout);
            categoryView = v.findViewById(R.id.category_view);
            mainView = (SwipeLayout) v.findViewById(R.id.main_view);
        }
    }

    public void addItem(Ingredient ingredient) {
        ingredients.add(ingredient);
        notifyDataSetChanged();
    }


    public void updateItems(List<Ingredient> ingredientList) {
        ingredients.clear();
        ingredients.addAll(ingredientList);
        notifyDataSetChanged();
    }

    public interface IngredientsEventListener {

        public void onRemoveIngredientEvent(Ingredient ingredient);
    }
}