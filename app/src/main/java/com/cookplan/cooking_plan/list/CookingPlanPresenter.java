package com.cookplan.cooking_plan.list;

import com.cookplan.models.Recipe;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface CookingPlanPresenter {

    void getCookingPlan();

    void removeRecipe(Recipe recipe);

    void onStop();
}
