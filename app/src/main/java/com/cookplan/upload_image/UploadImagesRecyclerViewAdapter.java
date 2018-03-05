package com.cookplan.upload_image;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cookplan.R;
import com.cookplan.utils.FirebaseImageLoader;
import com.cookplan.utils.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UploadImagesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> values;
    private UploadImagesEventListener listener;
    private Context context;

    public UploadImagesRecyclerViewAdapter(List<String> items, UploadImagesEventListener listener, Context context) {
        values = items != null ? items : new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.upload_add_image_item_layout, parent, false);
            return new AddImageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.desc_image_item_layout, parent, false);
            return new UploadImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        //        holder.mainView.setTag(recipe);
        //        holder.mainView.setOnClickListener(v -> {
        //            Recipe recipe1 = (Recipe) v.getTag();
        //            if (listener != null && recipe1 != null) {
        //                listener.onRecipeClick(recipe1);
        //            }
        //        });

        if (getItemViewType(position) == 0) {
            AddImageViewHolder addImageHolder = (AddImageViewHolder) holder;
            addImageHolder.mainView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddImageEvent();
                }
            });
        } else {
            String imageId = values.get(position - 1);
            UploadImageViewHolder uploadImageHolder = (UploadImageViewHolder) holder;

            if (Utils.INSTANCE.isStringUrl(imageId)) {
                Glide.with(context)
                        .load(imageId)
                        .into(uploadImageHolder.imageView);
            } else {
                StorageReference imageRef = FirebaseStorage.getInstance().getReference(imageId);
                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(imageRef)
                        .centerCrop()
                        .into(uploadImageHolder.imageView);
            }

            uploadImageHolder.deleteImageView.setTag(imageId);
            uploadImageHolder.deleteImageView.setOnClickListener(view -> {
                String deleteUrl = (String) view.getTag();
                if (listener != null && deleteUrl != null) {
                    listener.onDeleteImage(imageId);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return values.size() + 1;
    }

    public void removeImage(String imageId) {
        if (values != null) {
            values.remove(imageId);
            notifyDataSetChanged();
        }
    }

    public List<String> getValues() {
        return values;
    }

    public void addImage(String imageId) {
        if (values != null) {
            values.add(imageId);
            notifyDataSetChanged();
        }
    }

    public class AddImageViewHolder extends RecyclerView.ViewHolder {
        public final ViewGroup mainView;

        public AddImageViewHolder(View view) {
            super(view);
            mainView = (ViewGroup) view.findViewById(R.id.card_view);
        }
    }

    public class UploadImageViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        public final ImageView imageView;
        public final ImageView deleteImageView;

        public UploadImageViewHolder(View view) {
            super(view);
            mainView = view;
            imageView = (ImageView) view.findViewById(R.id.image_view);
            deleteImageView = (ImageView) view.findViewById(R.id.delete_image_view);

        }
    }

    public interface UploadImagesEventListener {
        void onAddImageEvent();

        void onDeleteImage(String url);
    }
}
