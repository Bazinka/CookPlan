package com.cookplan.product_list;


import com.cookplan.models.CookPlanError;
import com.cookplan.models.Product;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.impl.ProductProviderImpl;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class ProductListPresenterImpl implements ProductListPresenter {

    private ProductListView mainView;
    private ProductProvider dataProvider;

    public ProductListPresenterImpl(ProductListView mainView) {
        this.mainView = mainView;
        this.dataProvider = new ProductProviderImpl();
    }

    @Override
    public void getProductList() {
        dataProvider.getProductList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

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
                });
    }
}