package com.cookplan.companies.list;

import com.cookplan.models.Company;
import com.cookplan.models.CookPlanError;
import com.cookplan.providers.CompanyProvider;
import com.cookplan.providers.impl.CompanyProviderImpl;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class CompanyListPresenterImpl implements CompanyListPresenter {

    private CompanyListView mainView;
    private CompanyProvider dataProvider;
    private CompositeDisposable disposables;

    public CompanyListPresenterImpl(CompanyListView mainView) {
        this.mainView = mainView;
        this.dataProvider = new CompanyProviderImpl();
        disposables = new CompositeDisposable();
    }


    @Override
    public void onStop() {
        disposables.clear();
    }

    @Override
    public void getUsersCompanyList() {
        disposables.add(
                dataProvider.getUsersCompanyList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Company>>() {

                            @Override
                            public void onNext(List<Company> companies) {
                                if (mainView != null) {
                                    if (companies.size() != 0) {
                                        mainView.setCompanyList(companies);
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
    public void removeCompany(Company company) {
        dataProvider.removeCompany(company)
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
                        if (mainView != null && e instanceof CookPlanError) {
                            mainView.setErrorToast(e.getMessage());
                        }
                    }
                });
    }
}
