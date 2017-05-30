package com.cookplan.companies.review.product_list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.models.Company;
import com.cookplan.models.Product;

import java.util.List;

public class CompanyProductsFragment extends BaseFragment implements CompanyProductsView {

    private static final String COMPANY_OBJECT_KEY = "COMPANY_OBJECT_KEY";

    private CompanyProductsPresenter presenter;
    private Company company;

    public CompanyProductsFragment() {
    }


    public static CompanyProductsFragment newInstance(Company company) {
        CompanyProductsFragment fragment = new CompanyProductsFragment();
        Bundle args = new Bundle();
        args.putSerializable(COMPANY_OBJECT_KEY, company);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            company = (Company) getArguments().getSerializable(COMPANY_OBJECT_KEY);
        }
        setRetainInstance(true);
        presenter = new CompanyProductsPresenterImpl(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getCompanyProductList(company);
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
        mainView = (ViewGroup) inflater.inflate(R.layout.fragment_company_product_list, container, false);
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Set the adapter
        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.product_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return mainView;
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

    @Override
    public void setProductList(List<Product> productList) {
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.GONE);
        setRecyclerViewVisability(View.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.product_list_recycler);
        CompanyProductsRecyclerAdapter adapter = new CompanyProductsRecyclerAdapter(productList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }
}
