package com.cookplan.recipe.grid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cookplan.R;
import com.cookplan.models.Recipe;
import com.cookplan.utils.FirebaseImageLoader;
import com.cookplan.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RecipeGridRecyclerViewAdapter extends RecyclerView.Adapter<RecipeGridRecyclerViewAdapter.ViewHolder> {

    private final List<Recipe> mValues;
    private RecipeListClickListener listener;
    private Context context;

    public RecipeGridRecyclerViewAdapter(List<Recipe> items, RecipeListClickListener listener, Context context) {
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
            holder.authorNameView.setText(recipe.getUserName());
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
                        .placeholder(R.drawable.ic_default_recipe_image)
                        .centerCrop()
                        .into(holder.recipeImageView);
            } else {
                StorageReference imageRef = FirebaseStorage.getInstance().getReference(recipe.getImageUrls().get(0));
                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(imageRef)
                        .placeholder(R.drawable.ic_default_recipe_image)
                        .centerCrop()
                        .crossFade()
                        .into(holder.recipeImageView);
            }
        } else {
            holder.recipeImageView.setImageResource(R.drawable.ic_default_recipe_image);
        }
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
        public final TextView authorNameView;
        public final ViewGroup authorNameLayout;
        public final ImageView recipeImageView;

        public ViewHolder(View view) {
            super(view);
            mainView = view;
            nameView = (TextView) view.findViewById(R.id.name);
            authorNameView = (TextView) view.findViewById(R.id.author_name);
            authorNameLayout = (ViewGroup) view.findViewById(R.id.author_layout);
            recipeImageView = (ImageView) view.findViewById(R.id.recipe_image);

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
