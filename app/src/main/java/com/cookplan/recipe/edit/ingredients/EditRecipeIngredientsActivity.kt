package com.cookplan.recipe.edit.ingredients

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.Recipe

class EditRecipeIngredientsActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_recipe_ingredients)
        setNavigationArrow()

        if (intent.hasExtra(RECIPE_OBJECT_KEY)) {
            val recipe = intent.getSerializableExtra(RECIPE_OBJECT_KEY) as Recipe
            setTitle(getString(R.string.add_recipe_second_screen_title) + " " + recipe.name)

            val fragment = EditRecipeIngredientsFragment.newInstance(recipe)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()

        } else {
            finish()
        }
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

    companion object {
        val RECIPE_OBJECT_KEY = "new_recipe_name"
    }
}
