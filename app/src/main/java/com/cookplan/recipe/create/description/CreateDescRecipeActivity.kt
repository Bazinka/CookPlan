package com.cookplan.recipe.create.description

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.Recipe
import com.cookplan.recipe.edit.EditRecipePresenter
import com.cookplan.recipe.edit.EditRecipePresenterImpl
import com.cookplan.recipe.edit.EditRecipeView
import com.cookplan.recipe.edit.description.EditRecipeDescFragment

class CreateDescRecipeActivity : BaseActivity(), EditRecipeView {

    private var editRecipePresenter: EditRecipePresenter? = null
    private var fragment: EditRecipeDescFragment? = null
    private var recipeId: String? = null
    private var nameExample: String = String()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_desc_recipe)
        setNavigationArrow()

        setTitle(R.string.step2_description_title)

        if (!intent.hasExtra(RECIPE_ID_KEY)) {
            finish()
        } else {
            recipeId = intent.getStringExtra(RECIPE_ID_KEY)
            nameExample = intent.getStringExtra(RECIPE_EXAMPLE_NAME_KEY)

            fragment = EditRecipeDescFragment.newInstance()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()

            editRecipePresenter = EditRecipePresenterImpl(this)
        }
    }


    override fun onCreateOptionsMenu(_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_next, _menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.app_bar_next) {
            val contentDialogView = LayoutInflater.from(this).inflate(R.layout.create_recipe_name_dialog, null)
            val nameEditTextView = contentDialogView.findViewById<EditText>(R.id.recipe_name_edit_text)
            nameEditTextView.setText(nameExample)
            nameEditTextView.setSelection(nameEditTextView.text.length)
            AlertDialog.Builder(this)
                    .setView(contentDialogView)
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                        editRecipePresenter?.saveRecipe(
                                recipe = Recipe(id = recipeId ?: String(),
                                        name = nameEditTextView.text.toString(),
                                        desc = fragment?.getDescription() ?: String()))
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
                    .show()

            return true
        }
        return super.onOptionsItemSelected(item)
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

    override fun recipeSavedSuccessfully(recipe: Recipe) {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun recipeRemovedSuccessfully() {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            EditRecipeDescFragment.RC_IMAGE_PERMS -> {
                fragment?.requestPermissionsResult(grantResults)
            }
        }
    }

    companion object {

        val RECIPE_ID_KEY = "RECIPE_ID_KEY"
        val RECIPE_EXAMPLE_NAME_KEY = "RECIPE_EXAMPLE_NAME_KEY"
    }


}
