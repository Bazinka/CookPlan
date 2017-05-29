package com.cookplan.companies.list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.models.Company;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;


public class CompanyListFragment extends BaseFragment implements CompanyListView {

    private static final String MULTISELECT_LIST_KEY = "MULTISELECT_KEY";


    private CompanyListPresenter presenter;

    private OnPointsListClickListener listener;
    private CompanyListBaseAdapter adapter;
    private boolean isMultiselect;
    private View mainView;

    public CompanyListFragment() {
    }

    public static CompanyListFragment newInstance() {
        CompanyListFragment fragment = new CompanyListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static CompanyListFragment newInstance(boolean isMultiselect) {
        CompanyListFragment fragment = new CompanyListFragment();
        Bundle args = new Bundle();
        args.putBoolean(MULTISELECT_LIST_KEY, isMultiselect);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        isMultiselect = false;
        if (getArguments() != null) {
            isMultiselect = getArguments().getBoolean(MULTISELECT_LIST_KEY);
        }
        presenter = new CompanyListPresenterImpl(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getUsersCompanyList();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_company_list, container, false);

        RecyclerView companyListRecyclerView = (RecyclerView) mainView.findViewById(R.id.company_list_recycler);
        companyListRecyclerView.setHasFixedSize(false);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        companyListRecyclerView.setLayoutManager(manager);

        if (!isMultiselect) {
            adapter = new CompanyListRecyclerAdapter(new ArrayList<>(), company -> {
                if (listener != null) {
                    listener.onClick(company);
                }
            });
        } else {
            adapter = new CompanyMultiselectRecyclerAdapter(new ArrayList<>(), company -> {
                if (listener != null) {
                    listener.onClick(company);
                }
            });
        }
        companyListRecyclerView.setAdapter(adapter);
        // Set a RecyclerListener to clean up MapView from ListView
        companyListRecyclerView.setRecyclerListener(viewHolder -> {
            if (viewHolder instanceof CompanyListBaseAdapter.ViewHolder) {
                CompanyListBaseAdapter.ViewHolder holder = (CompanyListBaseAdapter.ViewHolder) viewHolder;
                if (holder.map != null) {
                    // Clear the map and free up resources by changing the map type to none
                    holder.map.clear();
                    holder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
                }
            }
        });

        return mainView;
    }

    public void setOnCompanyClickListener(OnPointsListClickListener _listener) {
        listener = _listener;
    }

    public List<Company> getValues() {
        return adapter != null ? adapter.getValues() : new ArrayList<>();
    }

    public List<Company> getSelectedValues() {
        if (adapter instanceof CompanyMultiselectRecyclerAdapter) {
            return ((CompanyMultiselectRecyclerAdapter) adapter).getSelectedValues();
        } else {
            return null;
        }
    }

    @Override
    public void setCompanyList(List<Company> companyList) {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.GONE);
        setRecyclerViewVisability(View.VISIBLE);
        if (adapter != null) {
            adapter.updateItems(companyList);
        }
    }

    @Override
    public void setEmptyView() {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.VISIBLE);
        setRecyclerViewVisability(View.GONE);
    }

    private void setRecyclerViewVisability(int visability) {
        View recyclerView = mainView.findViewById(R.id.product_list_recycler);
        if (recyclerView != null) {
            recyclerView.setVisibility(visability);
        }
    }

    public interface OnPointsListClickListener {
        void onClick(Company item);
    }
}
