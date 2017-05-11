package com.cookplan.companies.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.companies.firebase_database.FirebaseRecyclerAdapter;
import com.cookplan.models.Company;
import com.google.firebase.database.Query;

public class CompanyListRecyclerViewAdapter extends FirebaseRecyclerAdapter<Company, CompanyListRecyclerViewAdapter.PointListViewHolder> {

    private OnPointsListEventListener listener;

    /**
     * @param ref      The Firebase location to watch for data changes. Can also be a slice of a location,
     *                 using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     * @param listener callback if event of click on item or data changes will happen.
     */
    public CompanyListRecyclerViewAdapter(Query ref, OnPointsListEventListener listener) {
        super(Company.class, ref);
        this.listener = listener;
    }

    @Override
    protected void populateViewHolder(PointListViewHolder holder, Company company, int position) {
        holder.company = company;
        holder.nameTextView.setText(company.getName());
        holder.contentTextView.setText(company.getComment());

        holder.mainView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.company);
            }
        });
    }

    @Override
    protected void onDataChanged() {
        if (listener != null) {
            listener.onDataChanged();
        }
    }

    @Override
    public PointListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_point, parent, false);
        return new PointListViewHolder(view);
    }

    public static class PointListViewHolder extends RecyclerView.ViewHolder {
        final View mainView;
        final TextView nameTextView;
        final TextView contentTextView;
        Company company;

        PointListViewHolder(View view) {
            super(view);
            mainView = view;
            nameTextView = (TextView) view.findViewById(R.id.name);
            contentTextView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + contentTextView.getText() + "'";
        }
    }

    public interface OnPointsListEventListener {
        void onItemClick(Company item);

        void onDataChanged();
    }
}
