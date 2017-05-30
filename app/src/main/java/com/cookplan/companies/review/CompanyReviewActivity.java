package com.cookplan.companies.review;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.companies.review.todo_fragment.CompanyToDoListFragment;
import com.cookplan.main.ViewPagerTabsAdapter;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationArrow();


        company = (Company) getIntent().getSerializableExtra(COMPANY_OBJECT_KEY);
        if (company == null) {
            finish();
        } else {
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
            appBarLayout.addOnOffsetChangedListener(this::setToolbarChanged);

            ViewPager viewPager = (ViewPager) findViewById(R.id.company_tabs_viewpager);
            viewPager.setVisibility(View.VISIBLE);

            ViewPagerTabsAdapter adapter = new ViewPagerTabsAdapter(getSupportFragmentManager());
            adapter.addFragment(CompanyToDoListFragment.newInstance(company),
                                getString(R.string.company_todo_list_title));
            //            adapter.addFragment(ShopListByDishesFragment.newInstance(),
            //                                getString(R.string.company_product_list_title));
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
        }
    }

    private void setToolbarChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        TextView commentText = (TextView) findViewById(R.id.company_review_comment);
        if (verticalOffset < -200) {
            commentText.setVisibility(View.GONE);
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        } else {
            commentText.setVisibility(View.VISIBLE);
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
