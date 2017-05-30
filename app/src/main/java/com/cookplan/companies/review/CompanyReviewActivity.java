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
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.main.ViewPagerTabsAdapter;
import com.cookplan.models.Company;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;
import com.cookplan.shopping_list.list_by_dishes.ShopListByDishesFragment;
import com.cookplan.shopping_list.total_list.TotalShoppingListFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CompanyReviewActivity extends BaseActivity implements OnMapReadyCallback, CompanyReviewView {

    public static final String COMPANY_OBJECT_KEY = "COMPANY_OBJECT_KEY";

    private Company company;
    private CompanyReviewPresenter presenter;

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
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    setToolbarChanged(appBarLayout, verticalOffset);
                }
            });

            ViewPager viewPager = (ViewPager) findViewById(R.id.company_tabs_viewpager);
            viewPager.setVisibility(View.VISIBLE);

            ViewPagerTabsAdapter adapter = new ViewPagerTabsAdapter(getSupportFragmentManager());
            adapter.addFragment(CompanyToDoListFragment.newInstance(),
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

            presenter = new CompanyReviewPresenterImpl(this);

            TextView commentText = (TextView) findViewById(R.id.company_review_comment);
            commentText.setText(company.getComment());

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.company_review_map);
            mapFragment.getMapAsync(this);

            //            RecyclerView needToBuyRecyclerView = (RecyclerView) findViewById(R.id.company_review_todo_list_recycler);
            //            needToBuyRecyclerView.setHasFixedSize(true);
            //            needToBuyRecyclerView.setNestedScrollingEnabled(false);
            //            needToBuyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
    protected void onStart() {
        super.onStart();
        if (presenter != null && company != null) {
            presenter.getCompanyToDoList(company);
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
    public void onMapReady(GoogleMap _googleMap) {
        MapsInitializer.initialize(RApplication.getAppContext());
        GoogleMap googleMap = _googleMap;
        LatLng location = new LatLng(company.getLatitude(), company.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
        googleMap.addMarker(new MarkerOptions().position(location)
                                    .title(company.getName()));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setToDoList(List<ToDoItem> todoList) {
        //        for (ToDoItem item : todoList) {
        //            item.setCategory(presenter.getToDoCategoryById(item.getCategoryId()));
        //        }
        //        CompanyToDoRecyclerViewAdapter adapter = new CompanyToDoRecyclerViewAdapter(todoList);
        //        RecyclerView needToBuyRecyclerView = (RecyclerView) findViewById(R.id.company_review_todo_list_recycler);
        //        needToBuyRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setToDoCategoryList(List<ToDoCategory> toDoCategoryList) {
        if (presenter != null) {
            presenter.setCompanyCategoryToDoList(toDoCategoryList);
        }
    }

    @Override
    public void setEmptyView() {
    }
}
