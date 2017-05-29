package com.cookplan.companies.review;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Company;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CompanyReviewActivity extends BaseActivity implements OnMapReadyCallback {

    public static final String COMPANY_OBJECT_KEY = "COMPANY_OBJECT_KEY";

    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_review);
        setNavigationArrow();

        company = (Company) getIntent().getSerializableExtra(COMPANY_OBJECT_KEY);
        if (company == null) {
            finish();
        } else {
            setTitle(company.getName());

            TextView commentText = (TextView) findViewById(R.id.company_review_comment);
            if (company.getComment() != null && !company.getComment().isEmpty()) {
                commentText.setVisibility(View.VISIBLE);
                commentText.setText(company.getComment());
            } else {
                commentText.setVisibility(View.GONE);
            }

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.company_review_map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap _googleMap) {
        MapsInitializer.initialize(RApplication.getAppContext());
        GoogleMap googleMap = _googleMap;
        LatLng location = new LatLng(company.getLatitude(), company.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
        googleMap.addMarker(new MarkerOptions().position(location)
                                    .title(company.getName()));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
