package com.cookplan.recipe_view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;
import com.cookplan.recipe_new.add_info.EditRecipeInfoActivity;
import com.cookplan.recipe_steps.RecipeStepsViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewActivity extends BaseActivity implements RecipeView {

    public static final String RECIPE_OBJECT_KEY = "recipe_name";

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

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.recipe_view_fab);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(this, EditRecipeInfoActivity.class);
                intent.putExtra(EditRecipeInfoActivity.RECIPE_OBJECT_KEY, recipe);
                startActivityWithLeftAnimation(intent);
                finish();
            });
            if (user != null && recipe.getUserId().equals(user.getUid())) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.GONE);
            }
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            adapter = new RecipeViewInrgedientsAdapter(new ArrayList<>(), ingredient -> {
                if (presenter != null) {
                    presenter.saveSelectIngredientList(ingredient);
                }
            });
            recyclerView.setAdapter(adapter);

            presenter = new RecipeViewPresenterImpl(this, recipe);
            presenter.getIngredientList();

            TextView descTextView = (TextView) findViewById(R.id.description_body_textview);
            descTextView.setText(recipe.getDesc());

            ViewGroup stepByStepViewLayout = (ViewGroup) findViewById(R.id.step_by_step_button_layout);
            stepByStepViewLayout.setOnClickListener(v -> {
                Intent intent = new Intent(this, RecipeStepsViewActivity.class);
                //TODO: add extras to send data to activity
                intent.putExtra(RecipeStepsViewActivity.RECIPE_OBJECT_KEY, recipe);
                if (adapter != null) {
                    intent.putParcelableArrayListExtra(RecipeStepsViewActivity.INGREDIENT_LIST_OBJECT_KEY,
                                                       adapter.getIngredients());
                }
                startActivityWithLeftAnimation(intent);
                finish();
            });
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
    public void setIngredientSuccessfulUpdate(Ingredient ingredient) {
        String message = null;
        if (ingredient.getShopListStatus() == ShopListStatus.NEED_TO_BUY) {
            message = getString(R.string.ingr_added_to_shopping_list_title);
        } else {
            message = getString(R.string.ingr_removed_from_shopping_list_title);
        }
        Snackbar.make(findViewById(R.id.main_view),
                      ingredient.getName() + message,
                      Snackbar.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
