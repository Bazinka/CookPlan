package com.cookplan.recipe.edit.add_ingredients

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.Ingredient

/**
 * Created by DariaEfimova on 18.03.17.
 */

class EditRecipeInrgedientsAdapter(private val removelistener: (Ingredient) -> Unit) : RecyclerView.Adapter<EditRecipeInrgedientsAdapter.MainViewHolder>() {

    private val ingredients: MutableList<Ingredient> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MainViewHolder =
            MainViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.edit_recipe_ingredients_item_layout, parent, false))

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.nameTextView.text = ingredient.name

        if (ingredient.mainAmount > 1e-8) {
            holder.amountTextView.visibility = View.VISIBLE
            holder.amountTextView.text = ingredient.mainMeasureUnit.toValueString(ingredient.mainAmount)
        } else {
            holder.amountTextView.visibility = View.GONE
        }

        holder.removeButtonLayout.setBackgroundResource(ingredient.category.colorId)
        holder.categoryView.setBackgroundResource(ingredient.category.colorId)

        with(holder.mainRemoveLayout) {
            tag = ingredient
            setOnClickListener { removelistener(ingredient) }
        }
    }

    override fun getItemCount() = ingredients.size

    class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var nameTextView: TextView
        var amountTextView: TextView
        var mainRemoveLayout: ViewGroup
        var removeButtonLayout: ViewGroup
        var categoryView: View

        init {
            nameTextView = v.findViewById<TextView>(R.id.ingredient_item_name)
            amountTextView = v.findViewById<TextView>(R.id.ingredient_item_amount)
            mainRemoveLayout = v.findViewById<ViewGroup>(R.id.ingredient_remove_layout)
            removeButtonLayout = v.findViewById<ViewGroup>(R.id.ingredient_remove_button_layout)
            categoryView = v.findViewById(R.id.category_view)
        }
    }

    fun addItem(ingredient: Ingredient) {
        ingredients.add(ingredient)
        notifyDataSetChanged()
    }


    fun updateItems(ingredientList: List<Ingredient>) {
        ingredients.clear()
        ingredients.addAll(ingredientList)
        notifyDataSetChanged()
    }
}