package com.cookplan.companies.list;

import com.google.firebase.database.Query;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public interface CompanyListPresenter {

    public void onCreate();

    public void onDestroy();

    public Query getItems();
}
