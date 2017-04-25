package com.cookplan.recipe_view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.recipe_new.add_info.EditRecipeInfoActivity;
import com.cookplan.recipe_steps.RecipeStepsViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static com.cookplan.models.ShopListStatus.NEED_TO_BUY;

public class RecipeViewActivity extends BaseActivity implements RecipeView {

    public static final String RECIPE_OBJECT_KEY = "recipe_name";

    private RecipeViewPresenter presenter;
    private RecipeViewInrgedientsAdapter adapter;
    private Recipe recipe;
    private boolean isAllIngredientsChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationArrow();

        recipe = (Recipe) getIntent().getSerializableExtra(RECIPE_OBJECT_KEY);
        if (recipe == null) {
            finish();
        } else {
            setTitle(recipe.getName());

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            adapter = new RecipeViewInrgedientsAdapter(new ArrayList<>(), ingredient -> {
                if (presenter != null) {
                    presenter.addIngredientToShoppingList(ingredient);
                }
            });
            recyclerView.setAdapter(adapter);

            presenter = new RecipeViewPresenterImpl(this, recipe);

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

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_ingred_to_shop_list_fab);
            fab.setOnClickListener(view -> {
                if (!isAllIngredientsChecked) {
                    isAllIngredientsChecked = true;
                    setFabView();
                    for (Ingredient ingredient : adapter.getIngredients()) {
                        ingredient.setShopListStatus(NEED_TO_BUY);
                    }
                    adapter.notifyDataSetChanged();
                    if (presenter != null) {
                        presenter.addAllIngredientToShoppingList(adapter.getIngredients());
                    }
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getIngredientList();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void setIngredientList(List<Ingredient> ingredientList) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        if (!ingredientList.isEmpty()) {
            isAllIngredientsChecked = true;
            for (Ingredient ingredient : ingredientList) {
                if (ingredient.getShopListStatus() != NEED_TO_BUY) {
                    isAllIngredientsChecked = false;
                    break;
                }
            }

            if (adapter != null) {
                adapter.updateItems(ingredientList);
            }
        } else {
            isAllIngredientsChecked = false;
        }
        setFabView();
    }

    private void setFabView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_ingred_to_shop_list_fab);
        if (isAllIngredientsChecked) {
            fab.setImageResource(R.drawable.ic_shopping_list_white);
        } else {
            fab.setImageResource(R.drawable.ic_add_items_to_shop_list);
        }
    }

    @Override
    public void setIngredientSuccessfulUpdate(Ingredient ingredient) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.recipe_view_menu, _menu);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && recipe.getUserId().equals(user.getUid())) {
            _menu.findItem(R.id.app_bar_edit).setVisible(true);
        } else {
            _menu.findItem(R.id.app_bar_edit).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_edit) {
            Intent intent = new Intent(this, EditRecipeInfoActivity.class);
            intent.putExtra(EditRecipeInfoActivity.RECIPE_OBJECT_KEY, recipe);
            startActivityWithLeftAnimation(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
