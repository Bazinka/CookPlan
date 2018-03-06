package com.cookplan.recipe.edit.description

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cookplan.R
import com.cookplan.utils.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage

class DescImagesRecyclerViewAdapter(private val images: MutableList<String> = mutableListOf(),
                                    private val clicklistener: ((String) -> Unit)) : RecyclerView.Adapter<DescImagesRecyclerViewAdapter.MainViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MainViewHolder =
            MainViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.desc_list_image_item_layout, parent, false))


    override fun onBindViewHolder(uploadImageHolder: MainViewHolder, position: Int) {

        val imageId = images[position]

        val imageRef = FirebaseStorage.getInstance().getReference(imageId)
        Glide.with(uploadImageHolder.context)
                .using(FirebaseImageLoader())
                .load(imageRef)
                .centerCrop()
                .into(uploadImageHolder.imageView)
        with(uploadImageHolder.imageView) {
            setOnClickListener { clicklistener(imageId) }
        }
    }

    override fun getItemCount() = images.size

    class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var imageView: ImageView = v.findViewById<ImageView>(R.id.image_view)
        var context: Context = v.context
    }

    fun addItem(imageId: String) {
        images.add(imageId)
        notifyDataSetChanged()
    }


    fun updateItems(imageIdList: List<String>) {
        images.clear()
        images.addAll(imageIdList)
        notifyDataSetChanged()
    }

    fun removeItem(imageId: String) {
        images.remove(imageId)
        notifyDataSetChanged()
    }

    fun getItems(): List<String> {
        return images
    }
}
