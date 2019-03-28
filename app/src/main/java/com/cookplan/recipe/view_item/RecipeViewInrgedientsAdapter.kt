package com.cookplan.recipe.view_item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.ShopListStatus
import com.cookplan.recipe.view_item.RecipeViewInrgedientsAdapter.MainViewHolder
import com.cookplan.utils.MeasureUnitUtils

/**
 * Created by DariaEfimova on 18.03.17.
 */

class RecipeViewInrgedientsAdapter(private val ingredients: MutableList<Ingredient> = mutableListOf(),
                                   private val itemSelectListener: (Ingredient) -> Unit) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
            MainViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.recipe_view_ingredients_item_layout, parent, false))

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.nameTextView.text = ingredient.name

        if (ingredient.mainAmount > 0.0) {
            holder.amountTextView.visibility = View.VISIBLE

            holder.amountTextView.text = MeasureUnitUtils.valueToString(
                    ingredient.mainMeasureUnit,
                    ingredient.mainAmount) { holder.amountTextView.context }

        } else {
            holder.amountTextView.visibility = View.GONE
        }

        holder.checkBox.isChecked = ingredient.shopListStatus == ShopListStatus.NEED_TO_BUY

        val listener: (View) -> Unit = {
            it.tag = ingredient
            it.setOnClickListener {
                if (ingredient.shopListStatus === ShopListStatus.NEED_TO_BUY) {
                    ingredient.shopListStatus = ShopListStatus.NONE
                } else {
                    ingredient.shopListStatus = ShopListStatus.NEED_TO_BUY
                }

                itemSelectListener(ingredient)
                notifyDataSetChanged()
            }
        }
        with(holder.mainView) { listener(this) }
        with(holder.checkBox) { listener(this) }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var nameTextView: TextView
        var amountTextView: TextView
        var checkBox: CheckBox
        var mainView: View

        init {
            nameTextView = v.findViewById<TextView>(R.id.ingredient_item_name)
            amountTextView = v.findViewById<TextView>(R.id.ingredient_item_amount)
            checkBox = v.findViewById<CheckBox>(R.id.add_to_shop_list_checkbox)
            mainView = v.findViewById(R.id.main_view)
        }
    }

    fun updateItems(ingredientList: List<Ingredient>) {
        ingredients.clear()
        ingredients.addAll(ingredientList)
        notifyDataSetChanged()
    }

    fun getIngredients(): List<Ingredient> {
        return ingredients
    }
}