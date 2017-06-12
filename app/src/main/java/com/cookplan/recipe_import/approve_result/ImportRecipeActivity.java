package com.cookplan.recipe_import.approve_result;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Product;
import com.cookplan.models.Recipe;
import com.cookplan.recipe_view.RecipeViewActivity;

import java.util.List;
import java.util.Map;

public class ImportRecipeActivity extends BaseActivity implements ImportRecipeView {

    public static final String URL_TO_IMPORT_KEY = "URL_TO_IMPORT_KEY";

    private ImportRecipePresenter presenter;
    private ApproveIngredientsRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_recipe);
        setNavigationArrow();
        setTitle(getString(R.string.import_recipe_title));
        setSubTitle("Подтверждение правильности рецепта после импорта");
        presenter = new ImportRecipePresenterImpl(this);

        String url = getIntent().getStringExtra(URL_TO_IMPORT_KEY);

        if (presenter != null) {
            presenter.importRecipeFromUrl(url);
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.approve_ingredients_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setImportResult(Recipe recipe, Map<String, List<Ingredient>> recipeToingredientsMap) {
        if (presenter == null || recipeToingredientsMap == null || recipeToingredientsMap.size() == 0) {
            finish();
        }
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.approve_ingredients_recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        adapter = new ApproveIngredientsRecyclerAdapter(
                recipe,
                recipeToingredientsMap,
                presenter.getAllProductsList(),
                new ApproveIngredientsRecyclerAdapter.ApproveIngredientsEventListener() {
                    @Override
                    public void allItemsDone() {
                        startNewRecipeActivity(adapter.getRecipe());
                    }

                    @Override
                    public void onRecipeSaveEvent(Recipe recipe) {
                        if (presenter != null) {
                            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                            progressBar.setVisibility(View.VISIBLE);
                            presenter.saveRecipe(recipe);
                        }
                    }


                    @Override
                    public void onIngredientSaveEvent(Product product) {

                    }

                    @Override
                    public void onIngredientSaveEvent(String key, Ingredient ingredient) {

                    }
                },
                this);
        recyclerView.setAdapter(adapter);
    }

    private void startNewRecipeActivity(Recipe recipe) {
        Intent intent = new Intent(this, RecipeViewActivity.class);
        intent.putExtra(RecipeViewActivity.RECIPE_OBJECT_KEY, recipe);
        startActivityWithLeftAnimation(intent);
        finish();
    }

    @Override
    public void setError(String s) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, s, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void setRecipeSavedSuccessfully(String recipeId) {
        if (adapter != null) {
            adapter.setIdToRecipe(recipeId);
        }
    }
}
