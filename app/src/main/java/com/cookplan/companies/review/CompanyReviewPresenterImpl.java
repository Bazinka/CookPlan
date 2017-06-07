package com.cookplan.companies.review;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cookplan.R;
import com.cookplan.geofence.GeofenceErrorMessages;
import com.cookplan.geofence.responce.GeofenceTransitionsIntentService;
import com.cookplan.models.Company;
import com.cookplan.models.CookPlanError;
import com.cookplan.providers.CompanyProvider;
import com.cookplan.providers.impl.CompanyProviderImpl;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DariaEfimova on 07.06.17.
 */

public class CompanyReviewPresenterImpl implements CompanyReviewPresenter,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private CompanyProvider dataProvider;
    private CompanyReviewView mainView;
    private Company company;

    protected GoogleApiClient mGoogleApiClient;
    private PendingIntent mGeofencePendingIntent;
    private Context context;


    public CompanyReviewPresenterImpl(CompanyReviewView mainView, Company company, Context context) {
        this.mainView = mainView;
        this.company = company;
        this.context = context;
        dataProvider = new CompanyProviderImpl();

        mGeofencePendingIntent = null;
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onStart() {
        buildGoogleApiClient();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("GeoFencePresenterImpl", "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("GeoFencePresenterImpl", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i("GeoFencePresenterImpl", "Connection suspended");
    }

    @Override
    public void isCompanyAddedToGeoFence() {
        if (company != null) {
            dataProvider.getCompanyById(company.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Company>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Company company) {
                            if (mainView != null && company.getId() != null) {
                                mainView.setAddedToGeoFence(company.isAddedToGeoFence());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }
                    });
        }
    }

    @Override
    public void removeGeoFence(Company company) {
        if (!mGoogleApiClient.isConnected()) {
            if (mainView != null) {
                mainView.setError(R.string.geofence_error_not_connected);
            }
            return;
        }
        try {
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    getGeofencePendingIntent()
            ).setResultCallback(status -> {
                if (status.isSuccess()) {
                    company.setAddedToGeoFence(false);
                    dataProvider.updateCompany(company)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Company>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Company company) {
                                    if (mainView != null) {
                                        if (!company.isAddedToGeoFence()) {
                                            mainView.setGeofenceRemovedSuccessfull();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (mainView != null && e instanceof CookPlanError) {
                                        mainView.setError(R.string.error_update_object_to_database);
                                    }
                                }
                            });
                } else {
                    if (mainView != null) {
                        mainView.setError(GeofenceErrorMessages.getErrorString(status.getStatusCode()));
                    }
                }
            });
        } catch (SecurityException securityException) {
            if (mainView != null) {
                mainView.setError(R.string.geofence_error_permission_error);
            }
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
