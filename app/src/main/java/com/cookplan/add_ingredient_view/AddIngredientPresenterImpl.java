package com.cookplan.add_ingredient_view;

import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.MeasureUnit;
import com.cookplan.models.Product;
import com.cookplan.models.ProductCategory;
import com.cookplan.models.Recipe;
import com.cookplan.models.ShopListStatus;
import com.cookplan.providers.IngredientProvider;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.impl.IngredientProviderImpl;
import com.cookplan.providers.impl.ProductProviderImpl;
import com.cookplan.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class AddIngredientPresenterImpl implements AddIngredientPresenter {

    private AddIngredientView mainView;
    //    private DatabaseReference database;
    private Recipe recipe;
    private boolean isNeedToBuy;
    private double lastInputAmount;

    private ProductProvider productDataProvider;
    private IngredientProvider ingredientDataProvider;

    public AddIngredientPresenterImpl(AddIngredientView mainView) {
        this.mainView = mainView;
        ingredientDataProvider = new IngredientProviderImpl();
        productDataProvider = new ProductProviderImpl();
        isNeedToBuy = false;
    }

    @Override
    public void getAsyncProductList() {
        productDataProvider.getProductList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Product> products) {
                        if (mainView != null && mainView.isAddedToActivity()) {
                            mainView.setProductsList(products);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null && e instanceof CookPlanError) {
                            mainView.setErrorToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void saveIngredient(Product product, double amount, MeasureUnit newMeasureUnit) {
        if (product != null) {
            productDataProvider.increaseCountUsages(product)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null) {
                                mainView.setErrorToast(e.getMessage());
                            }
                        }
                    });
            //save ingredient
            Ingredient ingredient = new Ingredient(null,
                                                   product.getName(),
                                                   product,
                                                   recipe != null ? recipe.getId() : null,
                                                   newMeasureUnit,
                                                   amount,
                                                   isNeedToBuy ? ShopListStatus.NEED_TO_BUY : ShopListStatus.NONE);
            ingredientDataProvider.createIngredient(ingredient)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Ingredient>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Ingredient ingredient) {
                            if (mainView != null) {
                                mainView.setSuccessSaveIngredient();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null && e instanceof CookPlanError) {
                                mainView.setErrorToast(e.getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void saveProductAndIngredient(ProductCategory category, String name, double amount, MeasureUnit measureUnit) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Map<MeasureUnit, Double> map = null;
            List<MeasureUnit> measureUnitList = Arrays.asList(MeasureUnit.values());
            if (measureUnit == MeasureUnit.KILOGRAMM) {
                map = Utils.getKilogramUnitMap();
                measureUnitList = Utils.getWeightUnitList();
            }
            if (measureUnit == MeasureUnit.LITRE) {
                map = Utils.getLitreUnitMap();
                measureUnitList = Utils.getVolumeUnitList();
            }
            List<MeasureUnit> mainMeasureUnitList = new ArrayList<>();
            mainMeasureUnitList.add(measureUnit);
            Product product = new Product(category, name, mainMeasureUnitList,
                                          measureUnitList, map, user.getUid());
            productDataProvider.createProduct(product)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Product>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Product product) {
                            saveIngredient(product, amount, measureUnit);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null && e instanceof CookPlanError) {
                                mainView.setErrorToast(e.getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void setIsNeedToBuy(boolean isNeedToBuy) {
        this.isNeedToBuy = isNeedToBuy;
    }

    @Override
    public boolean isNeedToBuy() {
        return isNeedToBuy;
    }
}
