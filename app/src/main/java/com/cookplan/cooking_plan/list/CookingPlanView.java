package com.cookplan.cooking_plan.list;

import com.cookplan.models.Recipe;

import java.util.List;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface CookingPlanView {

    void setErrorToast(String error);

    void setCookingList(List<Recipe> ingredientList);

    void setEmptyView();
}
