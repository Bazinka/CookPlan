package com.cookplan.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cookplan.R;

import java.util.List;

/**
 * Created by DariaEfimova on 13.06.17.
 */

public class ImageViewPagerAdapter extends PagerAdapter {

    private List<String> imageUrls;
    private Context context;

    public ImageViewPagerAdapter(List<String> urls, Context context) {
        this.imageUrls = urls;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        String url = imageUrls.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.image_pagerview_item_layout, collection, false);

        ImageView imageView = (ImageView) layout.findViewById(R.id.recipe_image);
        Glide.with(context)
                .load(url)
                .into(imageView);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}