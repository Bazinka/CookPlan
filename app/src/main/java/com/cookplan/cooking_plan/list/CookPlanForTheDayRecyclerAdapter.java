package com.cookplan.cooking_plan.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cookplan.R;
import com.cookplan.cooking_plan.list.CookPlanMainRecyclerAdapter.CookPlanClickListener;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.utils.Constants.TypeOfTime;
import com.cookplan.utils.FirebaseImageLoader;
import com.cookplan.utils.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.cookplan.utils.Constants.ObjectType.INGREDIENT;
import static com.cookplan.utils.Constants.ObjectType.RECIPE;

public class CookPlanForTheDayRecyclerAdapter extends RecyclerView.Adapter<CookPlanForTheDayRecyclerAdapter.BaseViewHolder> {

    private final List<Object> values;
    private final LocalDate valueDate;
    private CookPlanClickListener listener;
    private Context context;

    public CookPlanForTheDayRecyclerAdapter(List<Object> items, LocalDate valueDate, CookPlanClickListener listener, Context context) {
        values = new ArrayList<>(items);
        this.valueDate = valueDate;
        sortValue();
        this.listener = listener;
        this.context = context;
    }

    private void sortValue() {
        Collections.sort(values, (object1, object2) -> {
            DateTime date1 = null;
            DateTime date2 = null;
            if (object1 instanceof Recipe) {
                Recipe recipe = (Recipe) object1;
                date1 = new DateTime(getRecipeMillisec(recipe));
            } else if (object1 instanceof Ingredient) {
                Ingredient ingredient = (Ingredient) object1;
                date1 = new DateTime(ingredient.getCookingDate());
            }
            if (object2 instanceof Recipe) {
                Recipe recipe = (Recipe) object2;
                date2 = new DateTime(getRecipeMillisec(recipe));
            } else if (object2 instanceof Ingredient) {
                Ingredient ingredient = (Ingredient) object2;
                date2 = new DateTime(ingredient.getCookingDate());
            }
            if (date1 != null && date2 != null) {
                if (date1.equals(date2)) {
                    return 0;
                } else if (date1.isAfter(date2)) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 0;
        });
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
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    recipeViewHolder.recipeImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    recipeViewHolder.recipeImageView.setImageBitmap(resource);
                                }
                            });
                } else {
                    StorageReference imageRef = FirebaseStorage.getInstance().getReference(recipe.getImageUrls().get(0));
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(imageRef)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    recipeViewHolder.recipeImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    recipeViewHolder.recipeImageView.setImageBitmap(resource);
                                }
                            });
                }
            } else {
                recipeViewHolder.recipeImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                recipeViewHolder.recipeImageView.setImageResource(R.drawable.ic_default_recipe_image);
            }
            setTimeField(recipeViewHolder.recipeTimeView, getRecipeMillisec(recipe));
            recipeViewHolder.recipeNameView.setText(recipe.getName());
        }

        if (getItemViewType(position) == INGREDIENT.getId()) {
            Ingredient ingredient = (Ingredient) object;
            IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
            if (ingredient.getCategory() != null) {
                ingredientViewHolder.cardView.setBackgroundResource(ingredient.getCategory().getColorId());
            }

            ingredientViewHolder.nameTextView.setText(ingredient.getName());


            if (ingredient.getMainAmount() != 0 && ingredient.getMainMeasureUnit() != null) {
                ingredientViewHolder.amountTextView.setVisibility(View.VISIBLE);
                String amount = ingredient.getMainMeasureUnit().toValueString(ingredient.getMainAmount());
                ingredientViewHolder.amountTextView.setText(amount);
            } else {
                ingredientViewHolder.amountTextView.setVisibility(View.GONE);
            }
            setTimeField(ingredientViewHolder.timeTextView, ingredient.getCookingDate());
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
                    listener.onRecipeLongClick(valueDate, (Recipe) tagObject);
                }
                if (tagObject instanceof Ingredient) {
                    listener.onIngredientLongClick((Ingredient) tagObject);
                }
            }
            return true;
        });
    }

    private long getRecipeMillisec(Recipe recipe) {
        for (Long millisec : recipe.getCookingDate()) {
            LocalDate date = new LocalDate(millisec);
            if (date.equals(valueDate)) {
                return millisec;
            }
        }
        return -1;
    }

    private void setTimeField(TextView recipeTimeView, long millisec) {
        DateTime date = new DateTime(millisec);
        TypeOfTime typeOfTime = TypeOfTime.getTypeOfTimeByHours(date.getHourOfDay());
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
