package com.cookplan.recipe_load;

import android.net.Uri;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public interface RecipeLoadPresenter {

    public Uri getOutputImagePath();

    public void doOCR(String language);
}
