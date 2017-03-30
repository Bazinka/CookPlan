package com.cookplan.recipe_new.add_info;

import com.cookplan.models.Recipe;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public interface EditRecipeInfoView {
    public void setErrorToSnackBar(String error);

    public void setAsyncErrorToSnackBar(String error);


    public void setErrorToast(String error);

    public void setAsyncTextResult(String result);


    public void setNextActivity(Recipe recipe);
}
