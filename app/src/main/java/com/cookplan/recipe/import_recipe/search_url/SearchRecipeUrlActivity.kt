package com.cookplan.recipe.import_recipe.search_url

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.network.GoogleRecipe
import com.cookplan.recipe.import_recipe.web_browser.WebBrowserActivity
import java.util.*

class SearchRecipeUrlActivity : BaseActivity(), SearchRecipeUrlView {

    private var presenter: SearchRecipeUrlPresenter? = null
    private var adapter: GoogleRecipeListRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe_url)
        setNavigationArrow()
        setTitle(getString(R.string.search_recipe_title))

        presenter = SearchRecipeUrlPresenterImpl(this)
        val searchButton = findViewById<Button>(R.id.search_btn)
        searchButton.setOnClickListener { view ->
            val queryEdiText = findViewById<EditText>(R.id.text_editText)
            val query = queryEdiText.text.toString()
            if (!query.isEmpty()) {
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                inputManager.hideSoftInputFromWindow(currentFocus.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS)

                findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                presenter?.searchRecipes(query)
                adapter?.clearItems()
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.search_results_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = GoogleRecipeListRecyclerAdapter(
                ArrayList(),
                { offset ->
                    findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                    presenter?.loadNextPart(offset)
                },
                { url -> startWebActivity(url) })
        recyclerView.adapter = adapter
    }

    private fun startWebActivity(url: String) {
        val intent = Intent(this, WebBrowserActivity::class.java)
        intent.putExtra(WebBrowserActivity.URL_KEY, url)
        startActivityWithLeftAnimation(intent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun setResultGoogleSearchList(googleRecipes: List<GoogleRecipe>) {
        findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
        adapter?.addItems(googleRecipes)
    }

    override fun setError(errorResource: Int) {
        findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
        Snackbar.make(findViewById<View>(R.id.main_view), getString(errorResource), Snackbar.LENGTH_LONG).show()
    }
}