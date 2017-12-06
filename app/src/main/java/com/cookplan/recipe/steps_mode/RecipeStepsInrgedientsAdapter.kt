package com.cookplan.recipe.steps_mode

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

class RecipeStepsInrgedientsAdapter(private val ingredients: List<Ingredient>) : RecyclerView.Adapter<RecipeStepsInrgedientsAdapter.MainViewHolder>() {
    private val selectedIngred: MutableList<Ingredient> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
            MainViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.recipe_step_ingredients_item_layout, parent, false))

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = ingredients[position]
        holder.nameTextView.text = item.name

        holder.amountTextView.text = item.mainMeasureUnit.toValueString(item.mainAmount)
        holder.amountTextView.visibility = if (item.mainAmount > 0.0) View.VISIBLE else View.GONE

        holder.mainView.setBackgroundResource(
                if (selectedIngred.contains(item)) R.color.accent_color_light else R.color.white)

        with(holder.mainView) {
            tag = item
            setOnClickListener {
                if (selectedIngred.contains(item)) {
                    selectedIngred.remove(item)
                } else {
                    selectedIngred.add(item)
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var nameTextView: TextView
        var amountTextView: TextView
        var mainView: View

        init {
            nameTextView = v.findViewById<TextView>(R.id.ingredient_item_name)
            amountTextView = v.findViewById<TextView>(R.id.ingredient_item_amount)
            mainView = v.findViewById(R.id.main_view)
        }
    }
}