package com.cookplan.recipe_steps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import java.util.List;

public class RecipeStepsViewActivity extends BaseActivity {

    public static final String RECIPE_OBJECT_KEY = "RECIPE_OBJECT_KEY";
    public static final String INGREDIENT_LIST_OBJECT_KEY = "INGREDIENT_LIST_OBJECT_KEY";

    private RecipeStepsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        List<Ingredient> ingredients = intent.getParcelableArrayListExtra(INGREDIENT_LIST_OBJECT_KEY);
        Recipe recipe = (Recipe) intent.getSerializableExtra(RECIPE_OBJECT_KEY);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecipeStepsInrgedientsAdapter adapter = new RecipeStepsInrgedientsAdapter(ingredients);
        recyclerView.setAdapter(adapter);

        presenter = new RecipeStepsPresenterImpl();

        ViewPager viewPager = (ViewPager) findViewById(R.id.steps_recipe_viewpager);
        viewPager.setAdapter(new RecipeStepsPagerAdapter(presenter.getRecipeSteps(recipe), this));
    }
}
