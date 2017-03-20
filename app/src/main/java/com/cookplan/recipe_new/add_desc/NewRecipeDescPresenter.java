package com.cookplan.recipe_new.add_desc;

import android.net.Uri;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public interface NewRecipeDescPresenter {

    public Uri getOutputImagePath();

    public void doOCR(String language);
}
