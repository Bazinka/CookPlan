package com.cookplan.cooking_plan.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cookplan.R;
import com.cookplan.cooking_plan.list.CookPlanMainRecyclerAdapter.CookPlanClickListener;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.utils.Constants.TypeOfTime;
import com.cookplan.utils.FirebaseImageLoader;
import com.cookplan.utils.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.cookplan.utils.Constants.ObjectType.INGREDIENT;
import static com.cookplan.utils.Constants.ObjectType.RECIPE;

public class CookPlanForTheDayRecyclerAdapter extends RecyclerView.Adapter<CookPlanForTheDayRecyclerAdapter.BaseViewHolder> {

    private final List<Object> values;
    private CookPlanClickListener listener;
    private Context context;

    public CookPlanForTheDayRecyclerAdapter(List<Object> items, CookPlanClickListener listener, Context context) {
        values = new ArrayList<>(items);
        this.listener = listener;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = values.get(position);
        if (object instanceof Recipe) {
            return RECIPE.getId();
        } else if (object instanceof Ingredient) {
            return INGREDIENT.getId();
        } else {
            return -1;
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RECIPE.getId()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cook_plan_recipe_item_layout, parent, false);
            return new RecipeViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cook_plan_ingredient_item_layout, parent, false);
            return new IngredientViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        Object object = values.get(position);
        if (getItemViewType(position) == RECIPE.getId()) {
            Recipe recipe = (Recipe) object;

            RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;

            if (recipe.getImageUrls() != null && !recipe.getImageUrls().isEmpty()) {
                if (Utils.isStringUrl(recipe.getImageUrls().get(0))) {
                    Glide.with(context)
                            .load(recipe.getImageUrls().get(0))
                            .placeholder(R.drawable.ic_default_recipe_image)
                            .centerCrop()
                            .into(recipeViewHolder.recipeImageView);
                } else {
                    StorageReference imageRef = FirebaseStorage.getInstance().getReference(recipe.getImageUrls().get(0));
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(imageRef)
                            .placeholder(R.drawable.ic_default_recipe_image)
                            .centerCrop()
                            .crossFade()
                            .into(recipeViewHolder.recipeImageView);
                }
            } else {
                recipeViewHolder.recipeImageView.setImageResource(R.drawable.ic_default_recipe_image);
            }
            recipeViewHolder.recipeNameView.setText(recipe.getName());
            setTimeTiField(recipeViewHolder.recipeTimeView, recipe.getCookingDate());
        }

        if (getItemViewType(position) == INGREDIENT.getId()) {
            Ingredient ingredient = (Ingredient) object;
            IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
            if (ingredient.getCategory() != null) {
                ingredientViewHolder.cardView.setBackgroundResource(ingredient.getCategory().getColorId());
            }

            ingredientViewHolder.nameTextView.setText(ingredient.getName());


            if (ingredient.getAmountString() != null) {
                ingredientViewHolder.amountTextView.setVisibility(View.VISIBLE);

                ingredientViewHolder.amountTextView.setText(ingredient.getAmountString());
            } else {
                //                ingredientViewHolder.amountTextView.setVisibility(View.GONE);
            }
            setTimeTiField(ingredientViewHolder.timeTextView, ingredient.getCookingDate());
        }

        holder.mainView.setTag(object);
        holder.mainView.setOnClickListener(view -> {
            Object tagObject = view.getTag();
            if (tagObject != null && listener != null) {
                if (tagObject instanceof Recipe) {
                    listener.onRecipeClick((Recipe) tagObject);
                }
            }
        });

        holder.mainView.setOnLongClickListener(view -> {
            Object tagObject = view.getTag();
            if (tagObject != null && listener != null) {
                if (tagObject instanceof Recipe) {
                    listener.onRecipeLongClick((Recipe) tagObject);
                }
                if (tagObject instanceof Ingredient) {
                    listener.onIngredientLongClick((Ingredient) tagObject);
                }
            }
            return true;
        });
    }

    private void setTimeTiField(TextView recipeTimeView, long millisec) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(millisec);
        TypeOfTime typeOfTime = TypeOfTime.getTypeOfTimeByHours(date.get(Calendar.HOUR_OF_DAY));
        if (typeOfTime != null) {
            recipeTimeView.setText(typeOfTime.toString());
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    private class IngredientViewHolder extends BaseViewHolder {
        final TextView nameTextView;
        final TextView amountTextView;
        final TextView timeTextView;
        final View cardView;

        IngredientViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.cook_plan_ingred_name);
            amountTextView = (TextView) v.findViewById(R.id.cook_plan_ingred_amount);
            timeTextView = (TextView) v.findViewById(R.id.cook_plan_ingred_time);
            cardView = v.findViewById(R.id.cook_plan_ingred_card_view);
        }
    }

    private class RecipeViewHolder extends BaseViewHolder {
        final TextView recipeNameView;
        final TextView recipeTimeView;
        final ImageView recipeImageView;

        RecipeViewHolder(View view) {
            super(view);
            recipeNameView = (TextView) view.findViewById(R.id.cook_plan_recipe_name);
            recipeTimeView = (TextView) view.findViewById(R.id.cook_plan_recipe_time);
            recipeImageView = (ImageView) view.findViewById(R.id.cook_plan_recipe_image);

        }
    }

    protected class BaseViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;

        BaseViewHolder(View v) {
            super(v);
            mainView = v.findViewById(R.id.main_view);
        }
    }
}
