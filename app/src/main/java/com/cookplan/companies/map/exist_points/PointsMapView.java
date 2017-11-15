package com.cookplan.companies.map.exist_points;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cookplan.R;
import com.cookplan.companies.map.MapEventListener;
import com.cookplan.models.Company;
import com.cookplan.utils.BitmapUtils;
import com.cookplan.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookplan.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE;


public class PointsMapView extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private boolean mPermissionDenied = false;
    private GoogleMap mMap;

    private Map<Marker, Company> markersToPointMap;
    private Marker prevSelectedMarker;
    private Company selectedCompany;

    private MapEventListener mapEventListener;

    public PointsMapView() {
    }

    public static PointsMapView newInstance() {
        PointsMapView fragment = new PointsMapView();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.view_points_list_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            // Get the location button view
//            View locationButton = ((View) mapFragment.getView().findViewById(1).getParent()).findViewById(2);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
//            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//            params.setMargins(0, 30, 30, 0);
        }
        return mainView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (getParentFragment() instanceof ExistingPointsMapFragment) {
            ((ExistingPointsMapFragment) getParentFragment()).loadPointsList();
        }

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermissionFromFragment(this, LOCATION_PERMISSION_REQUEST_CODE,
                                                          android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    View layout = LayoutInflater.from(getActivity()).inflate(R.layout.transparency_map_info_window, null);
                    return layout;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null && selectedCompany == null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                                                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
        if (mMap != null) {
            if (getParentFragment() instanceof ExistingPointsMapFragment) {
                ((ExistingPointsMapFragment) getParentFragment()).loadPointsList();
            }
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.ErrorDialog
                .newInstance("showMissingPermissionError").show(getActivity().getFragmentManager(), "dialog");
    }

    public void showPointsToMap(List<Company> companies) {
        if (mMap != null) {
            if (markersToPointMap == null) {
                markersToPointMap = new HashMap<>();
            } else {
                markersToPointMap.clear();
            }

            mMap.clear();

            for (Company company : companies) {
                LatLng latLng = new LatLng(company.getLatitude(), company.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(company.getName())
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_pin)));
                Marker marker = mMap.addMarker(markerOptions);
                markersToPointMap.put(marker, company);
            }
            showSelectedPoint();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    @Override
    public boolean onMarkerClick(Marker clickedMarker) {
        if (markersToPointMap != null && markersToPointMap.get(clickedMarker) != null) {
            if (prevSelectedMarker != null) {
                prevSelectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_pin)));
            }
            prevSelectedMarker = clickedMarker;
            prevSelectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_selected_pin)));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(prevSelectedMarker.getPosition(), 15);
            mMap.animateCamera(cameraUpdate);

            Company company = markersToPointMap.get(clickedMarker);
            if (mapEventListener != null) {
                mapEventListener.onPointClick(company);
            }
        }
        return false;
    }

    public void setSelectedCompany(Company company) {
        selectedCompany = company;
    }

    public void showSelectedPoint() {
        if (selectedCompany != null && markersToPointMap != null) {
            for (Map.Entry<Marker, Company> entry : markersToPointMap.entrySet()) {
                if (entry.getValue().getId() == selectedCompany.getId()) {
                    onMarkerClick(entry.getKey());
                }
            }
        }
    }

    public void setMapEventListener(MapEventListener mapEventListener) {
        this.mapEventListener = mapEventListener;
    }
}
