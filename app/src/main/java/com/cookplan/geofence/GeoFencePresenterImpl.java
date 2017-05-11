package com.cookplan.geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Company;
import com.cookplan.models.CookPlanError;
import com.cookplan.providers.CompanyProvider;
import com.cookplan.providers.impl.CompanyProviderImpl;
import com.cookplan.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    public void setGeoFence(List<Company> allCompanyList, List<Company> selectedCompanyList) {
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
                    for (Company allCompany : allCompanyList) {
                        boolean selected = false;
                        for (Company selectCompany : selectedCompanyList) {
                            if (allCompany.getId().equals(selectCompany.getId())) {
                                selected = true;
                                break;
                            }
                        }
                        allCompany.setAddedToGeoFence(selected);
                    }
                    updateCompanies(allCompanyList);
                    addGeoFence(selectedCompanyList);
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

    private void addGeoFence(List<Company> selectedCompanyList) {
        if (!selectedCompanyList.isEmpty()) {
            if (!mGoogleApiClient.isConnected()) {
                if (mainView != null) {
                    mainView.setGeoFenceError(R.string.geofence_error_not_connected);
                }
                return;
            }
            try {
                LocationServices.GeofencingApi.addGeofences(
                        mGoogleApiClient,
                        getGeofencingRequest(getGeoFenceList(selectedCompanyList)),
                        getGeofencePendingIntent()
                ).setResultCallback(status -> {
                    if (status.isSuccess()) {
                        RApplication.saveGeofenceModeTurnedOn(!selectedCompanyList.isEmpty());

                        if (mainView != null) {
                            mainView.setGeofenceAddedSuccessfull();
                        }
                    } else {
                        if (mainView != null) {
                            mainView.setGeoFenceError(GeofenceErrorMessages.getErrorString(status.getStatusCode()));
                        }
                    }
                });
            } catch (SecurityException securityException) {
                logSecurityException(securityException);
            }
        } else {
            RApplication.saveGeofenceModeTurnedOn(false);
            if (mainView != null) {
                mainView.setGeofenceAddedSuccessfull();
            }
        }
    }

    private GeofencingRequest getGeofencingRequest(List<Geofence> geoFenceList) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(geoFenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private void logSecurityException(SecurityException securityException) {
        if (mainView != null) {
            mainView.setGeoFenceError(R.string.geofence_error_permission_error);
        }
    }

    public void updateCompanies(List<Company> companies) {
        for (Company company : companies) {
            dataProvider.updateCompany(company)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Company>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Company company) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mainView != null && e instanceof CookPlanError) {
                                mainView.setGeoFenceError(R.string.error_update_object_to_database);
                            }
                        }
                    });
        }
    }

    private List<Geofence> getGeoFenceList(List<Company> companies) {
        ArrayList<Geofence> mGeofenceList = new ArrayList<>();
        for (Company company : companies) {

            mGeofenceList.add(new Geofence.Builder()
                                      // Set the request ID of the geofence. This is a string to identify this
                                      // geofence.
                                      .setRequestId(company.getName())
                                      .setCircularRegion(
                                              company.getLatitude(),
                                              company.getLongitude(),
                                              Constants.GEOFENCE_RADIUS_IN_METERS
                                      )
                                      .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                                      .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                                                  Geofence.GEOFENCE_TRANSITION_EXIT)
                                      .build());
        }
        return mGeofenceList;
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
