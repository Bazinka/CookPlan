package com.cookplan.product_list;


import com.cookplan.models.CookPlanError;
import com.cookplan.models.Product;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.impl.ProductProviderImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class ProductListPresenterImpl implements ProductListPresenter {

    private ProductListView mainView;
    private ProductProvider dataProvider;
    private CompositeDisposable disposables;

    public ProductListPresenterImpl(ProductListView mainView) {
        this.mainView = mainView;
        this.dataProvider = new ProductProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getProductList() {
        disposables.add(
                dataProvider.getProductList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Product>>() {

                            @Override
                            public void onNext(List<Product> products) {
                                if (mainView != null) {
                                    if (products.size() != 0) {
                                        mainView.setProductList(products);
                                    } else {
                                        mainView.setEmptyView();
                                    }
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
                        }));
    }

    @Override
    public void onStop() {
        disposables.clear();
    }
}