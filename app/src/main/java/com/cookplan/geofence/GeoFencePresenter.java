package com.cookplan.geofence;

import com.cookplan.models.Company;

import java.util.List;

/**
 * Created by DariaEfimova on 11.05.17.
 */

public interface GeoFencePresenter {

    void setGeoFence(List<Company> allCompanyList, List<Company> selectedCompanyList);

    void onStop();

}
