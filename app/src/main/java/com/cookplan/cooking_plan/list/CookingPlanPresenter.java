package com.cookplan.cooking_plan.list;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface CookingPlanPresenter {

    void getCookingPlan();

    void removeRecipeFromCookingPlan(Recipe recipe);

    void removeIngredientFromCookingPlan(Ingredient ingredient);

    void onStop();
}
