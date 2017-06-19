package com.cookplan.recipe.list.select;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cookplan.BaseActivity;
import com.cookplan.R;

public class RecipeSelectListActivity extends BaseActivity {

    public static final String SELECTED_RECIPE_KEY = "SELECTED_RECIPE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_select_list);
        setNavigationArrow();
        setTitle(R.string.choose_the_recipe);

        RecipeSelectListFragment fragment = RecipeSelectListFragment.newInstance();
        fragment.setListener(recipe -> {
            Intent intent = new Intent();
            intent.putExtra(SELECTED_RECIPE_KEY, recipe);
            setResult(RESULT_OK, intent);
            finish();
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
