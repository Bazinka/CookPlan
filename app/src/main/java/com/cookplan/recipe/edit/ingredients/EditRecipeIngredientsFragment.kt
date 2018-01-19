package com.cookplan.recipe.edit.ingredients


import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cookplan.BaseFragment
import com.cookplan.R
import com.cookplan.add_ingredient_view.AddIngredientViewFragment
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

        val fragment = AddIngredientViewFragment.newInstance(recipe, false)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.add_ingredient_fragment_container, fragment)
        transaction?.commit()


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
        } else {
            mainView?.findViewById<View>(R.id.list_card_view)?.visibility = View.VISIBLE
        }
    }

    override fun setErrorToast(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
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
