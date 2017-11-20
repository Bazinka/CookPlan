package com.cookplan.shopping_list.total_list

import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.ShopListStatus
import com.cookplan.shopping_list.total_list.TotalShopListRecyclerViewAdapter.ViewHolderTotalShopList


class TotalShopListRecyclerViewAdapter(private val ingredients: MutableList<Ingredient> = mutableListOf(),
                                       private val listener: (Ingredient) -> Unit) : RecyclerView.Adapter<ViewHolderTotalShopList>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolderTotalShopList(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.total_shop_list_item_layout, parent, false))

    override fun onBindViewHolder(holder: ViewHolderTotalShopList, position: Int) {
        val item = ingredients[position]

        val context = holder.nameTextView.context

        if (item.shopListStatus === ShopListStatus.ALREADY_BOUGHT) {
            holder.nameTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.white))
            holder.nameTextView.paintFlags = holder.nameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            holder.amountTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.white))
            holder.amountTextView.paintFlags = holder.amountTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            holder.mainView.setBackgroundResource(item.category.colorId)
        } else {
            holder.nameTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.primary_text_color))
            holder.nameTextView.paintFlags = holder.nameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.amountTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.primary_text_color))
            holder.amountTextView.paintFlags = holder.amountTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.mainView.setBackgroundResource(R.color.white)
        }
        holder.nameTextView.text = item.name

        holder.amountTextView.visibility = if (item.amountString != null) View.VISIBLE else View.GONE
        holder.amountTextView.text = item.amountString

        holder.categoryView.setBackgroundResource(item.category.colorId)

        with(holder.mainView) {
            tag = item
            setOnClickListener { listener(item) }
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun update(ingredientList: List<Ingredient>) {
        ingredients.clear()
        ingredients.addAll(ingredientList)
        notifyDataSetChanged()
    }

    fun getIngredients(): List<Ingredient> {
        return ingredients
    }

    inner class ViewHolderTotalShopList(v: View) : RecyclerView.ViewHolder(v) {
        var nameTextView: TextView
        var amountTextView: TextView
        var categoryView: View
        var mainView: View

        init {
            nameTextView = v.findViewById<View>(R.id.ingredient_item_name) as TextView
            amountTextView = v.findViewById<View>(R.id.ingredient_item_amount) as TextView
            categoryView = v.findViewById(R.id.category_view)
            mainView = v.findViewById(R.id.main_view)
        }
    }
}