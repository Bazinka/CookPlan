package com.cookplan.product_list.multiselect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Product;
import com.cookplan.product_list.ProductListPresenter;
import com.cookplan.product_list.ProductListPresenterImpl;
import com.cookplan.product_list.ProductListView;

import java.util.ArrayList;
import java.util.List;

import static com.cookplan.companies.review.products_fragment.CompanyProductsFragment.GET_PRODUCTS_TO_COMPANY_KEY;

public class MultiselectProductListActivity extends BaseActivity implements ProductListView {

    public final static String SELECTED_PRODUCTS_LIST_KEY = "SELECTED_PRODUCTS_LIST_KEY";
    private ProductListPresenter presenter;
    private MultiselectProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiselect_product_list);
        setNavigationArrow();
        presenter = new ProductListPresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getProductList();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.done_menu, _menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_done && adapter != null) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            ArrayList<Product> products = adapter.getSelectedProducts();
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(GET_PRODUCTS_TO_COMPANY_KEY, products);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setEmptyView() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.VISIBLE);
        setListViewVisability(View.GONE);
    }

    private void setListViewVisability(int visability) {
        View listView = findViewById(R.id.product_list_expListView);
        if (listView != null) {
            listView.setVisibility(visability);
        }
    }

    @Override
    public void setProductList(List<Product> productList) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        setEmptyViewVisability(View.GONE);
        setListViewVisability(View.VISIBLE);
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.product_list_expListView);
        ArrayList<Product> selectedProductList = getIntent().getParcelableArrayListExtra(SELECTED_PRODUCTS_LIST_KEY);
        if (selectedProductList == null) {
            selectedProductList = new ArrayList<>();
        }
        adapter = new MultiselectProductListAdapter(
                this, productList, selectedProductList);
        expandableListView.setAdapter(adapter);
        int count = adapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            expandableListView.expandGroup(position);
        }
    }

    @Override
    public void setErrorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    protected void setEmptyViewVisability(int visability) {
        ViewGroup emptyViewGroup = (ViewGroup) findViewById(R.id.empty_view);
        if (emptyViewGroup != null) {
            emptyViewGroup.setVisibility(visability);
        }
    }
}
