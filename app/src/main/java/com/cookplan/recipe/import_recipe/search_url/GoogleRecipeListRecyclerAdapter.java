package com.cookplan.recipe.import_recipe.search_url;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cookplan.R;
import com.cookplan.models.network.GoogleRecipe;

import java.util.List;


public class GoogleRecipeListRecyclerAdapter extends RecyclerView.Adapter<GoogleRecipeListRecyclerAdapter.ViewHolder> {
    private List<GoogleRecipe> values;
    private GoogleRecipeListEventListener listener;
    private Context context;
    private boolean needToLoadNextPart;

    public GoogleRecipeListRecyclerAdapter(List<GoogleRecipe> items,
                                           GoogleRecipeListEventListener listener, Context context) {
        this.values = items;
        this.listener = listener;
        this.context = context;
        needToLoadNextPart = true;
    }

    @Override
    public GoogleRecipeListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.google_recipe_list_item_layout, parent, false);
        return new GoogleRecipeListRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GoogleRecipeListRecyclerAdapter.ViewHolder holder, int position) {
        GoogleRecipe item = values.get(position);
        holder.nameTextView.setText(item.getTitle());
        holder.descTextView.setText(item.getDesc());
        holder.urlTextView.setText(item.getFormattedUrl());
        if (item.getImageUrl() != null) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_default_recipe_image)
                    .centerCrop()
                    .into(holder.imageView);
        }

        holder.mainView.setTag(item);
        holder.mainView.setOnClickListener(v -> {
            GoogleRecipe localRecipe = (GoogleRecipe) v.getTag();
            if (listener != null && localRecipe != null) {
                listener.onItemClick(localRecipe.getUrl());
            }
        });
        if (position > values.size() * 2 / 3 && listener != null && needToLoadNextPart) {
            needToLoadNextPart = false;
            listener.onLoadNextData(values.size());
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void addItems(List<GoogleRecipe> googleRecipes) {
        values.addAll(googleRecipes);
        notifyDataSetChanged();
        if (googleRecipes.size() == 0) {
            needToLoadNextPart = false;
        } else {
            needToLoadNextPart = true;
        }
    }

    public void clearItems() {
        values.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        public final TextView nameTextView;
        public final TextView descTextView;
        public final TextView urlTextView;
        public final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.card_view);
            nameTextView = (TextView) view.findViewById(R.id.name_textview);
            descTextView = (TextView) view.findViewById(R.id.desc_textview);
            urlTextView = (TextView) view.findViewById(R.id.link_textview);
            imageView = (ImageView) view.findViewById(R.id.recipe_image);
        }
    }

    public interface GoogleRecipeListEventListener {
        void onItemClick(String url);

        void onLoadNextData(int offset);
    }
}
