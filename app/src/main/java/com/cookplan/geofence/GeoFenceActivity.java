package com.cookplan.geofence;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Company;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.content.ContextCompat;

public class GeoFenceActivity extends BaseActivity implements GeoFenceView {

    public static final String COMPANY_OBJECT_KEY = "COMPANY_OBJECT_KEY";
    public static final String IS_COMPANY_ADDED_TO_GEOFENCE_KEY = "IS_COMPANY_ADDED_TO_GEOFENCE_KEY";

    private GeoFencePresenter presenter;
    private GoogleMap googleMap;
    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence);
        setNavigationArrow();
        presenter = new GeoFencePresenterImpl(this, this);
        company = (Company) getIntent().getSerializableExtra(COMPANY_OBJECT_KEY);
        if (company == null) {
            finish();
        } else {
            setNavigationArrow();
            setTitle(getString(R.string.alerts_for_title) + " " + company.getName());
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.company_review_map);
            mapFragment.getMapAsync(_googleMap -> {
                MapsInitializer.initialize(this);
                googleMap = _googleMap;
                LatLng location = new LatLng(company.getLatitude(), company.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
                googleMap.addMarker(new MarkerOptions().position(location)
                                            .title(company.getName()));
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            });
            EditText radiusEditText = (EditText) findViewById(R.id.radius_editText);
            radiusEditText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String radiusText = radiusEditText.getText().toString();
                    if (!radiusText.isEmpty()) {
                        LatLng location = new LatLng(company.getLatitude(), company.getLongitude());
                        googleMap.clear();
                        googleMap.addCircle(new CircleOptions()
                                                    .center(location)
                                                    .radius(Double.valueOf(radiusText))
                                                    .strokeWidth(1f)
                                                    .fillColor(ContextCompat.getColor(this, R.color.transparency_grey)));
                        googleMap.addMarker(new MarkerOptions().position(location)
                                                    .title(company.getName()));
                    }
                    isRadiusFilledCorrect();
                    return true;
                }
                return false;
            });
        }
    }

    private boolean isRadiusFilledCorrect() {
        EditText radiusEditText = (EditText) findViewById(R.id.radius_editText);
        String errorText = null;
        String radiusText = radiusEditText.getText().toString();
        if (!radiusText.isEmpty()) {
            radiusEditText.setError(null);
        } else {
            errorText = getString(R.string.error_required_field);
        }
        TextInputLayout radiusLayout = (TextInputLayout) findViewById(R.id.enter_radius_edittext_layout);
        if (radiusLayout != null) {
            radiusLayout.setError(errorText);
            radiusLayout.setErrorEnabled(errorText != null);
        }
        return errorText == null;
    }

    private boolean isPeriodFilledCorrect() {
        EditText daysEditText = (EditText) findViewById(R.id.days_editText);
        String errorText = null;
        String daysText = daysEditText.getText().toString();
        if (!daysText.isEmpty()) {
            daysEditText.setError(null);
        } else {
            errorText = getString(R.string.error_required_field);
        }
        TextInputLayout daysLayout = (TextInputLayout) findViewById(R.id.enter_days_edittext_layout);
        if (daysLayout != null) {
            daysLayout.setError(errorText);
            daysLayout.setErrorEnabled(errorText != null);
        }
        return errorText == null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.done_menu, _menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_done) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            if (presenter != null && isPeriodFilledCorrect() && isRadiusFilledCorrect()) {
                EditText radiusEditText = (EditText) findViewById(R.id.radius_editText);
                float radius = Float.valueOf(radiusEditText.getText().toString());

                EditText daysEditText = (EditText) findViewById(R.id.days_editText);
                long days = Long.valueOf(daysEditText.getText().toString());

                presenter.setGeoFence(company, radius, days);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void setGeofenceAddedSuccessfull() {
        Intent intent = new Intent();
        intent.putExtra(IS_COMPANY_ADDED_TO_GEOFENCE_KEY, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setGeoFenceError(int errorResourceId) {
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, getString(errorResourceId), Snackbar.LENGTH_LONG).show();
        }
        finish();
    }

    @Override
    public void setGeofenceRemovedSuccessfull() {

    }
}
