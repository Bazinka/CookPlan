package com.cookplan.cooking_plan.add;

import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;

import org.joda.time.DateTime;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface AddCookingItemPresenter {

    void saveRecipeToCookingPlan(Recipe recipe, DateTime date);

    void saveIngredientToCookingPlan(Ingredient ingredient, DateTime date);
}
