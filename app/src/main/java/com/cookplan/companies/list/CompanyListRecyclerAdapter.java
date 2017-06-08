package com.cookplan.companies.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.models.Company;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CompanyListRecyclerAdapter extends RecyclerView.Adapter<CompanyListRecyclerAdapter.ViewHolder> {
    private final List<Company> values;
    private CompanyListRecyclerAdapter.CompanyListEventListener listener;

    public CompanyListRecyclerAdapter(List<Company> items, CompanyListRecyclerAdapter.CompanyListEventListener listener) {
        values = items;
        this.listener = listener;
    }

    @Override
    public CompanyListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_list_item_layout, parent, false);
        return new CompanyListRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CompanyListRecyclerAdapter.ViewHolder holder, int position) {
        Company company = values.get(position);
        holder.nameView.setText(company.getName());
        holder.commentView.setText(company.getComment());

        holder.mainView.setTag(company);
        holder.mainView.setOnClickListener(v -> {
            Company localCompany = (Company) v.getTag();
            if (listener != null && localCompany != null) {
                listener.onCompanyClick(localCompany);
            }
        });

        holder.mainView.setOnLongClickListener(view -> {
            Company localCompany = (Company) view.getTag();
            if (listener != null && localCompany != null) {
                listener.onCompanyLongClick(localCompany);
            }
            return false;
        });

        holder.mapView.setTag(company);

        if (holder.map != null) {
            holder.setMapLocation(holder.map, company);
        }


        holder.geoFenceImageView.setTag(company);
        holder.geoFenceImageView.setOnClickListener(view -> {
            //TODO:доделать
        });
    }

    public void updateItems(List<Company> companies) {
        values.clear();
        values.addAll(companies);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public List<Company> getValues() {
        return values;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        public final View mainView;
        public MapView mapView;
        public final TextView nameView;
        public final TextView commentView;
        final ImageView geoFenceImageView;
        GoogleMap map;

        public ViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.main_view);
            nameView = (TextView) view.findViewById(R.id.company_item_name);
            commentView = (TextView) view.findViewById(R.id.company_item_comment);
            mapView = (MapView) view.findViewById(R.id.company_map_preview);
            initializeMapView();
            geoFenceImageView = (ImageView) view.findViewById(R.id.geofence_icon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(RApplication.getAppContext());
            map = googleMap;
            Company company = (Company) mapView.getTag();
            if (company != null) {
                setMapLocation(map, company);
            }
        }

        public void initializeMapView() {
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.setClickable(false);
                mapView.getMapAsync(this);
            }
        }

        private void setMapLocation(GoogleMap map, Company company) {
            LatLng location = new LatLng(company.getLatitude(), company.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
            map.addMarker(new MarkerOptions().position(location));
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }

    public interface CompanyListEventListener {
        void onCompanyClick(Company company);

        void onCompanyLongClick(Company company);
    }
}