package com.cookplan.recipe.list.main;

import com.cookplan.models.Recipe;
import com.cookplan.recipe.list.BaseRecipeListPresenter;

import org.joda.time.DateTime;

/**
 * Created by DariaEfimova on 21.03.17.
 */

public interface MainRecipeListPresenter extends BaseRecipeListPresenter {

    void addRecipeToCookPlan(Recipe recipe, DateTime date);

    void removeRecipeFromCookPlan(Recipe recipe);
}
