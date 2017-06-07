package com.cookplan.companies.review;

/**
 * Created by DariaEfimova on 07.06.17.
 */

public interface CompanyReviewView {

    void setAddedToGeoFence(boolean isAdded);

    void setError(int errorString);

    void setGeofenceRemovedSuccessfull();
}
