package com.cookplan.recipe_new.add_ingredients;

import android.content.Intent;
import android.os.Bundle;

import com.cookplan.R;
import com.cookplan.recipe_new.EditRecipeBaseActicity;
import com.cookplan.recipe_new.add_desc.NewRecipeDescActivity;

public class EditRecipeIngredientsActivity extends EditRecipeBaseActicity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe_ingredients);
        setNavigationArrow();
        String name = getIntent().getStringExtra(RECIPE_NAME_KEY);
        setTitle(getString(R.string.add_recipe_second_screen_title) + " " + name);
    }

    @Override
    public void onMenuNextButtonClick() {
        //TODO: saving data
        Intent intent = new Intent(this, NewRecipeDescActivity.class);
        startActivityWithLeftAnimation(intent);
        finish();
    }
}
