package com.cookplan.recipe.list.main;

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
import com.cookplan.models.Recipe;
import com.cookplan.utils.FirebaseImageLoader;
import com.cookplan.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RecipeListRecyclerViewAdapter extends RecyclerView.Adapter<RecipeListRecyclerViewAdapter.ViewHolder> {

    private final List<Recipe> mValues;
    private RecipeListClickListener listener;
    private Context context;

    public RecipeListRecyclerViewAdapter(List<Recipe> items, RecipeListClickListener listener, Context context) {
        mValues = items;
        this.listener = listener;
        this.context = context;
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

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (recipe.getUserId().equals(uid)) {
            holder.authorNameLayout.setVisibility(View.GONE);
        } else {
            holder.authorNameView.setText(context.getString(R.string.recipe_from_title) + " " + recipe.getUserName());
            holder.authorNameLayout.setVisibility(View.VISIBLE);
        }

        holder.nameView.setText(mValues.get(position).getName());

        holder.mainView.setTag(recipe);
        holder.mainView.setOnClickListener(v -> {
            Recipe recipe1 = (Recipe) v.getTag();
            if (listener != null && recipe1 != null) {
                listener.onRecipeClick(recipe1);
            }
        });

        if (recipe.getImageUrls() != null && !recipe.getImageUrls().isEmpty()) {
            if (Utils.isStringUrl(recipe.getImageUrls().get(0))) {
                Glide.with(context)
                        .load(recipe.getImageUrls().get(0))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                holder.emptyRecipeImageView.setVisibility(View.GONE);
                                holder.recipeImageView.setVisibility(View.VISIBLE);
                                holder.recipeImageView.setImageBitmap(resource);
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
                                holder.emptyRecipeImageView.setVisibility(View.GONE);
                                holder.recipeImageView.setVisibility(View.VISIBLE);
                                holder.recipeImageView.setImageBitmap(resource);
                            }
                        });
            }
        } else {
            holder.emptyRecipeImageView.setVisibility(View.VISIBLE);
            holder.recipeImageView.setVisibility(View.GONE);
        }

        if (recipe.getCookingDate() != null && !recipe.getCookingDate().isEmpty()) {
            holder.addToCookplanImageView.setImageResource(R.drawable.ic_cooking_plan_accent);
        } else {
            holder.addToCookplanImageView.setImageResource(R.drawable.ic_cooking_plan_white);
        }
        holder.addToCookplanImageView.setTag(recipe);
        holder.addToCookplanImageView.setOnClickListener(view -> {
            Recipe localRecipe = (Recipe) view.getTag();
            if (listener != null && localRecipe != null) {
                listener.onAddToCookPlanClick(localRecipe);
            }
        });

        holder.mainView.setOnLongClickListener(v -> {
            Recipe localRecipe = (Recipe) v.getTag();
            if (listener != null && localRecipe != null) {
                listener.onRecipeLongClick(localRecipe);
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
        public final TextView authorNameView;
        public final ViewGroup authorNameLayout;
        public final ImageView recipeImageView;
        public final ImageView emptyRecipeImageView;
        public final ImageView addToCookplanImageView;

        public ViewHolder(View view) {
            super(view);
            mainView = view;
            nameView = (TextView) view.findViewById(R.id.recipe_name);
            authorNameView = (TextView) view.findViewById(R.id.author_name);
            authorNameLayout = (ViewGroup) view.findViewById(R.id.author_layout);
            recipeImageView = (ImageView) view.findViewById(R.id.recipe_item_image);
            emptyRecipeImageView = (ImageView) view.findViewById(R.id.empty_recipe_image);
            addToCookplanImageView = (ImageView) view.findViewById(R.id.add_to_cookplan_image_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }

    public interface RecipeListClickListener {
        void onRecipeClick(Recipe recipe);

        void onRecipeLongClick(Recipe recipe);

        void onAddToCookPlanClick(Recipe recipe);
    }
}
