package com.cookplan.geofence.responce;

import com.cookplan.models.Company;
import com.cookplan.providers.CompanyProvider;
import com.cookplan.providers.ProviderFactory;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 29.05.17.
 */

public class GeofenceResponsePresenterImpl implements GeofenceResponsePresenter {
    private GeofenceResponseView mainView;
    private CompanyProvider dataProvider;

    public GeofenceResponsePresenterImpl(GeofenceResponseView mainView) {
        this.mainView = mainView;
        this.dataProvider = ProviderFactory.Companion.getCompanyProvider();
    }

    @Override
    public void getCompanyById(String companyId) {
        dataProvider.getCompanyById(companyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Company>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Company company) {
                        if (mainView != null && company.getId() != null) {
                            mainView.setCompany(company);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }
}
