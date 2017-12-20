package com.cookplan.recipe.grid

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cookplan.R
import com.cookplan.models.Recipe
import com.cookplan.recipe.grid.RecipeGridRecyclerViewAdapter.ViewHolder
import com.cookplan.utils.FirebaseImageLoader
import com.cookplan.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class RecipeGridRecyclerViewAdapter(private val clicklistener: (Recipe) -> Any,
                                    private val longClicklistener: (Recipe) -> Boolean,
                                    private val context: Context) : Adapter<ViewHolder>() {

    private val mValues: MutableList<Recipe> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.recipe_list_item_layout, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = mValues[position]

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val userName = holder.mainView.context.getString(R.string.my_recipe_title)

//        holder.authorNameLayout.visibility = if (recipe.userId == uid) View.GONE else View.VISIBLE
        holder.authorNameView.text = if (recipe.userId == uid) userName else recipe.userName

        holder.nameView.text = mValues[position].name

        if (!recipe.imageUrls.isEmpty()) {
            if (Utils.isStringUrl(recipe.imageUrls[0])) {
                Glide.with(context)
                        .load(recipe.imageUrls[0])
                        .placeholder(R.drawable.ic_default_recipe_image)
                        .centerCrop()
                        .into(holder.recipeImageView)
            } else {
                val imageRef = FirebaseStorage.getInstance().getReference(recipe.imageUrls[0])
                Glide.with(context)
                        .using(FirebaseImageLoader())
                        .load(imageRef)
                        .placeholder(R.drawable.ic_default_recipe_image)
                        .centerCrop()
                        .crossFade()
                        .into(holder.recipeImageView)
            }
        } else {
            holder.recipeImageView.setImageResource(R.drawable.ic_default_recipe_image)
        }

        with(holder.mainView) {
            tag = recipe
            setOnClickListener { clicklistener(recipe) }
            setOnLongClickListener { longClicklistener(recipe) }
        }
    }

    fun updateItems(recipeList: List<Recipe>) {
        mValues.clear()
        mValues.addAll(recipeList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mainView: View) : RecyclerView.ViewHolder(mainView) {
        val nameView: TextView
        val authorNameView: TextView
        val authorNameLayout: ViewGroup
        val recipeImageView: ImageView

        init {
            nameView = mainView.findViewById<TextView>(R.id.name)
            authorNameView = mainView.findViewById<TextView>(R.id.author_name)
            authorNameLayout = mainView.findViewById<ViewGroup>(R.id.author_layout)
            recipeImageView = mainView.findViewById<ImageView>(R.id.recipe_image)

        }

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "'"
        }
    }
}
