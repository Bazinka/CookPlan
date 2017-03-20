package com.cookplan.recipe_new;

import android.view.Menu;
import android.view.MenuItem;

import com.cookplan.BaseActivity;
import com.cookplan.R;

/**
 * Created by DariaEfimova on 18.03.17.
 */

abstract public class EditRecipeBaseActicity extends BaseActivity {

    protected final String RECIPE_NAME_KEY = "new_recipe_name";
    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.add_recipe_next_menu, _menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_next) {
            onMenuNextButtonClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void onMenuNextButtonClick();
}
