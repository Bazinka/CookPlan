package com.cookplan.recipe.create

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.Recipe
import com.cookplan.recipe.edit.EditRecipePresenter
import com.cookplan.recipe.edit.EditRecipePresenterImpl
import com.cookplan.recipe.edit.EditRecipeView
import com.cookplan.recipe.edit.description.EditRecipeDescActivity
import com.cookplan.recipe.edit.ingredients.EditRecipeIngredientsFragment

class IngredientsToRecipeActivity : BaseActivity(), EditRecipeView {

    private var editRecipePresenter: EditRecipePresenter? = null
    private var newRecipeId: String? = null
    private var ingredientsFragment: EditRecipeIngredientsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe_ingredients)
        setNavigationArrow()
        setTitle(getString(R.string.step1_ingregients_title))

        editRecipePresenter = EditRecipePresenterImpl(this)
        editRecipePresenter?.saveRecipe(null, String(), String())
    }


    override fun setErrorToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        editRecipePresenter?.removeRecipe(Recipe(newRecipeId ?: String()),
                ingredientsFragment?.getIngredientsList() ?: listOf())
    }

    override fun recipeSavedSuccessfully(recipe: Recipe) {
        newRecipeId = recipe.id

        ingredientsFragment = EditRecipeIngredientsFragment.newInstance(recipe)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, ingredientsFragment)
        transaction.commit()
    }

    override fun recipeRemovedSuccessfully() {
    }

    override fun onCreateOptionsMenu(_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_next, _menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.app_bar_next) {
            val intent = Intent()
            intent.setClass(this, EditRecipeDescActivity::class.java)
            startActivityWithLeftAnimation(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
