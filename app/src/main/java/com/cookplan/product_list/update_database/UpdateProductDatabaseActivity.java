package com.cookplan.product_list.update_database;

import android.os.Bundle;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.google.android.material.snackbar.Snackbar;

public class UpdateProductDatabaseActivity extends BaseActivity implements UpdateProductDatabaseView {

    private UpdateProductDatabasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product_database);
        setNavigationArrow();

        presenter = new UpdateProductDatabasePresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.updateProductDatabase();
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
    public void setError(String error) {
        Snackbar.make(findViewById(R.id.main_view), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setSuccessEnding() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
