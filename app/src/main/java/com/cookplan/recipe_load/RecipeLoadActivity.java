package com.cookplan.recipe_load;

import android.os.Bundle;

import com.cookplan.BaseActivity;
import com.cookplan.R;

public class RecipeLoadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_load);
        setNavigationArrow();
        setTitle("Загрузка нового рецепта");
    }
}
