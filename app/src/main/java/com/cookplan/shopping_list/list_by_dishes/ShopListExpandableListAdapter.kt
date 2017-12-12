package com.cookplan.shopping_list.list_by_dishes

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.models.ShopListStatus


class ShopListExpandableListAdapter(private val context: Context,
                                    val recipeList: MutableList<Recipe>,
                                    val recipeIdsToIngredientMap: Map<String, List<Ingredient>>,
                                    private val ingredClickListener: (Ingredient) -> Unit?,
                                    private val deleteGroupListener: (Recipe, List<Ingredient>) -> Any?) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosititon: Int): Any {
        val recipeId = recipeList[groupPosition].id
        return recipeIdsToIngredientMap[recipeId]?.get(childPosititon) ?: Unit
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val recipeId = recipeList[groupPosition].id
        return recipeIdsToIngredientMap.get(recipeId)?.size ?: 0
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int,
                              isLastChild: Boolean, childview: View?, parent: ViewGroup): View {
        val infalInflater = this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = childview ?: infalInflater.inflate(R.layout.shop_list_by_dish_item_layout, null)

        val child = getChild(groupPosition, childPosition)

        if (child is Ingredient) {
            val ingredient = child
            val nameTextView = convertView.findViewById<View>(R.id.ingredient_item_name) as TextView
            val amountTextView = convertView.findViewById<TextView>(R.id.ingredient_item_amount)
            val mainView = convertView.findViewById(R.id.main_view) ?: convertView

            val flags = nameTextView.paintFlags
            if (ingredient.shopListStatus === ShopListStatus.ALREADY_BOUGHT) {
                nameTextView.paintFlags = flags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                nameTextView.paintFlags = flags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            nameTextView.text = ingredient.name

            if (!(ingredient.userName?.isEmpty() ?: true)) {
                nameTextView?.text = nameTextView?.text.toString() +
                        " (by " + " " + ingredient.userName + ")"
            }

            if (ingredient.mainAmount != 0.0) {
                amountTextView?.visibility = View.VISIBLE
                val amount = ingredient.mainMeasureUnit.toValueString(ingredient.mainAmount)
                amountTextView?.text = amount
            } else {
                amountTextView?.visibility = View.GONE
            }

            with(mainView) {
                tag = ingredient
                setOnClickListener { ingredClickListener(ingredient) }
            }
        }
        return convertView
    }

    override fun getGroup(groupPosition: Int): Any {
        return recipeList[groupPosition]
    }

    override fun getGroupCount(): Int {
        return recipeList.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean,
                              groupView: View?, parent: ViewGroup): View {
        val recipe = getGroup(groupPosition) as Recipe
        val infalInflater = this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = groupView ?: infalInflater.inflate(R.layout.shop_list_by_dish_group_item_layout, null)
        val headerTextView = convertView.findViewById<View>(R.id.recipe_item_name) as TextView

        headerTextView.text = recipe.name


        val deleteImageView = convertView.findViewById<View>(R.id.delete_group_image_view) as ImageView
        deleteImageView.tag = recipe
        deleteImageView.setOnClickListener {
            val selectedRecipe = it.getTag() as Recipe
            val ingredients = recipeIdsToIngredientMap.get(selectedRecipe.id) ?: listOf()
            deleteGroupListener(selectedRecipe, ingredients)
        }
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
