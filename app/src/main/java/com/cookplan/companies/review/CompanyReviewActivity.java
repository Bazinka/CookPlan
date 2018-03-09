package com.cookplan.companies.review;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.companies.review.products_fragment.CompanyProductsFragment;
import com.cookplan.companies.review.todo_fragment.CompanyToDoListFragment;
import com.cookplan.geofence.GeoFenceActivity;
import com.cookplan.geofence.GeoFencePresenter;
import com.cookplan.geofence.GeoFencePresenterImpl;
import com.cookplan.geofence.GeoFenceView;
import com.cookplan.main.ViewPagerTabsAdapter;
import com.cookplan.models.Company;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.cookplan.geofence.GeoFenceActivity.IS_COMPANY_ADDED_TO_GEOFENCE_KEY;

public class CompanyReviewActivity extends BaseActivity implements OnMapReadyCallback, GeoFenceView {

    public static final String COMPANY_OBJECT_KEY = "COMPANY_OBJECT_KEY";
    private static final int SET_GEOFENCE_REQUEST = 13;

    private GeoFencePresenter presenter;
    private Company company;

    protected Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationArrow();


        company = (Company) getIntent().getSerializableExtra(COMPANY_OBJECT_KEY);
        if (company == null) {
            finish();
        } else {
            presenter = new GeoFencePresenterImpl(this, this);
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
            appBarLayout.addOnOffsetChangedListener(this::setToolbarChanged);

            ViewPager viewPager = (ViewPager) findViewById(R.id.company_tabs_viewpager);
            viewPager.setVisibility(View.VISIBLE);

            ViewPagerTabsAdapter adapter = new ViewPagerTabsAdapter(getSupportFragmentManager());
            adapter.addFragment(CompanyToDoListFragment.newInstance(company),
                                getString(R.string.company_todo_list_title));
            adapter.addFragment(CompanyProductsFragment.newInstance(company),
                                getString(R.string.company_product_list_title));
            viewPager.setAdapter(adapter);

            TabLayout tabsLayout = (TabLayout) findViewById(R.id.company_tabs_layout);
            tabsLayout.setVisibility(View.VISIBLE);
            tabsLayout.setupWithViewPager(viewPager);


            CollapsingToolbarLayout collapsingToolbarLayout =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle(company.getName());


            TextView commentText = (TextView) findViewById(R.id.company_review_comment);
            commentText.setText(company.getComment());

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.company_review_map);
            mapFragment.getMapAsync(this);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_company_product_fab);
            fab.setOnClickListener(view -> {
                if (adapter.getCount() >= 1) {
                    Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                    if (fragment instanceof CompanyProductsFragment) {
                        ((CompanyProductsFragment) fragment).startAddProductActivity();
                    } else {
                        Toast.makeText(this, "Пока на этом экране можно добавлять только продукты", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    private void setToolbarChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        TextView commentText = (TextView) findViewById(R.id.company_review_comment);
        MenuItem geofenceOn = menu != null ? menu.findItem(R.id.app_bar_geofence_on) : null;
        MenuItem geofenceOff = menu != null ? menu.findItem(R.id.app_bar_geofence_off) : null;
        if (verticalOffset < -200) {
            commentText.setVisibility(View.GONE);
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            if (geofenceOn != null) {
                geofenceOn.setIcon(R.drawable.ic_geofence_on_white);
            }
            if (geofenceOff != null) {
                geofenceOff.setIcon(R.drawable.ic_geofence_off_white);
            }
        } else {
            commentText.setVisibility(View.VISIBLE);
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (geofenceOn != null) {
                geofenceOn.setIcon(R.drawable.ic_geofence_on_primary);
            }
            if (geofenceOff != null) {
                geofenceOff.setIcon(R.drawable.ic_geofence_off_primary);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap _googleMap) {
        MapsInitializer.initialize(this);
        GoogleMap googleMap = _googleMap;
        LatLng location = new LatLng(company.getLatitude(), company.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
        googleMap.addMarker(new MarkerOptions().position(location)
                                    .title(company.getName()));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_GEOFENCE_REQUEST) {
            if (resultCode == RESULT_OK) {
                boolean isCompantAdded = data.getBooleanExtra(IS_COMPANY_ADDED_TO_GEOFENCE_KEY, false);
                company.setAddedToGeoFence(isCompantAdded);
                setAddedToGeoFence(isCompantAdded);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.company_review_menu, _menu);
        menu = _menu;
        if (presenter != null) {
            presenter.isCompanyAddedToGeoFence(company);
        }
        return super.onCreateOptionsMenu(_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_bar_geofence_on || id == R.id.app_bar_geofence_off) {
            if (company.isAddedToGeoFence()) {
                presenter.removeGeoFence(company);
            } else {
                Intent intent = new Intent(this, GeoFenceActivity.class);
                intent.putExtra(GeoFenceActivity.COMPANY_OBJECT_KEY, company);
                startActivityForResultWithLeftAnimation(intent, SET_GEOFENCE_REQUEST);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAddedToGeoFence(boolean isAdded) {
        if (isAdded) {
            menu.findItem(R.id.app_bar_geofence_off).setVisible(true);
            menu.findItem(R.id.app_bar_geofence_on).setVisible(false);
        } else {
            menu.findItem(R.id.app_bar_geofence_off).setVisible(false);
            menu.findItem(R.id.app_bar_geofence_on).setVisible(true);
        }
    }

    @Override
    public void setGeofenceAddedSuccessfull() {
        company.setAddedToGeoFence(true);
        setAddedToGeoFence(true);
    }

    @Override
    public void setGeoFenceError(int errorResourceId) {
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, getString(errorResourceId), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void setGeofenceRemovedSuccessfull() {
        company.setAddedToGeoFence(false);
        setAddedToGeoFence(false);
    }
}
