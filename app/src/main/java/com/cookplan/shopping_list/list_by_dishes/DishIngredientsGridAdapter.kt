package com.cookplan.shopping_list.list_by_dishes

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.ShopListStatus
import com.cookplan.views.dynamic_grid.DynamicGridAdapter

class DishIngredientsGridAdapter(val ingredients: List<Ingredient> = listOf(), val context: Context) : DynamicGridAdapter() {

    override fun getViewAtPosition(position: Int): View {
        val ingredient = ingredients[position]

        val mainView = LayoutInflater.from(context).inflate(R.layout.shop_list_by_dish_item_layout, null)

        val nameTextView: TextView = mainView.findViewById<TextView>(R.id.ingredient_item_name)
        val amountTextView: TextView = mainView.findViewById<TextView>(R.id.ingredient_item_amount)

        if (ingredient.shopListStatus === ShopListStatus.ALREADY_BOUGHT) {
            nameTextView.paintFlags = nameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            amountTextView.paintFlags = amountTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            nameTextView.paintFlags = nameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            amountTextView.paintFlags = amountTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        nameTextView.text = ingredient.name

        val amount = ingredient.mainMeasureUnit.toValueString(ingredient.mainAmount)

        if (!amount.isEmpty()) {
            amountTextView.visibility = View.VISIBLE

            amountTextView.text = amount
        } else {
            amountTextView.visibility = View.GONE
        }

        val categoryView: View = mainView.findViewById(R.id.category_view)

        val background = categoryView.background
        when (background) {
            is ShapeDrawable -> background.paint.color = ContextCompat.getColor(context, ingredient.category.colorId)
            is GradientDrawable -> background.setColor(ContextCompat.getColor(context, ingredient.category.colorId))
            is ColorDrawable -> background.color = ContextCompat.getColor(context, ingredient.category.colorId)
        }

        return mainView
    }

    override fun getItemsCount(): Int {
        return ingredients.size
    }

}