package com.cookplan.recipe.edit.description.image

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cookplan.R
import com.cookplan.utils.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage

/**
 * Created by DariaEfimova on 13.04.17.
 */

class RecipeDescImagesPagerAdapter(private val imageIds: MutableList<String> = mutableListOf(),
                                   private val removelistener: ((String) -> Unit)? = null) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(collection.context)
        val layout = inflater.inflate(R.layout.desc_image_item_layout, collection, false) as ViewGroup
        val imageView = layout.findViewById<ImageView>(R.id.image_view)

        val imageId = imageIds[position]
        val imageRef = FirebaseStorage.getInstance().getReference(imageId)
        Glide.with(collection.context)
                .using(FirebaseImageLoader())
                .load(imageRef)
                .centerCrop()
                .into(imageView)

        val deleteImageView = layout.findViewById<ImageView>(R.id.delete_image_view)
        with(deleteImageView) {
            tag = imageId
            setOnClickListener { removelistener?.invoke(imageId) }
        }
        deleteImageView.visibility = if (removelistener == null) GONE else VISIBLE

        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return imageIds.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun updateImages(newImageIds: MutableList<String>) {
        imageIds.clear()
        imageIds.addAll(newImageIds)
        notifyDataSetChanged()
    }
}