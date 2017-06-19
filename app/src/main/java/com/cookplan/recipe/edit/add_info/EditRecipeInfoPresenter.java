package com.cookplan.recipe.edit.add_info;

import android.net.Uri;

import com.cookplan.models.Recipe;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public interface EditRecipeInfoPresenter {

    public Uri getOutputImagePath();

    public void doOCR(String language);

    public void saveRecipe(Recipe recipe, String newName, String newDesc);
}
