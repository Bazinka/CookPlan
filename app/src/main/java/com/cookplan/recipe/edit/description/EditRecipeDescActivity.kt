package com.cookplan.recipe.edit.description

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.recipe.view_item.RecipeViewActivity

class EditRecipeDescActivity : BaseActivity() {

    private var fragment: EditRecipeDescFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe_description)
        setNavigationArrow()

        setTitle(R.string.recipe_description_title)

        if (intent.hasExtra(RECIPE_DESCRIPTION_KEY)) {
            val desc = intent.getStringExtra(RECIPE_DESCRIPTION_KEY)

            fragment = EditRecipeDescFragment.newInstance(desc)
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
            val intent = Intent()
            intent.putExtra(RecipeViewActivity.CHANGE_DESCRIPTION_KEY, fragment?.getDescription())
            setResult(Activity.RESULT_OK, intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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
        val RECIPE_DESCRIPTION_KEY = "RECIPE_DESCRIPTION_KEY"
    }
}
