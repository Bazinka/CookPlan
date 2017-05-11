package com.cookplan.companies.map.exist_points;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.companies.map.MapEventListener;
import com.cookplan.models.Company;

import java.util.List;

public class ExistingPointsMapFragment extends Fragment implements ExistingPointsMapView, MapEventListener {

    private static final String SELECTED_POINT_KEY = "SELECTED_POINT_KEY";
    private ExistingPointsMapPresenter presenter;

    private BottomSheetBehavior behavior;
    private ViewGroup mainView;

    private Company selectedCompany;

    public ExistingPointsMapFragment() {
    }

    public static ExistingPointsMapFragment newInstance() {
        ExistingPointsMapFragment fragment = new ExistingPointsMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ExistingPointsMapFragment newInstance(Company company) {
        ExistingPointsMapFragment fragment = new ExistingPointsMapFragment();
        Bundle args = new Bundle();
        if (company != null) {
            args.putSerializable(SELECTED_POINT_KEY, company);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            selectedCompany = (Company) getArguments().getSerializable(SELECTED_POINT_KEY);
        }
        presenter = new ExistingPointsMapPresenterImpl(getActivity(), this);
        presenter.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_existing_points_map, container, false);

        View bottomSheet = mainView.findViewById(R.id.map_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });
        RecyclerView photosRecyclerView = (RecyclerView) bottomSheet.findViewById(R.id.photos_recycler_view);
        photosRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        photosRecyclerView.setLayoutManager(layoutManager);

        PointsMapView pointsMapView = (PointsMapView) getChildFragmentManager()
                .findFragmentById(R.id.points_map_view);
        if (pointsMapView != null) {
            pointsMapView.setMapEventListener(this);
            if (selectedCompany != null) {
                //                onPointClick(selectedPoint);
                pointsMapView.setSelectedCompany(selectedCompany);
            }
        }
        return mainView;
    }

    @Override
    public void setListPointsToMap(List<Company> pointsList) {
        PointsMapView pointsMapView = (PointsMapView) getChildFragmentManager()
                .findFragmentById(R.id.points_map_view);
        if (pointsMapView != null) {
            pointsMapView.showPointsToMap(pointsList);
        }
    }

    public void loadPointsList() {
        if (presenter != null) {
            presenter.getPoints();
        }
    }

    @Override
    public void onPointClick(Company company) {
        if (company != null) {
            View bottomSheet = mainView.findViewById(R.id.map_bottom_sheet);
            bottomSheet.setVisibility(View.VISIBLE);
            //        bottomSheet.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            TextView nameTextView = (TextView) bottomSheet.findViewById(R.id.name_place_text_view);
            nameTextView.setText(company.getName());

            TextView descTextView = (TextView) bottomSheet.findViewById(R.id.desc_place_text_view);
            descTextView.setText(company.getComments());

            RecyclerView photosRecyclerView = (RecyclerView) bottomSheet.findViewById(R.id.photos_recycler_view);
            //            if (point.getPhotoList() != null && point.getPhotoList().size() > 0) {
            //                photosRecyclerView.setVisibility(View.VISIBLE);
            //                PhotoListRecyclerAdapter adapter = new PhotoListRecyclerAdapter(getActivity(), point.getPhotoList());
            //                photosRecyclerView.setAdapter(adapter);
            //            } else {
            photosRecyclerView.setVisibility(View.GONE);
            //            }

            selectedCompany = null;
        }
    }
}
