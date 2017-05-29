package com.cookplan.companies.review;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Company;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;
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
        setNavigationArrow();

        company = (Company) getIntent().getSerializableExtra(COMPANY_OBJECT_KEY);
        if (company == null) {
            finish();
        } else {
            presenter = new CompanyReviewPresenterImpl(this);
            setTitle(company.getName());

            TextView commentText = (TextView) findViewById(R.id.company_review_comment);
            if (company.getComment() != null && !company.getComment().isEmpty()) {
                commentText.setVisibility(View.VISIBLE);
                commentText.setText(company.getComment());
            } else {
                commentText.setVisibility(View.GONE);
            }

            TextView todoLitTitleTextView = (TextView) findViewById(R.id.company_review_todo_list_title);
            String todoListTitle = getString(R.string.todo_list_relate_company_title);
            todoListTitle = todoListTitle.replace("company_name", company.getName());
            todoLitTitleTextView.setText(todoListTitle);

            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.company_review_map);
            mapFragment.getMapAsync(this);

            RecyclerView needToBuyRecyclerView = (RecyclerView) findViewById(R.id.company_review_todo_list_recycler);
            needToBuyRecyclerView.setHasFixedSize(true);
            needToBuyRecyclerView.setNestedScrollingEnabled(false);
            needToBuyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        TextView todoLitTitleTextView = (TextView) findViewById(R.id.company_review_todo_list_title);
        todoLitTitleTextView.setVisibility(View.VISIBLE);
        for (ToDoItem item : todoList) {
            item.setCategory(presenter.getToDoCategoryById(item.getCategoryId()));
        }
        CompanyToDoRecyclerViewAdapter adapter = new CompanyToDoRecyclerViewAdapter(todoList);
        RecyclerView needToBuyRecyclerView = (RecyclerView) findViewById(R.id.company_review_todo_list_recycler);
        needToBuyRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setToDoCategoryList(List<ToDoCategory> toDoCategoryList) {
        if (presenter != null) {
            presenter.setCompanyCategoryToDoList(toDoCategoryList);
        }
    }

    @Override
    public void setEmptyView() {
        TextView todoLitTitleTextView = (TextView) findViewById(R.id.company_review_todo_list_title);
        todoLitTitleTextView.setVisibility(View.GONE);
    }
}
