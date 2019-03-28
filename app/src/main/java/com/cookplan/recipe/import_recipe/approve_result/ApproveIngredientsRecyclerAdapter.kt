package com.cookplan.recipe.import_recipe.approve_result

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookplan.R
import com.cookplan.add_ingredient_view.ProductCategoriesSpinnerAdapter
import com.cookplan.models.*
import com.cookplan.recipe.import_recipe.approve_result.ApproveIngredientsRecyclerAdapter.ItemType.INGREDIENT
import com.cookplan.recipe.import_recipe.approve_result.ApproveIngredientsRecyclerAdapter.ItemType.RECIPE
import com.cookplan.utils.MeasureUnitUtils
import com.cookplan.utils.Utils


/**
 * Created by DariaEfimova on 09.06.17.
 */

open class ApproveIngredientsRecyclerAdapter(var recipe: Recipe,
                                             private val recipeToingredientsMap: MutableMap<String, List<Ingredient>>,
                                             private val productList: List<Product> = listOf(),
                                             private val allItemsDone: () -> Unit,
                                             private val saveRecipe: (Recipe) -> Unit,
                                             private val saveProductAndIngredient: (String, ProductCategory, String, Double, MeasureUnit) -> Unit,
                                             private val saveIngredient: (String, Ingredient) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val values: MutableList<String> = recipeToingredientsMap.keys.toMutableList()

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && recipe.id.isNullOrEmpty()) {
            RECIPE.id
        } else {
            INGREDIENT.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == RECIPE.id) {
                RecipeViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.approve_recipe_info_item_layout, parent, false))
            } else {
                IngredientViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.approve_ingredients_item_layout, parent, false))
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == RECIPE.id) {
            val recipeViewHolder = holder as RecipeViewHolder
            recipeViewHolder.nameTextView.text = recipe.name
            recipeViewHolder.descTextView.text = recipe.desc
            recipeViewHolder.saveButton.setOnClickListener { saveRecipe(recipe) }
        }
        if (getItemViewType(position) == INGREDIENT.id) {
            val ingredientViewHolder = holder as IngredientViewHolder
            val key = if (itemCount == values.size) values[position] else values[position - 1]
            fillIngredientItemView(key, ingredientViewHolder)
        }
    }

    override fun getItemCount(): Int {
        return if (recipe.id.isNullOrEmpty()) values.size + 1 else values.size
    }

    fun updateRecipe(newRecipe: Recipe) {
        recipe = newRecipe
        notifyDataSetChanged()
    }

    fun removeIngredientItem(key: String) {
        values.remove(key)
        recipeToingredientsMap.remove(key)
        if (values.size == 0 && !recipe.id.isNullOrEmpty()) {
            allItemsDone()
        } else {
            notifyDataSetChanged()
        }
    }

    private fun fillIngredientItemView(key: String, ingredientViewHolder: IngredientViewHolder) {
        ingredientViewHolder.howItWasTextView.text = key

        ingredientViewHolder.productNameEditText.text = null

        val adapter = ParsedIngredientsRecyclerAdapter(
                recipeToingredientsMap[key] ?: listOf()
        ) {
            with(ingredientViewHolder.productNameEditText) {
                tag = null
                text = null
                setCategorySpinnerValues(null, ingredientViewHolder)
            }
        }

        if (recipeToingredientsMap[key]?.size != 0) {
            ingredientViewHolder.parsedIngredListLayout.visibility = View.VISIBLE
            ingredientViewHolder.localingredRecyclerView.setHasFixedSize(true)
            ingredientViewHolder.localingredRecyclerView.isNestedScrollingEnabled = false
            ingredientViewHolder.localingredRecyclerView.layoutManager = LinearLayoutManager(
                    ingredientViewHolder.mainView.context)
            ingredientViewHolder.localingredRecyclerView.itemAnimator = DefaultItemAnimator()
            ingredientViewHolder.localingredRecyclerView.adapter = adapter
        } else {
            ingredientViewHolder.parsedIngredListLayout.visibility = View.GONE
        }
        with(ingredientViewHolder.saveButton) {
            tag = key
            setOnClickListener {
                if (!recipe.id.isNullOrEmpty()) {
                    if (adapter.selectedIngred != null) {
                        val ingredient = adapter.selectedIngred
                        ingredient?.recipeId = recipe.id
                        if (ingredient != null) saveIngredient(key, ingredient)
                    } else {
                        val productName = ingredientViewHolder.productNameEditText.text.toString()
                        if (!productName.isEmpty()) {
                            val amount = getAmountFromString(key)
                            val unit = getMeasureUnitFromString(key) { ingredientViewHolder.mainView.context }
                            var product: Product? = null
                            if (ingredientViewHolder.productNameEditText.tag != null) {
                                product = ingredientViewHolder.productNameEditText.tag as Product
                            }
                            if (product == null || productName != product.toStringName()) {
                                saveProductAndIngredient(
                                        key,
                                        ingredientViewHolder.productCategorySpinner.selectedItem as ProductCategory,
                                        productName, amount, unit)
                            } else {
                                val ingredient = Ingredient(null, product.toStringName(),
                                        product, recipe.id,
                                        unit, amount, ShopListStatus.NONE)
                                saveIngredient(key, ingredient)
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Сначала подтвердите название и описание рецепта", Toast.LENGTH_LONG).show()
                }
            }
        }

        with(ingredientViewHolder.productNameEditText) {
            setAdapter(ProductListAdapter(context, productList))
            setOnItemClickListener { _, _, pos, _ ->
                tag = productList[pos]
                setCategorySpinnerValues(productList[pos], ingredientViewHolder)
                addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable) {}

                    override fun beforeTextChanged(s: CharSequence, start: Int,
                                                   count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence, start: Int,
                                               before: Int, count: Int) {
                        if (count > 0) {
                            adapter.clearSelectedItem()
                        }
                    }
                })
            }
        }

        setCategorySpinnerValues(null, ingredientViewHolder)

        with(ingredientViewHolder.closeButton) {
            tag = key
            setOnClickListener { removeIngredientItem(key) }
        }
    }

    private fun getMeasureUnitFromString(tag: String, getContext: () -> Context): MeasureUnit {
        val splits = tag.split("\\d+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return MeasureUnitUtils.parseUnit(splits[splits.size - 1], getContext)
//        String[] splits = tag.split("\\d+");
//        return MeasureUnit.Companion.parseUnit(splits[splits.length - 1]);

    }

    private fun getAmountFromString(tag: String): Double {
        var amount = 0.0
        val splits = tag.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (splits.size == 2) {
            val splitsSpace = splits[1].split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            amount = Utils.getDoubleFromString(splitsSpace[1])
        }
        return amount

//        double amount = 0;
//        String[] splits = tag.split(":");
//        if (splits.length == 2) {
//            String[] splitsSpace = splits[1].split("\\s+");
//            amount = Utils.getDoubleFromString(splitsSpace[1]);
//        }
//        return amount;

    }

    private fun setCategorySpinnerValues(selectedProduct: Product?, holder: IngredientViewHolder) {
        with(holder.productCategorySpinner) {
            adapter = ProductCategoriesSpinnerAdapter(context,
                    if (selectedProduct != null) {
                        mutableListOf(selectedProduct.category)
                    } else ProductCategory.values().toMutableList())
            setSelection(0)
        }
    }

    private inner class IngredientViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val mainView: View = view.findViewById(R.id.main_view)
        internal val howItWasTextView = view.findViewById<TextView>(R.id.approve_ingred_how_it_was)
        internal val localingredRecyclerView = view.findViewById<RecyclerView>(R.id.local_ingredients_recycler_view)
        internal val parsedIngredListLayout = view.findViewById<ViewGroup>(R.id.parsed_list_layout)
        internal val productNameEditText = view.findViewById<AutoCompleteTextView>(R.id.product_name_text)
        internal val productCategorySpinner = view.findViewById<Spinner>(R.id.category_list_spinner)
        internal val saveButton = view.findViewById<Button>(R.id.approve_button)
        internal val closeButton = view.findViewById<ImageView>(R.id.close_btn)

    }

    private inner class RecipeViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal val nameTextView = view.findViewById<TextView>(R.id.approve_recipe_name)
        internal val descTextView = view.findViewById<TextView>(R.id.approve_recipe_desc)
        internal val saveButton = view.findViewById<Button>(R.id.approve_button)

    }

    protected enum class ItemType constructor(val id: Int) {
        RECIPE(0),
        INGREDIENT(1)
    }
}