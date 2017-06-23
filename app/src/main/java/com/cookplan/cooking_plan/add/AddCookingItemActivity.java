package com.cookplan.cooking_plan.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.add_ingredient_view.AddIngredientViewFragment;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.recipe.list.select.RecipeSelectListActivity;
import com.cookplan.utils.Constants;
import com.cookplan.views.ChooseCookingTimeView;

import org.joda.time.DateTime;

import static com.cookplan.recipe.list.select.RecipeSelectListActivity.SELECTED_RECIPE_KEY;

public class AddCookingItemActivity extends BaseActivity implements AddCookingItemView {

    private AddCookingItemPresenter presenter;

    public static final int SELECT_RECIPE_REQUEST = 21;
    public static final String IS_RECIPE_NEEDED_TO_ADD_KEY = "IS_RECIPE_NEEDED_TO_ADD_KEY";

    private AddIngredientViewFragment ingredientFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cooking_item);
        setNavigationArrow();
        setTitle(getString(R.string.add_cooking_item_title));

        presenter = new AddCookingItemPresenterImpl(this);
        boolean isRecipeAdding = getIntent().getBooleanExtra(IS_RECIPE_NEEDED_TO_ADD_KEY, false);
        View chooseRecipeLayout = findViewById(R.id.choose_recipe_layout);
        View addIngredientsView = findViewById(R.id.fragment_container);
        if (isRecipeAdding) {
            chooseRecipeLayout.setVisibility(View.VISIBLE);
            addIngredientsView.setVisibility(View.GONE);

            chooseRecipeLayout.setOnClickListener(v -> {
                startChooseRecipeActivity();
            });
            startChooseRecipeActivity();
        } else {
            chooseRecipeLayout.setVisibility(View.GONE);
            addIngredientsView.setVisibility(View.VISIBLE);

            ingredientFragment = AddIngredientViewFragment.newInstance(false, false);
            ingredientFragment.setEventListener(new AddIngredientViewFragment.AddIngredientViewEventListener() {
                @Override
                public void onSuccessSaveIngredient(Ingredient ingredient) {
                    ChooseCookingTimeView chooseCookingTimeView = (ChooseCookingTimeView) findViewById(R.id.choose_time_view);
                    Constants.TypeOfTime selectedTime = chooseCookingTimeView.getSelectedTime();
                    DateTime choosenDate = chooseCookingTimeView.getSelectedDate();
                    if (selectedTime != null && choosenDate != null && presenter != null) {
                        presenter.saveIngredientToCookingPlan(
                                ingredient,
                                choosenDate.withHourOfDay(selectedTime.getHour())
                                        .withMinuteOfHour(selectedTime.getMinute()));
                    }
                }

                @Override
                public void onErrorSaveIngredient(String error) {
                }
            });
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, ingredientFragment);
            transaction.commit();
        }
    }

    private void startChooseRecipeActivity() {
        Intent intent = new Intent(this, RecipeSelectListActivity.class);
        startActivityForResultWithLeftAnimation(intent, SELECT_RECIPE_REQUEST);
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
            ChooseCookingTimeView chooseCookingTimeView = (ChooseCookingTimeView) findViewById(R.id.choose_time_view);
            Constants.TypeOfTime selectedTime = chooseCookingTimeView.getSelectedTime();
            DateTime choosenDate = chooseCookingTimeView.getSelectedDate();
            if (selectedTime != null && choosenDate!= null) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                if (ingredientFragment != null) {
                    ingredientFragment.saveButtonClick();
                } else if (presenter != null) {
                    TextView recipeTitle = (TextView) findViewById(R.id.recipe_textView);
                    Recipe recipe = (Recipe) recipeTitle.getTag();
                    if (recipe != null) {
                        presenter.saveRecipeToCookingPlan(
                                recipe,
                                choosenDate.withHourOfDay(selectedTime.getHour())
                                        .withMinuteOfHour(selectedTime.getMinute()));
                    }
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_RECIPE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Recipe selectedRecipe = (Recipe) data.getSerializableExtra(SELECTED_RECIPE_KEY);
                if (selectedRecipe != null) {
                    TextView recipeTitle = (TextView) findViewById(R.id.recipe_textView);
                    recipeTitle.setTag(selectedRecipe);
                    recipeTitle.setText(selectedRecipe.getName());
                }
            }
        }
    }

    @Override
    public void setSuccessfullItemSaving() {
        finish();
    }

    @Override
    public void setError(String error) {
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, error, Snackbar.LENGTH_LONG).show();
        }
    }
}
