package com.cookplan.recipe.import_recipe.approve_result

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.cookplan.R
import com.cookplan.models.Product
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by DariaEfimova on 20.03.17.
 */

class ProductListAdapter(context: Context, private val itemsAll: List<Product>) : ArrayAdapter<Product>(context, R.layout.product_autocomplete_item_layout, itemsAll) {
    private val suggestions: MutableList<Product> = mutableListOf()

    private val nameFilter = object : Filter() {
        override fun convertResultToString(resultValue: Any): String {
            return (resultValue as Product).toStringName()
        }

        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            suggestions.clear()
            for (product in itemsAll) {
                if (product.toStringName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    suggestions.add(product)
                }
            }
            val filterResults = Filter.FilterResults()
            filterResults.values = suggestions
            filterResults.count = suggestions.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
            if (results.values != null && results.count > 0) {
                val filteredList = CopyOnWriteArrayList(results.values as ArrayList<*>)
                clear()
                for (c in filteredList) {
                    add(c as Product)
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = convertView ?: vi.inflate(R.layout.product_autocomplete_item_layout, null)

        val product = itemsAll[position]

        val productNameTextView = view?.findViewById<TextView>(R.id.product_item_name)
        productNameTextView?.text = product.toStringName()

        val categoryNameTextView = view.findViewById<TextView>(R.id.category_product_item_name)
        categoryNameTextView?.setText(product.category.getNameResourceId())
        categoryNameTextView?.setTextColor(ContextCompat.getColor(parent.context, product.category.colorId))

        val categoryView = view.findViewById<View>(R.id.category_view)
        categoryView?.setBackgroundResource(product.category.colorId)

        return view
    }

    override fun getFilter(): Filter {
        return nameFilter
    }
}