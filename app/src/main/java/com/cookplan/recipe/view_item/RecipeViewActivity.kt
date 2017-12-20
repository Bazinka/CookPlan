package com.cookplan.recipe.view_item

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.main.ImageViewPagerAdapter
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.models.ShopListStatus.NEED_TO_BUY
import com.cookplan.models.ShopListStatus.NONE
import com.cookplan.recipe.edit.add_info.EditRecipeInfoActivity
import com.cookplan.recipe.steps_mode.RecipeStepsViewActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class RecipeViewActivity : BaseActivity(), RecipeView {

    private var adapter: RecipeViewInrgedientsAdapter? = null
    private var isAllIngredientsChecked: Boolean = false
    private var presenter: RecipeViewPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_view)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        setNavigationArrow()

        if (!intent.hasExtra(RECIPE_OBJECT_KEY)) {
            finish()
        } else {
            var reviewRecipe = intent.getSerializableExtra(RECIPE_OBJECT_KEY) as Recipe

            setTitle(reviewRecipe.name)

            val recyclerView = findViewById<RecyclerView>(R.id.ingredients_recycler_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.layoutManager = LinearLayoutManager(this)

            adapter = RecipeViewInrgedientsAdapter { ingredient ->
                presenter?.addIngredientToShoppingList(ingredient)
            }
            recyclerView.adapter = adapter

            presenter = RecipeViewPresenterImpl(this, reviewRecipe)

            val descTextView = findViewById<TextView>(R.id.description_body_textview)
            descTextView.text = reviewRecipe.desc

            val addToShopListButton = findViewById<Button>(R.id.add_shop_list_items_button)
            addToShopListButton.setOnClickListener {
                val progressBar = findViewById<View>(R.id.progress_bar_layout)
                progressBar.visibility = View.VISIBLE

                if (!isAllIngredientsChecked) {
                    isAllIngredientsChecked = true
                    presenter?.changeIngredListShopStatus(adapter?.getIngredients() ?: listOf(), NEED_TO_BUY)
                } else {
                    isAllIngredientsChecked = false
                    presenter?.changeIngredListShopStatus(adapter?.getIngredients() ?: listOf(), NONE)
                }
            }

            val fab = findViewById<FloatingActionButton>(R.id.step_by_step_fab)
            fab.setOnClickListener {
                val intent = Intent(this, RecipeStepsViewActivity::class.java)
                intent.putExtra(RecipeStepsViewActivity.RECIPE_OBJECT_KEY, reviewRecipe)
                intent.putParcelableArrayListExtra(RecipeStepsViewActivity.INGREDIENT_LIST_OBJECT_KEY,
                        adapter?.getIngredients() as ArrayList<Ingredient>)
                startActivityWithLeftAnimation(intent)
            }

            val viewPager = findViewById<View>(R.id.recipe_images_viewpager) as ViewPager
            if (reviewRecipe.imageUrls.isEmpty() ?: false) {
                viewPager.adapter = ImageViewPagerAdapter(reviewRecipe.imageUrls ?: listOf(), this)
                viewPager.visibility = View.VISIBLE
            } else {
                viewPager.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.getIngredientList()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun setIngredientList(ingredientList: List<Ingredient>) {
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.INVISIBLE
        if (!ingredientList.isEmpty()) {
            isAllIngredientsChecked = true
            for (ingredient in ingredientList) {
                if (ingredient.shopListStatus !== NEED_TO_BUY) {
                    isAllIngredientsChecked = false
                    break
                }
            }
            adapter?.updateItems(ingredientList)
            val button = findViewById<Button>(R.id.add_shop_list_items_button)
            button.setText(
                    if (isAllIngredientsChecked) {
                        R.string.remove_all_ingredients_from_shop_list_title
                    } else {
                        R.string.add_all_ingredients_to_shop_list_title
                    })
        } else {
            findViewById<View>(R.id.ingredient_content_card_view).visibility = View.GONE
            isAllIngredientsChecked = false
        }
    }


    override fun setIngredientSuccessfulUpdate(ingredient: Ingredient) {
        //        adapter.notifyDataSetChanged();
    }

    override fun ingredListChangedShoplistStatus(isRemoved: Boolean) {
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.INVISIBLE
        val mainView = findViewById<View>(R.id.snackbar_layout)
        if (mainView != null) {
            val snackbar: Snackbar
            if (isRemoved) {
                snackbar = Snackbar.make(mainView, "Продукты успешно удалены из списка покупок", Snackbar.LENGTH_LONG)
            } else {
                snackbar = Snackbar.make(mainView, "Продукты успешно добавлены в список покупок", Snackbar.LENGTH_LONG)
                        .setAction("Открыть список покупок") { view ->
                            setResult(Activity.RESULT_OK)
                            finish()
                        }

            }
            snackbar.show()
        }
        //        adapter.notifyDataSetChanged();
    }

    override fun setErrorToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.recipe_view_menu, _menu)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && presenter?.getRecipeObject()?.userId == user.uid) {
            _menu.findItem(R.id.app_bar_edit).isVisible = true
        } else {
            _menu.findItem(R.id.app_bar_edit).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.app_bar_edit) {
            val intent = Intent(this, EditRecipeInfoActivity::class.java)
            intent.putExtra(EditRecipeInfoActivity.RECIPE_OBJECT_KEY, presenter?.getRecipeObject())
            startActivityWithLeftAnimation(intent)
            finish();
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val RECIPE_OBJECT_KEY = "recipe_name"
    }
}
