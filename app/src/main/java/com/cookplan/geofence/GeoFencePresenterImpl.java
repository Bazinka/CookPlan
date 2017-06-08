package com.cookplan.geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cookplan.R;
import com.cookplan.geofence.responce.GeofenceTransitionsIntentService;
import com.cookplan.models.Company;
import com.cookplan.models.CookPlanError;
import com.cookplan.providers.CompanyProvider;
import com.cookplan.providers.impl.CompanyProviderImpl;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.cookplan.utils.Constants.DAYS_TO_MILLISECONDS;

/**
 * Created by DariaEfimova on 11.05.17.
 */

public class GeoFencePresenterImpl implements GeoFencePresenter,
        ConnectionCallbacks, OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;
    private PendingIntent mGeofencePendingIntent;

    private CompanyProvider dataProvider;
    private GeoFenceView mainView;
    private Context context;

    public GeoFencePresenterImpl(Context context, GeoFenceView mainView) {
        this.mainView = mainView;
        this.context = context;
        dataProvider = new CompanyProviderImpl();

        mGeofencePendingIntent = null;
        buildGoogleApiClient();
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
    public void isCompanyAddedToGeoFence(Company company) {
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
                                if (company.isAddedToGeoFence()) {
                                    mainView.setGeofenceAddedSuccessfull();
                                } else {
                                    mainView.setGeofenceRemovedSuccessfull();
                                }
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
                mainView.setGeoFenceError(R.string.geofence_error_not_connected);
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
                                        mainView.setGeofenceRemovedSuccessfull();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (mainView != null && e instanceof CookPlanError) {
                                        mainView.setGeoFenceError(R.string.error_update_object_to_database);
                                    }
                                }
                            });
                } else {
                    if (mainView != null) {
                        mainView.setGeoFenceError(GeofenceErrorMessages.getErrorString(status.getStatusCode()));
                    }
                }
            });
        } catch (SecurityException securityException) {
            if (mainView != null) {
                mainView.setGeoFenceError(R.string.geofence_error_permission_error);
            }
        }
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
    public void setGeoFence(Company company, float radius, long days) {
        if (!mGoogleApiClient.isConnected()) {
            if (mainView != null) {
                mainView.setGeoFenceError(R.string.geofence_error_not_connected);
            }
            return;
        }
        try {
            addGeoFence(company, radius, days * DAYS_TO_MILLISECONDS);
        } catch (SecurityException securityException) {
            logSecurityException(securityException);
        }
    }

    private void addGeoFence(Company company, float radius, long millisec) {
        if (!mGoogleApiClient.isConnected()) {
            if (mainView != null) {
                mainView.setGeoFenceError(R.string.geofence_error_not_connected);
            }
            return;
        }
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(getGeoFence(company, radius, millisec)),
                    getGeofencePendingIntent()
            ).setResultCallback(status -> {
                if (status.isSuccess()) {
                    company.setAddedToGeoFence(true);
                    updateCompany(company);
                } else {
                    if (mainView != null) {
                        mainView.setGeoFenceError(GeofenceErrorMessages.getErrorString(status.getStatusCode()));
                    }
                }
            });
        } catch (SecurityException securityException) {
            logSecurityException(securityException);
        }
    }

    private GeofencingRequest getGeofencingRequest(Geofence geoFence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofence(geoFence);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private void logSecurityException(SecurityException securityException) {
        if (mainView != null) {
            mainView.setGeoFenceError(R.string.geofence_error_permission_error);
        }
    }

    private void updateCompany(Company company) {
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
                            if (company.isAddedToGeoFence()) {
                                mainView.setGeofenceAddedSuccessfull();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mainView != null && e instanceof CookPlanError) {
                            mainView.setGeoFenceError(R.string.error_update_object_to_database);
                        }
                    }
                });
    }

    private Geofence getGeoFence(Company company, float radius, long millisec) {
        Geofence geofence = new Geofence.Builder()
                .setRequestId(company.getId())
                .setCircularRegion(
                        company.getLatitude(),
                        company.getLongitude(),
                        radius
                )
                .setExpirationDuration(millisec)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();
        return geofence;
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
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
