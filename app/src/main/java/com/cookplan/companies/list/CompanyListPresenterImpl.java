package com.cookplan.companies.list;

import android.content.Context;

import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class CompanyListPresenterImpl implements CompanyListPresenter {

    private CompanyListView mainView;
    private Context context;

    private DatabaseReference pointListRef;

    public CompanyListPresenterImpl(Context contex, CompanyListView mainView) {
        this.context = contex;
        this.mainView = mainView;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        pointListRef = ref.child(DatabaseConstants.DATABASE_POINT_TABLE);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public Query getItems() {
        Query lastFifty = pointListRef.limitToLast(50);
        return lastFifty;
    }
}
