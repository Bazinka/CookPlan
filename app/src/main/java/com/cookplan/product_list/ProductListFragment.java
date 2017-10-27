package com.cookplan.product_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import com.cookplan.models.Product;
import com.cookplan.product_list.update_database.UpdateProductDatabaseActivity;
import com.cookplan.utils.FillProductDatabaseProvider;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends BaseFragment implements ProductListView {

    private ProductListRecyclerAdapter adapter;
    private ProductListPresenter presenter;

    public ProductListFragment() {
    }


    public static ProductListFragment newInstance() {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }
        setRetainInstance(true);
        presenter = new ProductListPresenterImpl(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getProductList();
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
        setMainView((ViewGroup) inflater.inflate(R.layout.fragment_product_list, container, false));
        ProgressBar progressBar = (ProgressBar) getMainView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Set the adapter
        RecyclerView recyclerView = (RecyclerView) getMainView().findViewById(R.id.product_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ProductListRecyclerAdapter(new ArrayList<>(), recipe -> {
            Activity activity = getActivity();
            if (activity instanceof BaseActivity) {
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) getMainView().findViewById(R.id.add_product_fab);
        fab.setOnClickListener(view -> {

            new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle).setTitle(R.string.attention_title)
                    .setMessage("Вы уверены, что хотите обновить базу продуктов?")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        if (getActivity() instanceof BaseActivity) {
                            Intent intent = new Intent(getActivity(), UpdateProductDatabaseActivity.class);
                            ((BaseActivity) getActivity()).startActivityWithLeftAnimation(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                    .show();
        });


        return getMainView();
    }

    @Override
    public void setEmptyView() {
        ProgressBar progressBar = (ProgressBar) getMainView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.VISIBLE);
        setRecyclerViewVisability(View.GONE);
    }

    private void setRecyclerViewVisability(int visability) {
        View recyclerView = getMainView().findViewById(R.id.product_list_recycler);
        if (recyclerView != null) {
            recyclerView.setVisibility(visability);
        }
    }

    @Override
    public void setProductList(List<Product> productList) {
        ProgressBar progressBar = (ProgressBar) getMainView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.GONE);
        setRecyclerViewVisability(View.VISIBLE);
        adapter.updateItems(productList);
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }
}
