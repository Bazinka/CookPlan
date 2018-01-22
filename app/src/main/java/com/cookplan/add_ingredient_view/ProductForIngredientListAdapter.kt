package com.cookplan.add_ingredient_view

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
        holder.categoryNameTextView.setTextColor(ContextCompat.getColor(holder.mainView.context, product.category.colorId))

        holder.categoryView.setBackgroundResource(product.category.colorId)


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
        val productNameTextView: TextView
        val categoryNameTextView: TextView
        val categoryView: View

        init {
            productNameTextView = mainView.findViewById<TextView>(R.id.product_item_name)
            categoryNameTextView = mainView.findViewById<TextView>(R.id.category_product_item_name)
            categoryView = mainView.findViewById<View>(R.id.category_view)
        }

    }
}