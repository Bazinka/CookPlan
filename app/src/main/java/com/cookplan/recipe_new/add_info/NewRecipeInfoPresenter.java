package com.cookplan.recipe_new.add_info;

import android.net.Uri;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public interface NewRecipeInfoPresenter {

    public Uri getOutputImagePath();

    public void doOCR(String language);

    public void saveNewRecipe(String name, String desc);
}
