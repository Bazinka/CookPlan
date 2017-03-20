package com.cookplan.recipe_new.add_ingredients;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cookplan.R;
import com.cookplan.recipe_new.EditRecipeBaseActicity;
import com.cookplan.recipe_new.add_desc.NewRecipeDescActivity;

import java.util.ArrayList;

public class EditRecipeIngridientsActivity extends EditRecipeBaseActicity implements EditRecipeIngridientsView {

    private EditRecipeIngridientsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe_ingridients);
        setNavigationArrow();
        String name = getIntent().getStringExtra(RECIPE_NAME_KEY);
        setTitle(getString(R.string.add_recipe_second_screen_title) + " " + name);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        IngredientListRecyclerAdapter adapter = new IngredientListRecyclerAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        presenter = new EditRecipeIngridientsPresenterImpl();
    }

    @Override
    public void onMenuNextButtonClick() {
        //TODO: saving data
        Intent intent = new Intent(this, NewRecipeDescActivity.class);
        startActivityWithLeftAnimation(intent);
        finish();
    }
}
