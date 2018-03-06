package com.cookplan.recipe.view_item

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.models.Ingredient
import com.cookplan.models.Recipe
import com.cookplan.models.ShopListStatus.NEED_TO_BUY
import com.cookplan.models.ShopListStatus.NONE
import com.cookplan.recipe.edit.EditRecipePresenter
import com.cookplan.recipe.edit.EditRecipePresenterImpl
import com.cookplan.recipe.edit.EditRecipeView
import com.cookplan.recipe.edit.description.DescImagesRecyclerViewAdapter
import com.cookplan.recipe.edit.description.EditRecipeDescActivity
import com.cookplan.recipe.edit.description.EditRecipeDescActivity.Companion.RECIPE_DESCRIPTION_IMAGES_KEY
import com.cookplan.recipe.edit.description.EditRecipeDescActivity.Companion.RECIPE_DESCRIPTION_KEY
import com.cookplan.recipe.edit.ingredients.EditRecipeIngredientsActivity
import com.cookplan.recipe.steps_mode.RecipeStepsViewActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class RecipeViewActivity : BaseActivity(), RecipeView, EditRecipeView {

    private var adapter: RecipeViewInrgedientsAdapter? = null
    private var isAllIngredientsChecked: Boolean = false
    private var viewPresenter: RecipeViewPresenter? = null
    private var editPresenter: EditRecipePresenter? = null

    private var imageListAdapter: DescImagesRecyclerViewAdapter? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_view)
        setNavigationArrow()

        if (!intent.hasExtra(RECIPE_OBJECT_KEY)) {
            finish()
        } else {
            val recipe = intent.getSerializableExtra(RECIPE_OBJECT_KEY) as Recipe

            val backImageView = findViewById<ImageView>(R.id.back_image_view)
            backImageView.setOnClickListener {
                onBackPressed()
            }
            setName(recipe.name)

            val recyclerView = findViewById<RecyclerView>(R.id.ingredients_recycler_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.layoutManager = LinearLayoutManager(this)

            adapter = RecipeViewInrgedientsAdapter { ingredient ->
                viewPresenter?.addIngredientToShoppingList(ingredient)
            }
            recyclerView.adapter = adapter

            viewPresenter = RecipeViewPresenterImpl(this, recipe)

            setDescriptionPart(recipe.desc, recipe.descImageUrls)

            val addToShopListButton = findViewById<Button>(R.id.add_shop_list_items_button)
            addToShopListButton.setOnClickListener {
                val progressBar = findViewById<View>(R.id.progress_bar_layout)
                progressBar.visibility = View.VISIBLE

                if (!isAllIngredientsChecked) {
                    isAllIngredientsChecked = true
                    viewPresenter?.changeIngredListShopStatus(adapter?.getIngredients() ?: listOf(), NEED_TO_BUY)
                } else {
                    isAllIngredientsChecked = false
                    viewPresenter?.changeIngredListShopStatus(adapter?.getIngredients() ?: listOf(), NONE)
                }
            }

            val fab = findViewById<FloatingActionButton>(R.id.step_by_step_fab)
            fab.setOnClickListener {
                startStepActivity()
            }
            val editNameLayout = findViewById<ViewGroup>(R.id.name_recipe_layout)
            editNameLayout.setOnClickListener {
                val contentDialogView = LayoutInflater.from(this).inflate(R.layout.name_edit_dialog_layout, null)
                val nameEditTextView = contentDialogView.findViewById<EditText>(R.id.recipe_name_edit_text)
                val nameTextView = findViewById<TextView>(R.id.name_recipe_textview)
                nameEditTextView.setText(nameTextView.text)

                AlertDialog.Builder(this)
                        .setView(contentDialogView)
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                            val text = nameEditTextView.text.toString()
                            if (!text.isEmpty()) {
                                setName(text)
                                val recipe = viewPresenter?.getRecipe()
                                recipe?.name = text
                                if (recipe != null) {
                                    editPresenter?.saveRecipe(recipe)
                                }
                            }
                        }
                        .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
                        .show()
            }

            val editDescriptionButton = findViewById<ImageView>(R.id.edit_description_image_view)
            editDescriptionButton.setOnClickListener {
                val intent = Intent(this, EditRecipeDescActivity::class.java)
                intent.putExtra(RECIPE_DESCRIPTION_KEY, viewPresenter?.getRecipe()?.desc)
                intent.putStringArrayListExtra(RECIPE_DESCRIPTION_IMAGES_KEY,
                        viewPresenter?.getRecipe()?.descImageUrls ?: arrayListOf<String>())
                startActivityForResultWithLeftAnimation(intent, CHANGE_DESCRIPTION_REQUEST)
            }

            val editIngredientsButton = findViewById<ImageView>(R.id.edit_ingredients_image_view)
            editIngredientsButton.setOnClickListener {
                val intent = Intent(this, EditRecipeIngredientsActivity::class.java)
                intent.putExtra(EditRecipeIngredientsActivity.RECIPE_OBJECT_KEY, viewPresenter?.getRecipe())
                startActivityWithLeftAnimation(intent)
            }
            val removeRecipeImageView = findViewById<ImageView>(R.id.remove_recipe_image_view)
            if (recipe.userId == FirebaseAuth.getInstance().currentUser?.uid) {
                removeRecipeImageView.visibility = VISIBLE
                removeRecipeImageView.setOnClickListener {
                    AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                            .setTitle(R.string.attention_title)
                            .setMessage(R.string.are_you_sure_remove_recipe)
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                findViewById<View>(R.id.progress_bar_layout)?.visibility = View.VISIBLE
                                editPresenter?.removeRecipe(recipe, adapter?.getIngredients() ?: listOf())
                            }
                            .setNegativeButton(android.R.string.cancel, null)
                            .show()
                }
            } else {
                removeRecipeImageView.visibility = GONE
            }

            editPresenter = EditRecipePresenterImpl(this)
        }
    }

    private fun setDescriptionPart(desc: String, imageIds: MutableList<String>) {
        val descTextView = findViewById<TextView>(R.id.description_body_textview)
        descTextView.text = desc

        val recyclerView = findViewById<RecyclerView>(R.id.image_list_recycler)
        recyclerView?.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = HORIZONTAL
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()

        imageListAdapter = DescImagesRecyclerViewAdapter(imageIds) {
            startStepActivity()
        }
        recyclerView?.adapter = imageListAdapter

        if (!imageIds.isEmpty()) {
            recyclerView?.visibility = View.VISIBLE
        } else {
            recyclerView?.visibility = View.GONE
        }
    }

    private fun startStepActivity() {
        val intent = Intent(this, RecipeStepsViewActivity::class.java)
        intent.putExtra(RecipeStepsViewActivity.RECIPE_OBJECT_KEY, viewPresenter?.getRecipe())
        intent.putParcelableArrayListExtra(RecipeStepsViewActivity.INGREDIENT_LIST_OBJECT_KEY,
                adapter?.getIngredients() as ArrayList<Ingredient>)
        startActivityWithLeftAnimation(intent)
    }

    private fun setName(name: String?) {
        val nameTextView = findViewById<TextView>(R.id.name_recipe_textview)
        nameTextView.text = name
    }

    override fun onStart() {
        super.onStart()
        viewPresenter?.getIngredientList()
    }

    override fun onStop() {
        super.onStop()
        viewPresenter?.onStop()
    }

    override fun setIngredientList(ingredientList: List<Ingredient>) {
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.INVISIBLE
        if (!ingredientList.isEmpty()) {
            isAllIngredientsChecked = ingredientList.none { it.shopListStatus !== NEED_TO_BUY }
            adapter?.updateItems(ingredientList)

            val ingredientListButton = findViewById<Button>(R.id.add_shop_list_items_button)
            ingredientListButton.setText(
                    if (isAllIngredientsChecked) {
                        R.string.remove_all_ingredients_from_shop_list_title
                    } else {
                        R.string.add_all_ingredients_to_shop_list_title
                    })
        } else {
            isAllIngredientsChecked = false
        }

        val nonEmptyIngredientsLayout = findViewById<View>(R.id.non_empty_ingredients_layout)
        nonEmptyIngredientsLayout.visibility = if (!ingredientList.isEmpty()) VISIBLE else GONE


        val emptyIngredientsLayout = findViewById<View>(R.id.empty_ingredients_layout)
        emptyIngredientsLayout.visibility = if (ingredientList.isEmpty()) VISIBLE else GONE
    }


    override fun setIngredientSuccessfulUpdate(ingredient: Ingredient) {
        //        adapter.notifyDataSetChanged();
    }

    override fun showProgressBar() {
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.VISIBLE

    }

    override fun hideProgressBar() {
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.GONE
    }

    override fun ingredListChangedShoplistStatus(isRemoved: Boolean) {
        val mainView = findViewById<View>(R.id.snackbar_layout)
        if (mainView != null) {
            val snackbar: Snackbar = if (isRemoved) {
                Snackbar.make(mainView, "Продукты успешно удалены из списка покупок", Snackbar.LENGTH_LONG)
            } else {
                Snackbar.make(mainView, "Продукты успешно добавлены в список покупок", Snackbar.LENGTH_LONG)
                        .setAction("Открыть список покупок") {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }

            }
            snackbar.show()
        }
        val progressBar = findViewById<View>(R.id.progress_bar_layout)
        progressBar.visibility = View.INVISIBLE

    }

    override fun setErrorToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun recipeSavedSuccessfully(recipe: Recipe) {
        setName(recipe.name)
        setDescriptionPart(recipe.desc, recipe.descImageUrls)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int,
                                         data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CHANGE_DESCRIPTION_REQUEST -> {
                    val recipe = viewPresenter?.getRecipe()
                    val text = data?.getStringExtra(CHANGE_DESCRIPTION_KEY) ?: getString(R.string.recipe_desc_is_not_needed_title)
                    recipe?.desc = text

                    recipe?.descImageUrls = data?.getStringArrayListExtra(CHANGE_DESCRIPTION_IMAGES_KEY) ?: arrayListOf()
                    if (recipe != null) {
                        editPresenter?.saveRecipe(recipe)
                        setDescriptionPart(recipe.desc, recipe.descImageUrls)
                    }
                }
            }
        }
    }

    override fun recipeRemovedSuccessfully() {
        finish()
    }

    companion object {
        val RECIPE_OBJECT_KEY = "recipe_name"
        val CHANGE_DESCRIPTION_KEY = "CHANGE_DESCRIPTION_KEY"
        val CHANGE_DESCRIPTION_IMAGES_KEY = "CHANGE_DESCRIPTION_IMAGES_KEY"
        val CHANGE_DESCRIPTION_REQUEST = 11
    }
}