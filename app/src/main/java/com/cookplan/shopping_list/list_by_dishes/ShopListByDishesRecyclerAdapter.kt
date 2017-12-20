package com.cookplan.shopping_list.list_by_dishes

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe


class ShopListByDishesRecyclerAdapter(private val recipeList: MutableList<Recipe>,
                                      private val recipeIdsToIngredientMap: Map<String, List<Ingredient>>,
                                      private val deleteGroupListener: (Recipe, List<Ingredient>) -> Any?) : RecyclerView.Adapter<ShopListByDishesRecyclerAdapter.ViewHolderShopList>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolderShopList(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.shop_list_by_dish_group_item_layout, parent, false))

    override fun onBindViewHolder(holder: ViewHolderShopList, position: Int) {
        val recipe = recipeList[position]

        holder.nameTextView.text = recipe.name

        val adapter = DishIngredientsAdapter(
                recipeIdsToIngredientMap[recipe.id] ?: listOf()
        )

        holder.ingredientsRecyclerView.setHasFixedSize(true)
        holder.ingredientsRecyclerView.isNestedScrollingEnabled = false
        holder.ingredientsRecyclerView.layoutManager = LinearLayoutManager(holder.mainView.context)
        holder.ingredientsRecyclerView.itemAnimator = DefaultItemAnimator()
        holder.ingredientsRecyclerView.adapter = adapter
        with(holder.deleteImageView) {
            tag = recipe
            setOnClickListener {
                val ingredients = recipeIdsToIngredientMap.get(recipe.id) ?: listOf()
                deleteGroupListener(recipe, ingredients)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

//    fun update(ingredientList: List<Ingredient>) {
//        ingredients.clear()
//        ingredients.addAll(ingredientList)
//        notifyDataSetChanged()
//    }
//
//    fun getIngredients(): List<Ingredient> {
//        return ingredients
//    }

    inner class ViewHolderShopList(v: View) : RecyclerView.ViewHolder(v) {
        var nameTextView = v.findViewById<TextView>(R.id.recipe_item_name)
        var ingredientsRecyclerView = v.findViewById<RecyclerView>(R.id.local_ingredients_recycler_view)
        var mainView = v.findViewById<View>(R.id.main_view)
        val deleteImageView = v.findViewById<ImageView>(R.id.delete_group_image_view)

    }
}