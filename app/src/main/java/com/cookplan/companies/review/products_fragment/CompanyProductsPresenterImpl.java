package com.cookplan.companies.review.products_fragment;


import com.cookplan.models.Company;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.Product;
import com.cookplan.providers.ProductProvider;
import com.cookplan.providers.impl.ProductProviderImpl;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 20.03.17.
 */

public class CompanyProductsPresenterImpl implements CompanyProductsPresenter {

    private CompanyProductsView mainView;
    private ProductProvider dataProvider;
    private CompositeDisposable disposables;
    private Company company;

    public CompanyProductsPresenterImpl(CompanyProductsView mainView, Company company) {
        this.mainView = mainView;
        this.company = company;
        this.dataProvider = new ProductProviderImpl();
        disposables = new CompositeDisposable();
    }

    @Override
    public void getCompanyProductList() {
        if (company != null) {
            disposables.add(
                    dataProvider.getCompanyProductList(company.getId())
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
    }

    @Override
    public void onStop() {
        disposables.clear();
    }

    @Override
    public void deleteProductsFromCompany(List<Product> products) {
        for (Product product : products) {
            if (product.getCompanyIdList().contains(company.getId())) {
                product.getCompanyIdList().remove(company.getId());
            }
            dataProvider.updateProductCompanies(product)
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
        }
    }

    @Override
    public void updateProducts(List<Product> newProducts) {
        for (Product product : newProducts) {
            if (!product.getCompanyIdList().contains(company.getId())) {
                product.getCompanyIdList().add(company.getId());
                dataProvider.updateProductCompanies(product)
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
            }
        }
    }
}