package com.cookplan.add_ingredient_view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.ProductCategory

/**
 * Created by DariaEfimova on 20.03.17.
 */

class ProductCategoriesSpinnerAdapter(context: Context, private val itemsAll: List<ProductCategory>) : ArrayAdapter<ProductCategory>(context, R.layout.product_category_spinner_layout, itemsAll), SpinnerAdapter {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = convertView ?: vi.inflate(R.layout.product_category_dropdown_layout, null)


        val category = itemsAll[position]
        val categoryNameTextView = view.findViewById<TextView>(R.id.category_product_item_name)
        categoryNameTextView?.text = category.toString()
        categoryNameTextView?.setTextColor(ContextCompat.getColor(parent.context, category.colorId))
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: vi.inflate(R.layout.product_category_spinner_layout, null)

        val category = itemsAll[position]

        val categoryNameTextView = view.findViewById<TextView>(R.id.category_product_item_name)
        categoryNameTextView?.text = category.toString()
        val categoryView = view.findViewById<View>(R.id.main_view)
        categoryView?.setBackgroundResource(category.colorId)

        return view
    }
}