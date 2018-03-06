package com.cookplan.images

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cookplan.R
import com.cookplan.utils.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage
import java.util.*

/**
 * Created by DariaEfimova on 13.04.17.
 */

class RecipeDescImagesPagerAdapter(private val imageIds: MutableList<String> = mutableListOf()) : PagerAdapter() {

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
        val numberTextView = layout.findViewById<TextView>(R.id.number_image_textview)
        numberTextView.text = (position + 1).toString() + " " +
                numberTextView.context.getString(R.string.from_text) + " " +
                count.toString()

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

    fun removeImage(imageId: String) {
        imageIds.remove(imageId)
        notifyDataSetChanged()
    }

    fun getArrayListItems(): ArrayList<String>? {
        return ArrayList(imageIds)
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}