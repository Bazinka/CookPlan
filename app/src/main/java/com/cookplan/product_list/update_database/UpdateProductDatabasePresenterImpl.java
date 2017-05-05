package com.cookplan.product_list.update_database;


import com.cookplan.models.CookPlanError;
import com.cookplan.models.Product;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.impl.ProductProviderImpl;
import com.cookplan.utils.FillProductDatabaseProvider;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class UpdateProductDatabasePresenterImpl implements UpdateProductDatabasePresenter {

    private UpdateProductDatabaseView mainView;
    private ProductProvider dataProvider;
    private CompositeDisposable disposables;

    public UpdateProductDatabasePresenterImpl(UpdateProductDatabaseView mainView) {
        this.mainView = mainView;
        this.dataProvider = new ProductProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void updateProductDatabase() {
        disposables.add(
                dataProvider.getProductList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Product>>() {

                            @Override
                            public void onNext(List<Product> allExistedProducts) {
                                updateDatabase(allExistedProducts);
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (mainView != null && e instanceof CookPlanError) {
                                    mainView.setError(e.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
    }

    private void updateDatabase(List<Product> allExistedProducts) {
        List<Product> newProductList = FillProductDatabaseProvider.getNewProductList();
        final int[] count = {0};
        for (Product newProduct : newProductList) {
            for (Product oldProduct : allExistedProducts) {
                if (oldProduct.getRusName() != null && newProduct.getRusName() != null) {
                    if (oldProduct.getRusName().equals(newProduct.getRusName())) {
                       oldProduct.setEngName(newProduct.getEngName());

                        dataProvider.updateProductNames(oldProduct)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Product>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Product product) {
                                        count[0]++;
                                        if (count[0] == newProductList.size()) {
                                            if (mainView != null) {
                                                mainView.setSuccessEnding();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        count[0]++;
                                        if (mainView != null && e instanceof CookPlanError) {
                                            mainView.setError("error with " + oldProduct.toStringName() + ": " + e.getMessage());
                                        }
                                    }
                                });
                    }
                }
            }
        }
    }

    @Override
    public void onStop() {
        disposables.clear();
    }
}