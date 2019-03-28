package com.cookplan.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

import com.bumptech.glide.Glide
import com.cookplan.R
import com.cookplan.utils.FirebaseImageLoader
import com.cookplan.utils.Utils
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/**
 * Created by DariaEfimova on 13.06.17.
 */

class ImageViewPagerAdapter(private val imageUrls: List<String>, private val context: Context) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val url = imageUrls[position]
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.image_pagerview_item_layout, collection, false) as ViewGroup

        val imageView = layout.findViewById<View>(R.id.recipe_image) as ImageView
        if (Utils.isStringUrl(url)) {
            Glide.with(context)
                    .load(url)
                    .into(imageView)
        } else {
            val imageRef = FirebaseStorage.getInstance().getReference(url)
            Glide.with(context)
//                    .using(FirebaseImageLoader())
                    .load(imageRef)
//                    .centerCrop()
                    .into(imageView)
        }
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}