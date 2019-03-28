package com.cookplan.recipe.import_recipe.search_url

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookplan.R
import com.cookplan.models.network.GoogleRecipe
import com.cookplan.recipe.import_recipe.search_url.GoogleRecipeListRecyclerAdapter.GoogleRecipeViewHolder


class GoogleRecipeListRecyclerAdapter(private val values: MutableList<GoogleRecipe>,
                                      private val loadNextDatalistener: (Int) -> Unit,
                                      private val openUrlListener: (String) -> Unit) : RecyclerView.Adapter<GoogleRecipeViewHolder>() {
    private var needToLoadNextPart: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoogleRecipeViewHolder =
            GoogleRecipeViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.google_recipe_list_item_layout, parent, false))

    override fun onBindViewHolder(holderGoogleRecipe: GoogleRecipeViewHolder, position: Int) {
        val item = values[position]
        holderGoogleRecipe.nameTextView.text = item.title
        holderGoogleRecipe.descTextView.text = item.desc
        holderGoogleRecipe.urlTextView.text = item.formattedUrl
        if (item.imageUrl != null) {
            Glide.with(holderGoogleRecipe.imageView.context)
                    .load(item.imageUrl)
//                    .placeholder(R.drawable.ic_default_recipe_image)
//                    .centerCrop()
                    .into(holderGoogleRecipe.imageView)
        }

        with(holderGoogleRecipe.mainView) {
            tag = item
            setOnClickListener { if (item.url != null) openUrlListener(item.url) }
        }

        if (position > values.size * 2 / 3 && needToLoadNextPart) {
            needToLoadNextPart = false
            loadNextDatalistener(values.size)
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    fun addItems(googleRecipes: List<GoogleRecipe>) {
        values.addAll(googleRecipes)
        notifyDataSetChanged()
        needToLoadNextPart = googleRecipes.size != 0
    }

    fun clearItems() {
        values.clear()
        notifyDataSetChanged()
    }

    inner class GoogleRecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainView: View = view.findViewById(R.id.card_view)
        val nameTextView: TextView = view.findViewById<TextView>(R.id.name_textview)
        val descTextView: TextView = view.findViewById<TextView>(R.id.desc_textview)
        val urlTextView: TextView = view.findViewById<TextView>(R.id.link_textview)
        val imageView: ImageView = view.findViewById<ImageView>(R.id.recipe_image)
    }
}
