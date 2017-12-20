package com.cookplan.recipe.edit.add_ingredients

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.add_ingredient_view.AddIngredientViewFragment
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe

class EditRecipeIngredientsActivity : BaseActivity(), EditRecipeIngredientsView {

    private var adapter: EditRecipeInrgedientsAdapter? = null
    private var presenter: EditRecipeIngredientsPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_recipe_ingredients)
        setNavigationArrow()

        if (intent.hasExtra(RECIPE_OBJECT_KEY)) {
            val recipe = intent.getSerializableExtra(RECIPE_OBJECT_KEY) as Recipe
            setTitle(getString(R.string.add_recipe_second_screen_title) + " " + recipe.name)

            val fragment = AddIngredientViewFragment.newInstance(recipe, false)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()


            val recyclerView = findViewById<RecyclerView>(R.id.ingredients_recycler_view)
            recyclerView.setHasFixedSize(true)

            val layoutManager = LinearLayoutManager(this)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()

            adapter = EditRecipeInrgedientsAdapter { ingredient ->
                presenter?.removeIngredient(ingredient)
            }
            recyclerView.adapter = adapter

            presenter = EditRecipeIngredientsPresenterImpl(this, recipe)
        } else {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.getAsyncIngredientList()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onCreateOptionsMenu(_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.done_menu, _menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.app_bar_done) {
            //TODO: saving data
            //            Intent intent = new Intent(this, MainActivity.class);
            //            startActivityWithLeftAnimation(intent);
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun setIngredientList(ingredientList: List<Ingredient>) {
        if (ingredientList.isEmpty()) {
            findViewById<View>(R.id.list_card_view).visibility = View.GONE
        } else {
            adapter?.updateItems(ingredientList)
        }
    }

    override fun setErrorToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    companion object {
        val RECIPE_OBJECT_KEY = "new_recipe_name"
    }
}
