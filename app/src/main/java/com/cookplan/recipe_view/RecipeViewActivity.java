package com.cookplan.recipe_view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewActivity extends BaseActivity implements RecipeView {

    public static final String RECIPE_OBJECT_KEY = "new_recipe_name";

    private RecipeViewPresenter presenter;
    private RecipeViewInrgedientsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationArrow();

        Recipe recipe = (Recipe) getIntent().getSerializableExtra(RECIPE_OBJECT_KEY);
        if (recipe == null) {
            finish();
        } else {
            setTitle(recipe.getName());

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.recipe_view_fab);
            fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
            recyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            adapter = new RecipeViewInrgedientsAdapter(new ArrayList<>());
            recyclerView.setAdapter(adapter);

            presenter = new RecipeViewPresenterImpl(this, recipe);
            presenter.getIngredientList();

            TextView descTextView = (TextView) findViewById(R.id.description_body_textview);
            descTextView.setText(recipe.getDesc());
        }
    }

    @Override
    public void setIngredientList(List<Ingredient> ingredientList) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        if (!ingredientList.isEmpty()) {
            if (adapter != null) {
                adapter.updateItems(ingredientList);
            }
        }
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
