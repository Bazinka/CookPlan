package com.cookplan.geofence;

import com.cookplan.models.Company;

/**
 * Created by DariaEfimova on 11.05.17.
 */

public interface GeoFencePresenter {

    void setGeoFence(Company company, float radius, long days);

    void onStop();

}
