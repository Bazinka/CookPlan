package com.cookplan.companies.review;

import com.cookplan.models.Company;

/**
 * Created by DariaEfimova on 07.06.17.
 */

public interface CompanyReviewPresenter {

    void isCompanyAddedToGeoFence();

    void removeGeoFence(Company company);

    void onStop();

    void onStart();

}
