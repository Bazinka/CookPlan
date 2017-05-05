package com.cookplan.recipe_new.add_ingredients;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.add_ingredient_view.AddIngredientViewFragment;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class EditRecipeIngredientsActivity extends BaseActivity implements EditRecipeIngredientsView {

    public static final String RECIPE_OBJECT_KEY = "new_recipe_name";

    private EditRecipeInrgedientsAdapter adapter;
    private EditRecipeIngredientsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe_ingredients);
        setNavigationArrow();

        Recipe recipe = (Recipe) getIntent().getSerializableExtra(RECIPE_OBJECT_KEY);
        if (recipe == null) {
            finish();
        }
        setTitle(getString(R.string.add_recipe_second_screen_title) + " " + recipe.getName());

        AddIngredientViewFragment fragment = AddIngredientViewFragment.newInstance(recipe, false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new EditRecipeInrgedientsAdapter(this, new ArrayList<>(), ingredient -> {
            if (presenter != null) {
                presenter.removeIngredient(ingredient);
            }
        });
        recyclerView.setAdapter(adapter);

        presenter = new EditRecipeIngredientsPresenterImpl(this, recipe);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getAsyncIngredientList();
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
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.done_menu, _menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_done) {
            //TODO: saving data
            //            Intent intent = new Intent(this, MainActivity.class);
            //            startActivityWithLeftAnimation(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setIngredientList(List<Ingredient> ingredientList) {
        adapter.updateItems(ingredientList);
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
