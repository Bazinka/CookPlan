package com.cookplan.companies.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by DariaEfimova on 11.05.17.
 */

public abstract class CompanyListBaseAdapter extends RecyclerView.Adapter<CompanyListBaseAdapter.ViewHolder> {
    private final List<Company> values;
    private CompanyListRecyclerAdapter.CompanyListEventListener listener;

    public CompanyListBaseAdapter(List<Company> items, CompanyListRecyclerAdapter.CompanyListEventListener listener) {
        values = items;
        this.listener = listener;
    }

    @Override
    public CompanyListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_list_item_layout, parent, false);
        return new CompanyListBaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Company company = values.get(position);
        holder.nameView.setText(company.getName());
        holder.commentView.setText(company.getComment());

        holder.mainView.setTag(company);
        holder.mainView.setOnClickListener(v -> {
            Company localCompany = (Company) v.getTag();
            if (listener != null && localCompany != null) {
                listener.onCompanyClick(localCompany);
            }
            onCompanyClick(company);
        });

        holder.mapView.setTag(company);

        // Ensure the map has been initialised by the on map ready callback in ViewHolder.
        // If it is not ready yet, it will be initialised with the NamedLocation set as its tag
        // when the callback is received.
        if (holder.map != null) {
            // The map is already ready to be used
            holder.setMapLocation(holder.map, company);
        }


        setCustomFields(company, holder);
    }

    protected abstract void onCompanyClick(Company company);

    protected abstract void setCustomFields(Company company, ViewHolder holder);

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
        GoogleMap map;

        public ViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.main_view);
            nameView = (TextView) view.findViewById(R.id.company_item_name);
            commentView = (TextView) view.findViewById(R.id.company_item_comment);
            mapView = (MapView) view.findViewById(R.id.company_map_preview);
            initializeMapView();
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
    }
}
