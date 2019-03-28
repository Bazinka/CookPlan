package com.cookplan.companies.map.search_new_point;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.companies.edit.EditCompanyActivity;
import com.cookplan.companies.map.MapEventListener;
import com.cookplan.models.Company;
import com.cookplan.utils.BitmapUtils;
import com.cookplan.utils.PermissionUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.cookplan.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE;

public class NewPointMapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private boolean mPermissionDenied = false;
    private GoogleMap mMap;

    private Marker newPointMarker;

    private Company selectedCompany;

    private MapEventListener mapEventListener;
    private ViewGroup mainView;

    public NewPointMapView() {
    }

    public static NewPointMapView newInstance() {
        NewPointMapView fragment = new NewPointMapView();
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
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.fragment_new_points_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            // Get the location button view
//            View locationButton = ((View) mapFragment.getView().findViewById(1).getParent()).findViewById(2);

//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//            params.setMargins(0, 0, 30, 30);
        }


        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                showNewPoint(place.getLatLng());
                startAddPointActivity(place.getLatLng().latitude, place.getLatLng().longitude, place.getName().toString());
            }

            @Override
            public void onError(Status status) {

            }
        });

        return mainView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(this);
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
                    ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.TransparentBackground);
                    LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    return inflater.inflate(R.layout.add_point_map_info_window, null);
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
            if (newPointMarker != null) {
                newPointMarker.remove();
                newPointMarker = null;
            }
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.ErrorDialog
                .newInstance("showMissingPermissionError").show(getActivity().getFragmentManager(), "dialog");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showNewPoint(latLng);
    }

    private void showNewPoint(LatLng latLng) {
        if (mMap != null) {

            mMap.clear();

            if (newPointMarker != null) {
                newPointMarker.remove();
            }

            MarkerOptions markerOption = new MarkerOptions()
                    .position(latLng)
                    .title("Сохранить место?")
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_pin)));
            newPointMarker = mMap.addMarker(markerOption);
            newPointMarker.showInfoWindow();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        startAddPointActivity(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    @Override
    public boolean onMarkerClick(Marker clickedMarker) {
        startAddPointActivity(newPointMarker.getPosition().latitude, newPointMarker.getPosition().longitude);
        return false;
    }

    void startAddPointActivity(double latitude, double longitude) {
        Intent intent = new Intent(getActivity(), EditCompanyActivity.class);
        intent.putExtra(EditCompanyActivity.PLACE_LATITUDE_KEY, latitude);
        intent.putExtra(EditCompanyActivity.PLACE_LONGITUDE_KEY, longitude);
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).startActivityWithUpAnimation(intent);
            getActivity().finish();
        }
    }

    void startAddPointActivity(double latitude, double longitude, String name) {
        Intent intent = new Intent(getActivity(), EditCompanyActivity.class);
        intent.putExtra(EditCompanyActivity.PLACE_LATITUDE_KEY, latitude);
        intent.putExtra(EditCompanyActivity.PLACE_LONGITUDE_KEY, longitude);
        intent.putExtra(EditCompanyActivity.PLACE_NAME_KEY, name);
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).startActivityWithUpAnimation(intent);
            getActivity().finish();
        }
    }

    public void setMapEventListener(MapEventListener mapEventListener) {
        this.mapEventListener = mapEventListener;
    }
}
