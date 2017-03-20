package com.cookplan.recipe_new.add_basics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.cookplan.R;
import com.cookplan.recipe_new.EditRecipeBaseActicity;
import com.cookplan.recipe_new.add_ingredients.EditRecipeIngredientsActivity;

public class EditRecipeBasicsInfoActivity extends EditRecipeBaseActicity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe_basics);
        setNavigationArrow();
        setTitle(getString(R.string.add_recipe_first_screen_title));
    }

    @Override
    public void onMenuNextButtonClick() {
        EditText recipeEditText = (EditText) findViewById(R.id.recipe_name_edit_text);
        if (recipeEditText != null) {
            String text = recipeEditText.getText().toString();
            if (!text.isEmpty()) {
                //TODO: saving data
                Intent intent = new Intent(this, EditRecipeIngredientsActivity.class);
                intent.putExtra(RECIPE_NAME_KEY, text);
                startActivityWithLeftAnimation(intent);
                finish();
            } else {
                TextInputLayout recipeEditLayout = (TextInputLayout) findViewById(R.id.recipe_name_edit_layout);
                if (recipeEditLayout != null) {
                    recipeEditLayout.setError(getString(R.string.error_required_field));
                }

            }
        }
    }
}
