package com.cookplan.companies.list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.models.Company;

import java.util.List;


public class CompanyListFragment extends BaseFragment implements CompanyListView {

    private CompanyListPresenter presenter;

    private OnPointsListClickListener listener;

    private CompanyListRecyclerViewAdapter adapter;
    private View mainView;

    public CompanyListFragment() {
    }

    public static CompanyListFragment newInstance() {
        CompanyListFragment fragment = new CompanyListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        mainView = inflater.inflate(R.layout.fragment_point_list, container, false);

        RecyclerView pointListRecyclerView = (RecyclerView) mainView.findViewById(R.id.points_list_recyclerview);
        pointListRecyclerView.setHasFixedSize(false);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        pointListRecyclerView.setLayoutManager(manager);

        attachRecyclerViewAdapter();
        return mainView;
    }

    private void attachRecyclerViewAdapter() {

        //        adapter = new CompanyListRecyclerViewAdapter(presenter != null ? presenter.getUsersCompanyList() : null,
        //                                                     new CompanyListRecyclerViewAdapter.OnPointsListEventListener() {
        //                                                         @Override
        //                                                         public void onItemClick(Company item) {
        //                                                             if (listener != null) {
        //                                                                 listener.onClick(item);
        //                                                             }
        //                                                         }
        //
        //                                                         @Override
        //                                                         public void onDataChanged() {
        //                                                             View emptyListView = mainView.findViewById(R.id.emptyTextView);
        //                                                             emptyListView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
        //                                                         }
        //                                                     });

        //        RecyclerView pointListRecyclerView = (RecyclerView) mainView.findViewById(R.id.points_list_recyclerview);

        //        pointListRecyclerView.setAdapter(adapter);
    }


    public void setOnCompanyClickListener(OnPointsListClickListener _listener) {
        listener = _listener;
    }

    @Override
    public void setCompanyList(List<Company> companyList) {

    }

    @Override
    public void setEmptyView() {

    }

    public interface OnPointsListClickListener {
        void onClick(Company item);
    }
}
