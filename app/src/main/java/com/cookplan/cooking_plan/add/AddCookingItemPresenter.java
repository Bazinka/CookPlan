package com.cookplan.cooking_plan.add;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import java.util.Calendar;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface AddCookingItemPresenter {

    void saveRecipeToCookingPlan(Recipe recipe, int hour, int minute, long dateMillisek);

    void saveIngredientToCookingPlan(Ingredient ingredient, int hour, int minute, long dateMillisek);
}
