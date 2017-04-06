package com.cookplan.recipe_new.add_ingredients;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Ingredient;

import java.util.List;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class EditRecipeInrgedientsAdapter extends RecyclerView.Adapter<EditRecipeInrgedientsAdapter.MainViewHolder> {

    private List<Ingredient> ingredients;
    private int removeShownPrevPosition;
    private int removeShownPosition;
    private IngredientsEventListener listener;
    private Context context;

    public EditRecipeInrgedientsAdapter(Context context, List<Ingredient> ingredientsList, IngredientsEventListener listener) {
        this.listener = listener;
        this.context = context;
        ingredients = ingredientsList;
        removeShownPosition = -1;
        removeShownPrevPosition = -1;
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

        if (ingredient.getMainAmount() != null && ingredient.getMainMeasureUnit() != null
                && ingredient.getMainAmount() > 1e-8) {
            holder.amountTextView.setVisibility(View.VISIBLE);
            holder.amountTextView.setText(ingredient.getMainMeasureUnit().toValueString(ingredient.getMainAmount()));
        } else {
            holder.amountTextView.setVisibility(View.GONE);
        }

        if (ingredient.getCategory() != null) {
            holder.removeItemLayout.setBackgroundResource(ingredient.getCategory().getColorId());
            holder.categoryView.setBackgroundResource(ingredient.getCategory().getColorId());
        }

        if (removeShownPosition == position) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.left_to_right_view);
            holder.removeItemLayout.clearAnimation();
            holder.removeItemLayout.startAnimation(animation);
            holder.contentItemLayout.clearAnimation();
            holder.contentItemLayout.startAnimation(animation);
            holder.removeItemLayout.setVisibility(View.VISIBLE);
        } else {
            if (removeShownPosition == -1 && removeShownPrevPosition == position) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.right_to_left_view);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.removeItemLayout.clearAnimation();
                        holder.contentItemLayout.clearAnimation();
                        holder.removeItemLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                holder.removeItemLayout.startAnimation(animation);
                holder.contentItemLayout.startAnimation(animation);
            } else {
                holder.removeItemLayout.setVisibility(View.GONE);
            }
        }


        holder.removeItemLayout.setTag(ingredient);
        holder.removeItemLayout.setOnClickListener(v -> {
            Ingredient ingred = (Ingredient) v.getTag();
            removeShownPrevPosition = -1;
            removeShownPosition = -1;
            if (listener != null && ingred != null) {
                listener.onRemoveIngredientEvent(ingred);
            }
        });

        holder.mainView.setTag(position);
        holder.mainView.setOnClickListener(v -> {
            Integer pos = (Integer) v.getTag();
            if (pos != null) {
                removeShownPrevPosition = removeShownPosition;
                if (removeShownPosition == pos) {
                    removeShownPosition = -1;
                } else {
                    removeShownPosition = pos;
                }
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView amountTextView;
        ViewGroup removeItemLayout;
        View categoryView;
        ViewGroup contentItemLayout;
        View mainView;

        MainViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.ingredient_item_name);
            amountTextView = (TextView) v.findViewById(R.id.ingredient_item_amount);
            removeItemLayout = (ViewGroup) v.findViewById(R.id.ingredient_remove_layout);
            contentItemLayout = (ViewGroup) v.findViewById(R.id.ingredient_content_layout);
            categoryView = v.findViewById(R.id.category_view);
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
        removeShownPosition = -1;
        removeShownPrevPosition = -1;
        notifyDataSetChanged();
    }

    public interface IngredientsEventListener {

        public void onRemoveIngredientEvent(Ingredient ingredient);
    }
}