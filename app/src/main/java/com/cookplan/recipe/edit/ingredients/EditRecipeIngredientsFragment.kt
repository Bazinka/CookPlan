package com.cookplan.recipe.edit.ingredients


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookplan.BaseActivity
import com.cookplan.BaseFragment
import com.cookplan.R
import com.cookplan.add_ingredient_view.ProductForIngredientActivity
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe

class EditRecipeIngredientsFragment : BaseFragment(), EditRecipeIngredientsView {

    private var adapter: EditRecipeInrgedientsAdapter? = null
    private var presenter: EditRecipeIngredientsPresenter? = null
    private var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = arguments?.getSerializable(RECIPE_KEY) as Recipe
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_edit_recipe_ingredients, container, false) as ViewGroup

        val chooseButton = mainView?.findViewById<Button>(R.id.add_shop_list_items_button)
        chooseButton?.setOnClickListener {
            val intent = Intent(activity, ProductForIngredientActivity::class.java)
            intent.putExtra(ProductForIngredientActivity.RECIPE_ID_KEY, recipe?.id)
            intent.putExtra(ProductForIngredientActivity.RECIPE_NEED_TO_BUY_KEY, false)
            if (activity is BaseActivity) {
                (activity as BaseActivity).startActivityWithLeftAnimation(intent)
            }
        }
        val recyclerView = mainView?.findViewById<RecyclerView>(R.id.ingredients_recycler_view)
        recyclerView?.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(activity)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()

        adapter = EditRecipeInrgedientsAdapter { ingredient ->
            presenter?.removeIngredient(ingredient)
        }
        recyclerView?.adapter = adapter

        presenter = EditRecipeIngredientsPresenterImpl(this, recipe)
        presenter?.getAsyncIngredientList()
        return mainView
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
    }

    override fun setIngredientList(ingredientList: List<Ingredient>) {
        adapter?.updateItems(ingredientList)
        if (ingredientList.isEmpty()) {
            mainView?.findViewById<View>(R.id.list_card_view)?.visibility = View.GONE
            val layoutparams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            layoutparams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            mainView?.findViewById<View>(R.id.add_product_card_view)?.layoutParams = layoutparams
        } else {
            mainView?.findViewById<View>(R.id.list_card_view)?.visibility = View.VISIBLE
            val layoutparams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            layoutparams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            mainView?.findViewById<View>(R.id.add_product_card_view)?.layoutParams = layoutparams
        }
    }

    override fun setErrorToast(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
    }

    override fun setErrorToast(errorId: Int) {
        Toast.makeText(activity, getString(errorId), Toast.LENGTH_LONG).show()
    }

    fun getIngredientsList(): List<Ingredient> {
        return adapter?.getItems() ?: listOf()
    }

    companion object {
        private val RECIPE_KEY: String = "RECIPE_KEY"

        @JvmStatic
        fun newInstance(recipe: Recipe): EditRecipeIngredientsFragment {
            return EditRecipeIngredientsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(RECIPE_KEY, recipe)
                }
            }
        }
    }
}
