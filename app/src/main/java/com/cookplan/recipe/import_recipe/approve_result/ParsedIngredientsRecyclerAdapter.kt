package com.cookplan.recipe.import_recipe.approve_result

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.recipe.import_recipe.approve_result.ParsedIngredientsRecyclerAdapter.ParsedIngredientsViewHolder


class ParsedIngredientsRecyclerAdapter(val ingredients: List<Ingredient> = listOf(),
                                       private val itemSelect: (Ingredient) -> Unit) : Adapter<ParsedIngredientsViewHolder>() {

    var selectedIngred: Ingredient? = null

    init {
        if (ingredients.size == 1) {
            selectedIngred = ingredients[0]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParsedIngredientsViewHolder =
            ParsedIngredientsViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.parsed_ingred_list_item_layout, parent, false))

    override fun onBindViewHolder(holder: ParsedIngredientsViewHolder, position: Int) {
        val ingredient = ingredients[position]

        val context = holder.nameTextView.context
        if (ingredient.name == selectedIngred?.name) {
            holder.nameTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.white))
            holder.amountTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.white))
            holder.mainView.setBackgroundResource(ingredient.category.colorId)
        } else {
            holder.nameTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.primary_text_color))
            holder.amountTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.primary_text_color))
            holder.mainView.setBackgroundResource(R.color.white)
        }

        holder.nameTextView.text = ingredient.name

        val amount = ingredient.mainMeasureUnit.toValueString(ingredient.mainAmount)

        if (!amount.isEmpty()) {
            holder.amountTextView.visibility = View.VISIBLE

            holder.amountTextView.text = amount
        } else {
            holder.amountTextView.visibility = View.GONE
        }

        holder.categoryView.setBackgroundResource(ingredient.category.colorId)


        with(holder.mainView) {
            tag = ingredient
            setOnClickListener {
                selectedIngred = ingredient
                notifyDataSetChanged()
                itemSelect(ingredient)
            }
        }
    }

    override fun getItemCount() = ingredients.size

    fun clearSelectedItem() {
        selectedIngred = null
        notifyDataSetChanged()
    }

    inner class ParsedIngredientsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var nameTextView: TextView = v.findViewById<TextView>(R.id.ingredient_item_name)
        var amountTextView: TextView = v.findViewById<TextView>(R.id.ingredient_item_amount)
        var categoryView: View = v.findViewById(R.id.category_view)
        var mainView: View = v.findViewById(R.id.main_view)
    }
}