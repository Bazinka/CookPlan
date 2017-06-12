package com.cookplan.recipe_import.approve_result;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Product;
import com.cookplan.models.Recipe;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.RecipeProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.cookplan.providers.impl.ProductProviderImpl;
import com.cookplan.providers.impl.RecipeProviderImpl;
import com.cookplan.recipe_import.parser.Parser;
import com.cookplan.recipe_import.parser.ParserFactory;
import com.cookplan.recipe_import.parser.ParserResultListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 08.06.17.
 */

public class ImportRecipePresenterImpl implements ImportRecipePresenter {

    private ImportRecipeView mainView;
    private ProductProvider productDataProvider;
    private RecipeProvider recipeDataProvider;
    private IngredientProvider ingredientDataProvider;
    private CompositeDisposable disposables;
    private List<Product> allProductsList;

    public ImportRecipePresenterImpl(ImportRecipeView mainView) {
        this.mainView = mainView;
        productDataProvider = new ProductProviderImpl();
        recipeDataProvider = new RecipeProviderImpl();
        ingredientDataProvider = new IngredientProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void importRecipeFromUrl(String url) {
        getAsyncProductList();
        Parser parser = ParserFactory.createParser(url);
        if (parser != null) {
            parser.parceUrl(new ParserResultListener() {
                @Override
                public void onSuccess(Recipe recipe, Map<String, List<Ingredient>> ingredientList) {
                    if (mainView != null) {
                        mainView.setImportResult(recipe, ingredientList);
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

    @Override
    public List<Product> getAllProductsList() {
        return allProductsList;
    }

    private void getAsyncProductList() {
        disposables.add(
                productDataProvider.getProductList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Product>>() {
                            @Override
                            public void onNext(List<Product> products) {
                                disposables.clear();
                                allProductsList = new ArrayList<>(products);
                            }

                            @Override
                            public void onError(Throwable e) {
                                allProductsList = new ArrayList<>();
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        recipeDataProvider.createRecipe(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Recipe>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Recipe recipe) {
                        if (mainView != null) {
                            mainView.setRecipeSavedSuccessfully(recipe.getId());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null && e instanceof CookPlanError) {
                            mainView.setError(e.getMessage());
                        }
                    }
                });
    }
}
