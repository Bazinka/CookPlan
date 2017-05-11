package com.cookplan.companies.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Company;

import java.util.List;

/**
 * Created by DariaEfimova on 11.05.17.
 */

public abstract class CompanyListBaseAdapter extends RecyclerView.Adapter<CompanyListBaseAdapter.ViewHolder> {
    private final List<Company> mValues;
    private CompanyListRecyclerAdapter.CompanyListEventListener listener;

    public CompanyListBaseAdapter(List<Company> items, CompanyListRecyclerAdapter.CompanyListEventListener listener) {
        mValues = items;
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
        Company company = mValues.get(position);
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

        setCustomFields(company, holder);
    }

    protected abstract void onCompanyClick(Company company);

    protected abstract void setCustomFields(Company company, ViewHolder holder);

    public void updateItems(List<Company> companies) {
        mValues.clear();
        mValues.addAll(companies);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mainView;
        public final TextView nameView;
        public final TextView commentView;

        public ViewHolder(View view) {
            super(view);
            mainView = view.findViewById(R.id.main_view);
            nameView = (TextView) view.findViewById(R.id.company_item_name);
            commentView = (TextView) view.findViewById(R.id.company_item_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }

    public interface CompanyListEventListener {
        void onCompanyClick(Company company);
    }
}
