package com.cookplan.recipe.import_recipe.approve_result

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ProgressBar
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.recipe.view_item.RecipeViewActivity

class ImportRecipeActivity : BaseActivity(), ImportRecipeView {

    private var presenter: ImportRecipePresenter? = null
    private var adapter: ApproveIngredientsRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_recipe)
        setNavigationArrow()
        setTitle(getString(R.string.import_recipe_title))
        setSubTitle("Подтверждение правильности рецепта после импорта")
        presenter = ImportRecipePresenterImpl(this)

        val url = intent.getStringExtra(URL_TO_IMPORT_KEY)

        presenter?.importRecipeFromUrl(url)

        findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

        val recyclerView = findViewById<RecyclerView>(R.id.approve_ingredients_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun setImportResult(recipe: Recipe, recipeToingredientsMap: MutableMap<String, List<Ingredient>>) {
        if (recipeToingredientsMap.isEmpty()) {
            finish()
        }
        findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
        val recyclerView = findViewById<RecyclerView>(R.id.approve_ingredients_recycler_view)
        recyclerView.visibility = View.VISIBLE
        adapter = ApproveIngredientsRecyclerAdapter(
                recipe,
                recipeToingredientsMap,
                presenter?.getAllProductsList() ?: listOf(),
                { startNewRecipeActivity(adapter?.recipe) },
                {
                    findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                    presenter?.saveRecipe(it)
                },
                { key, category, name, amount, measureUnit ->
                    findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                    presenter?.saveProductAndIngredient(key, category, name, amount, measureUnit)
                },
                { key, ingredient ->
                    findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                    presenter?.saveIngredient(key, ingredient)
                })
        recyclerView.adapter = adapter
    }

    private fun startNewRecipeActivity(recipe: Recipe?) {
        val intent = Intent(this, RecipeViewActivity::class.java)
        if (recipe != null) intent.putExtra(RecipeViewActivity.RECIPE_OBJECT_KEY, recipe)
        startActivityWithLeftAnimation(intent)
        finish()
    }

    override fun setError(s: String) {
        findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
        val mainView = findViewById<View>(R.id.main_view)
        Snackbar.make(mainView, s, Snackbar.LENGTH_LONG).show()
    }

    override fun setRecipeSavedSuccessfully(recipe: Recipe) {
        findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
        adapter?.updateRecipe(recipe)
    }

    override fun setIngredientSavedSuccessfully(key: String) {
        findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
        adapter?.removeIngredientItem(key)
    }

    companion object {

        val URL_TO_IMPORT_KEY = "URL_TO_IMPORT_KEY"
    }
}