package com.cookplan.cooking_plan.list;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import org.joda.time.LocalDate;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface CookingPlanPresenter {

    void getCookingPlan();

    void removeRecipeFromCookingPlan(LocalDate date, Recipe recipe);

    void removeIngredientFromCookingPlan(Ingredient ingredient);

    void onStop();
}
