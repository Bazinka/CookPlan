package com.cookplan.shopping_list.add_from_notify;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class ToShopListFromNotifyActivity extends BaseActivity implements ToShopListFromNotifyView {

    public static final String RECIPE_IDS_KEY = "RECIPE_IDS_KEY";
    public static final String INGREDIENTS_IDS_KEY = "INGREDIENTS_IDS_KEY";

    private ToShopListRecipesRecyclerAdapter adapter;
    private ToShopListFromNotifyPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_shop_list_from_notify);
        setTitle(getString(R.string.add_to_shop_list_from_notify_title));

        ArrayList<String> recipeIdsList = getIntent().getStringArrayListExtra(RECIPE_IDS_KEY);
        ArrayList<String> ingredientsIdsList = getIntent().getStringArrayListExtra(INGREDIENTS_IDS_KEY);
        if (recipeIdsList == null && ingredientsIdsList == null) {
            finish();
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.add_from_notify_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ToShopListRecipesRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        presenter = new ToShopListFromNotifyPresenterImpl(this);
        presenter.getItems(recipeIdsList, ingredientsIdsList);
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
            //TODO: доделать сохранение добавления в список покупок
            List<Ingredient> selectedIngredients = adapter.getSelectedIngredList();
            if (presenter != null) {
                presenter.setIngredientsNeedToBuy(selectedIngredients);
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setError(String error) {
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void setResults(Recipe recipe, List<Ingredient> ingredientList) {
        if (adapter != null) {
            adapter.addItem(recipe, ingredientList);
        }
    }
}
