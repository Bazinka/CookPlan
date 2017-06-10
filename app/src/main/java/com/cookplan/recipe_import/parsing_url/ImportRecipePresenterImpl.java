package com.cookplan.recipe_import.parsing_url;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.recipe_import.parser.Parser;
import com.cookplan.recipe_import.parser.ParserFactory;
import com.cookplan.recipe_import.parser.ParserResultListener;

import java.util.List;
import java.util.Map;

/**
 * Created by DariaEfimova on 08.06.17.
 */

public class ImportRecipePresenterImpl implements ImportRecipePresenter {

    private ImportRecipeView mainView;

    public ImportRecipePresenterImpl(ImportRecipeView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void importRecipeFromUrl(String url) {
        Parser parser = ParserFactory.createParser(url);
        if (parser != null) {
            parser.parceUrl(new ParserResultListener() {
                @Override
                public void onSuccess(Recipe recipe, Map<String, List<Ingredient>> ingredientList) {
                    if (mainView != null) {
                        mainView.setResult(recipe, ingredientList);
                    }
                }

                @Override
                public void onError(String error) {
                    if (mainView != null) {
                        mainView.setError(error);
                    }
                }
            });
        } else {
            if (mainView != null) {
                mainView.setError(
                        RApplication.getAppContext()
                                .getString(R.string.error_import_from_the_wrong_site_title));
            }
        }
    }
}
