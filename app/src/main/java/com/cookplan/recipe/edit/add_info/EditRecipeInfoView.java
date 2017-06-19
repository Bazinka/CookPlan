package com.cookplan.recipe.edit.add_info;

import com.cookplan.models.Recipe;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public interface EditRecipeInfoView {
    public void setErrorToSnackBar(String error);

    public void setAsyncErrorToSnackBar(String error);


    public void setErrorToast(String error);

    public void setAsyncTextResult(String result);

    void showProgressBar();

    void hideProgressBar();

    public void setNextActivity(Recipe recipe);
}
