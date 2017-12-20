package com.cookplan.shopping_list.list_by_dishes

import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.ShopListStatus
import com.cookplan.shopping_list.list_by_dishes.DishIngredientsAdapter.DishIngredientsViewHolder


class DishIngredientsAdapter(val ingredients: List<Ingredient> = listOf()) : RecyclerView.Adapter<DishIngredientsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishIngredientsViewHolder =
            DishIngredientsViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.shop_list_by_dish_item_layout, parent, false))

    override fun onBindViewHolder(holder: DishIngredientsViewHolder, position: Int) {
        val ingredient = ingredients[position]

        val context = holder.nameTextView.context

        if (ingredient.shopListStatus === ShopListStatus.ALREADY_BOUGHT) {
            holder.nameTextView.paintFlags = holder.nameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.amountTextView.paintFlags = holder.amountTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.nameTextView.paintFlags = holder.nameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.amountTextView.paintFlags = holder.amountTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.nameTextView.text = ingredient.name

        val amount = ingredient.mainMeasureUnit.toValueString(ingredient.mainAmount)

        if (!amount.isEmpty()) {
            holder.amountTextView.visibility = View.VISIBLE

            holder.amountTextView.text = amount
        } else {
            holder.amountTextView.visibility = View.GONE
        }

        val background = holder.categoryView.getBackground()
        if (background is ShapeDrawable) {
            background.paint.color = ContextCompat.getColor(context, ingredient.category.colorId)
        } else if (background is GradientDrawable) {
            background.setColor(ContextCompat.getColor(context, ingredient.category.colorId))
        } else if (background is ColorDrawable) {
            background.color = ContextCompat.getColor(context, ingredient.category.colorId)
        }
    }

    override fun getItemCount() = ingredients.size

    fun clearSelectedItem() {
        notifyDataSetChanged()
    }

    inner class DishIngredientsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var nameTextView: TextView = v.findViewById<TextView>(R.id.ingredient_item_name)
        var amountTextView: TextView = v.findViewById<TextView>(R.id.ingredient_item_amount)
        var categoryView: View = v.findViewById(R.id.category_view)
        var mainView: View = v.findViewById(R.id.main_view)
    }
}