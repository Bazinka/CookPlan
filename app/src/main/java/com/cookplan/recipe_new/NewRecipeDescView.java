package com.cookplan.recipe_new;

/**
 * Created by DariaEfimova on 16.03.17.
 */

public interface NewRecipeDescView {
    public void setErrorToSnackBar(String error);

    public void setAsyncErrorToSnackBar(String error);


    public void setErrorToast(String error);

    public void setAsyncTextResult(String result);
}
