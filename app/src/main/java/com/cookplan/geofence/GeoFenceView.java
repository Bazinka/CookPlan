package com.cookplan.geofence;

/**
 * Created by DariaEfimova on 11.05.17.
 */

public interface GeoFenceView {

    void setGeofenceAddedSuccessfull();

    void setGeoFenceError(int errorResourceId);

    void setGeofenceRemovedSuccessfull();
}
