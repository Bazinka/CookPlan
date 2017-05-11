package com.cookplan.companies.map.search_new_point;

import android.content.Context;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class SearchNewPointMapPresenterImpl implements SearchNewPointMapPresenter {

    private SearchNewPointMapView mainView;
    private Context context;

    public SearchNewPointMapPresenterImpl(Context contex, SearchNewPointMapView mainView) {
        this.context = contex;
        this.mainView = mainView;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }
}
