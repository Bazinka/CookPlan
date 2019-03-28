package com.cookplan.recipe.steps_mode

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe

class RecipeStepsViewActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_by_step_view)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val intent = intent
        val ingredients = intent.getParcelableArrayListExtra<Ingredient>(INGREDIENT_LIST_OBJECT_KEY)
        val recipe = intent.getSerializableExtra(RECIPE_OBJECT_KEY) as Recipe

        val recyclerView = findViewById<RecyclerView>(R.id.ingredients_recycler_view)
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()

        val adapter = RecipeStepsInrgedientsAdapter(ingredients)
        recyclerView.adapter = adapter

        val presenter: RecipeStepsPresenter = RecipeStepsPresenterImpl()

        val viewPager = findViewById<ViewPager>(R.id.steps_recipe_viewpager)
        viewPager.adapter = RecipeStepsPagerAdapter(presenter.getRecipeSteps(recipe))
    }

    companion object {

        val RECIPE_OBJECT_KEY = "RECIPE_OBJECT_KEY"
        val INGREDIENT_LIST_OBJECT_KEY = "INGREDIENT_LIST_OBJECT_KEY"
    }
}
