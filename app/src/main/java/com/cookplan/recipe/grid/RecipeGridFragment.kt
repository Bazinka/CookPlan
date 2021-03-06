package com.cookplan.recipe.grid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookplan.BaseActivity
import com.cookplan.BaseFragment
import com.cookplan.R
import com.cookplan.main.MainActivity
import com.cookplan.models.Recipe
import com.cookplan.recipe.create.ingredients.IngredientsToRecipeActivity
import com.cookplan.recipe.import_recipe.search_url.SearchRecipeUrlActivity
import com.cookplan.recipe.view_item.RecipeViewActivity
import com.cookplan.utils.GridSpacingItemDecoration
import com.cookplan.utils.Utils
import com.github.clans.fab.FloatingActionButton

class RecipeGridFragment : BaseFragment(), RecipeGridView {

    private var adapter: RecipeGridRecyclerViewAdapter? = null
    private var presenter: RecipeGridPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = RecipeGridPresenterImpl(this)
    }

    override fun onStart() {
        super.onStart()
        presenter?.getRecipeList()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_recipe_list, container, false) as ViewGroup
        val progressBar = mainView?.findViewById<View>(R.id.progress_bar_layout)
        progressBar?.visibility = View.VISIBLE

        // Set the adapter
        val recyclerView = mainView?.findViewById<RecyclerView>(R.id.recipe_list_recycler)
        recyclerView?.layoutManager = GridLayoutManager(activity, 2)
        recyclerView?.addItemDecoration(GridSpacingItemDecoration(2,
                Utils.dpToPx(activity as Context, 16), true))
        recyclerView?.itemAnimator = DefaultItemAnimator()
        adapter = RecipeGridRecyclerViewAdapter(
                { clickRecipe ->
                    if (activity is BaseActivity) {
                        val intent = Intent(activity, RecipeViewActivity::class.java)
                        intent.putExtra(RecipeViewActivity.RECIPE_OBJECT_KEY, clickRecipe)
                        (activity as BaseActivity).startActivityForResultWithLeftAnimation(intent,
                                MainActivity.OPEN_SHOP_LIST_REQUEST)
                    }
                }, activity as Context)
        recyclerView?.adapter = adapter

        val addRecipeFab = mainView?.findViewById<View>(R.id.add_recipe_fab) as FloatingActionButton
        addRecipeFab.setOnClickListener { startNewRecipeActivity() }

        val importRecipeFab = mainView?.findViewById<View>(R.id.import_recipe_fab) as FloatingActionButton
        importRecipeFab.setOnClickListener { startSearchRecipeUrlActivity() }

        return mainView
    }

    internal fun startSearchRecipeUrlActivity() {
        val intent = Intent(activity, SearchRecipeUrlActivity::class.java)
        (activity as BaseActivity).startActivityWithLeftAnimation(intent)
    }

    internal fun startNewRecipeActivity() {
        val intent = Intent(activity, IngredientsToRecipeActivity::class.java)
        (activity as BaseActivity).startActivityForResultWithLeftAnimation(intent, CREATE_RECIPE_REQUEST)
    }

    override fun setEmptyView() {
        val progressBar = mainView?.findViewById<View>(R.id.progress_bar_layout)
        progressBar?.visibility = View.GONE
        setEmptyViewVisability(View.VISIBLE)
        setRecyclerViewVisability(View.GONE)
    }

    private fun setRecyclerViewVisability(visability: Int) {
        mainView?.findViewById<View>(R.id.recipe_list_recycler)?.visibility = visability
    }

    override fun setRecipeList(recipeList: List<Recipe>) {
        mainView?.findViewById<View>(R.id.progress_bar_layout)?.visibility = View.GONE
        setEmptyViewVisability(View.GONE)
        setRecyclerViewVisability(View.VISIBLE)
        adapter?.updateItems(recipeList)
    }

    companion object {

        val CREATE_RECIPE_REQUEST = 12

        @JvmStatic
        fun newInstance(): RecipeGridFragment {
            val fragment = RecipeGridFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
