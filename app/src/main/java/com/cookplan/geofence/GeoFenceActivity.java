package com.cookplan.geofence;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.companies.list.CompanyListFragment;

public class GeoFenceActivity extends BaseActivity implements GeoFenceView {


    private GeoFencePresenter presenter;
    private CompanyListFragment companyListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence);
        setNavigationArrow();
        presenter = new GeoFencePresenterImpl(this, this);
        companyListFragment = CompanyListFragment.newInstance(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, companyListFragment);
        transaction.commit();
        companyListFragment.setOnCompanyClickListener(item -> {
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.done_menu, _menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_bar_done) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            if (presenter != null) {
                if (companyListFragment.getSelectedValues() != null) {
                    presenter.setGeoFence(companyListFragment.getValues(), companyListFragment.getSelectedValues());
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void setGeofenceAddedSuccessfull() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setGeoFenceError(int errorResourceId) {
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, getString(errorResourceId), Snackbar.LENGTH_LONG).show();
        }
    }
}
