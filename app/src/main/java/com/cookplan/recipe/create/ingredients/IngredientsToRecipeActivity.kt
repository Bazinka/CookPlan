package com.cookplan.recipe.create.ingredients

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.Recipe
import com.cookplan.recipe.create.description.CreateDescRecipeActivity
import com.cookplan.recipe.create.description.CreateDescRecipeActivity.Companion.RECIPE_ID_KEY
import com.cookplan.recipe.edit.EditRecipePresenter
import com.cookplan.recipe.edit.EditRecipePresenterImpl
import com.cookplan.recipe.edit.EditRecipeView
import com.cookplan.recipe.edit.ingredients.EditRecipeIngredientsFragment

class IngredientsToRecipeActivity : BaseActivity(), EditRecipeView {

    private var editRecipePresenter: EditRecipePresenter? = null
    private var newRecipeId: String? = null
    private var ingredientsFragment: EditRecipeIngredientsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe_ingredients)
        setNavigationArrow()
        setTitle(getString(R.string.step1_ingregients_title))

        editRecipePresenter = EditRecipePresenterImpl(this)
        editRecipePresenter?.saveRecipe(Recipe(name = getString(R.string.new_recipe_title)))
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
            intent.setClass(this, CreateDescRecipeActivity::class.java)
            intent.putExtra(RECIPE_ID_KEY, newRecipeId)
            intent.putExtra(CreateDescRecipeActivity.RECIPE_EXAMPLE_NAME_KEY, getExampleName())
            startActivityForResultWithLeftAnimation(intent, CREATE_DESC_RECIPE_REQUEST)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getExampleName(): String {
        var ingredList = ingredientsFragment?.getIngredientsList()
        var exampleName = getString(R.string.new_recipe_title)
        when (ingredList?.size) {
            1 -> exampleName = ingredList.get(0).name

            2 -> exampleName = ingredList.get(0).name + " " +
                    getString(R.string.and_recipe_name_separator) + " " +
                    ingredList.get(1).name

            3 -> exampleName = ingredList.get(0).name+
                    getString(R.string.second_name_separator) + " " +
                    ingredList.get(1).name + " " +
                    getString(R.string.and_recipe_name_separator) + " " +
                    ingredList.get(2).name
        }
        return exampleName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == CREATE_DESC_RECIPE_REQUEST) {
            setResult(Activity.RESULT_OK)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {

        val CREATE_DESC_RECIPE_REQUEST = 16
    }
}
