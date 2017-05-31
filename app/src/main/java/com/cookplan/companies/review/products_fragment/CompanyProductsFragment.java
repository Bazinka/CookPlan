package com.cookplan.companies.review.products_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.models.Company;
import com.cookplan.models.Product;
import com.cookplan.product_list.multiselect.MultiselectProductListActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.cookplan.product_list.multiselect.MultiselectProductListActivity.SELECTED_PRODUCTS_LIST_KEY;

public class CompanyProductsFragment extends BaseFragment implements CompanyProductsView {

    public static final int GET_PRODUCTS_TO_COMPANY_REQUEST = 15;
    public static final String GET_PRODUCTS_TO_COMPANY_KEY = "GET_PRODUCTS_TO_COMPANY_KEY";
    private static final String COMPANY_OBJECT_KEY = "COMPANY_OBJECT_KEY";

    private CompanyProductsPresenter presenter;
    private Company company;
    private CompanyProductsRecyclerAdapter adapter;

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
        presenter = new CompanyProductsPresenterImpl(this, company);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getCompanyProductList();
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
        adapter = new CompanyProductsRecyclerAdapter(productList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_PRODUCTS_TO_COMPANY_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                ArrayList<Product> productArrayList = data.getParcelableArrayListExtra(GET_PRODUCTS_TO_COMPANY_KEY);
                ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                changedProductList(productArrayList);
            }
        }
    }

    private void changedProductList(List<Product> newProducts) {
        if (presenter != null) {
            List<Product> oldProducts = adapter != null ? adapter.getProductList() : new ArrayList<>();
            List<Product> productsToDelete = new LinkedList<>(oldProducts);
            for (Product oldProduct : oldProducts) {
                for (Product newProduct : newProducts) {
                    if (oldProduct.getId().equals(newProduct.getId())) {
                        productsToDelete.remove(oldProduct);
                    }
                }
            }
            if (!productsToDelete.isEmpty()) {
                presenter.deleteProductsFromCompany(productsToDelete);
            }
            presenter.updateProducts(newProducts);
        }
    }

    public void startAddProductActivity() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            Intent intent = new Intent(getActivity(), MultiselectProductListActivity.class);
            if (adapter != null) {
                intent.putParcelableArrayListExtra(SELECTED_PRODUCTS_LIST_KEY, adapter.getProductList());
            }
            startActivityForResult(
                    intent, GET_PRODUCTS_TO_COMPANY_REQUEST);
        }
    }
}
