package com.cookplan.companies.map.exist_points;

import android.content.Context;

import com.cookplan.companies.firebase_database.FirebaseArray;
import com.cookplan.models.Company;
import com.cookplan.utils.DatabaseConstants;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

/**
 * Created by DariaEfimova on 19.10.16.
 */

public class ExistingPointsMapPresenterImpl implements ExistingPointsMapPresenter {

    private ExistingPointsMapView mainView;
    private DatabaseReference mDatabase;

    public ExistingPointsMapPresenterImpl(Context contex, ExistingPointsMapView mainView) {
        this.mainView = mainView;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void getPoints() {
        Query lastFifty = mDatabase.child(DatabaseConstants.DATABASE_POINT_TABLE).limitToLast(50);
        FirebaseArray<Company> mPointsList = new FirebaseArray<>(lastFifty, Company.class);
        mPointsList.setOnChangedListener(new FirebaseArray.OnChangedListener() {
            @Override
            public void onChildChanged(EventType type, int index, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                if (mainView != null) {
                    List<Company> companies = mPointsList.toModelItemArrayList();
                    mainView.setListPointsToMap(companies);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
