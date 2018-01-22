package com.cookplan.add_ingredient_view

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
import com.cookplan.add_ingredient_view.ProductForIngredientListAdapter.ViewHolder
import com.cookplan.models.Product

/**
 * Created by DariaEfimova on 20.03.17.
 */

class ProductForIngredientListAdapter(private val productList: MutableList<Product> = mutableListOf(),
                                      private val clicklistener: (Product) -> Any) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_autocomplete_item_layout, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.productNameTextView.text = product.toStringName()

        holder.categoryNameTextView.text = product.category.toString()


        val background = holder.categoryView.background
        if (background is ShapeDrawable) {
            background.paint.color = ContextCompat.getColor(holder.mainView.context, product.category.colorId)
        } else if (background is GradientDrawable) {
            background.setColor(ContextCompat.getColor(holder.mainView.context, product.category.colorId))
        } else if (background is ColorDrawable) {
            background.color = ContextCompat.getColor(holder.mainView.context, product.category.colorId)
        }

        with(holder.mainView) {
            tag = product
            setOnClickListener { clicklistener(product) }
        }
    }

    fun updateItems(products: List<Product>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ViewHolder(val mainView: View) : RecyclerView.ViewHolder(mainView) {
        val productNameTextView: TextView = mainView.findViewById<TextView>(R.id.product_item_name)
        val categoryNameTextView: TextView = mainView.findViewById<TextView>(R.id.category_product_item_name)
        val categoryView: View = mainView.findViewById<View>(R.id.category_view)

    }
}