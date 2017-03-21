package com.cookplan.recipe_new.add_ingredients;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.recipe_new.EditRecipeBaseActicity;
import com.cookplan.recipe_new.add_desc.NewRecipeDescActivity;

import java.util.ArrayList;
import java.util.List;

public class EditRecipeIngredientsActivity extends EditRecipeBaseActicity implements EditRecipeIngredientsView {

    private ShoppingListRecyclerAdapter adapter;
    private EditRecipeIngredientsPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe_ingredients);
        setNavigationArrow();
        String name = getIntent().getStringExtra(RECIPE_NAME_KEY);
        setTitle(getString(R.string.add_recipe_second_screen_title) + " " + name);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shopping_list_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ShoppingListRecyclerAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        presenter = new EditRecipeIngredientsPresenterImpl(this);
        presenter.getAsyncIngredientList();
    }

    @Override
    public void onMenuNextButtonClick() {
        //TODO: saving data
        Intent intent = new Intent(this, NewRecipeDescActivity.class);
        startActivityWithLeftAnimation(intent);
        finish();
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
