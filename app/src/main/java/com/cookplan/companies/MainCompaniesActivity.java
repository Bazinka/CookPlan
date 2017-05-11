package com.cookplan.companies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.companies.list.CompanyListFragment;
import com.cookplan.companies.map.exist_points.ExistingPointsMapFragment;
import com.cookplan.companies.map.search_new_point.SearchNewPointActivity;
import com.cookplan.models.Company;

public class MainCompaniesActivity extends BaseActivity {

    private enum TypeFragment {
        LIST, MAP;
    }

    private TypeFragment selectedType;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_companies_activity);
        setNavigationArrow();

        selectedType = TypeFragment.MAP;
        setPointsMapFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_point_fab);
        fab.setOnClickListener(view -> {
            startNewPointActivity();
        });
    }

    void startNewPointActivity() {
        Intent intent = new Intent(this, SearchNewPointActivity.class);
        startActivityWithLeftAnimation(intent);
    }

    void setPointsMapFragment(Company selectedCompany) {
        setTitle(getString(R.string.points_on_map_title));
        ExistingPointsMapFragment existingPointsMapFragment = ExistingPointsMapFragment.newInstance(selectedCompany);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, existingPointsMapFragment);
        transaction.commit();
    }

    void setPointsMapFragment() {
        setTitle(getString(R.string.points_on_map_title));
        ExistingPointsMapFragment existingPointsMapFragment = ExistingPointsMapFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, existingPointsMapFragment);
        transaction.commit();
    }

    void setPointsListFragment() {
        setTitle(getString(R.string.list_points_title));
        CompanyListFragment companyListFragment = CompanyListFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, companyListFragment);
        transaction.commit();
        companyListFragment.setOnPointClickListener(item -> {
            if (mMenu != null) {
                MenuItem menuItem = mMenu.findItem(R.id.action_points_list);
                if (menuItem != null) {
                    menuItem.setIcon(R.drawable.ic_list_points);
                }
            }
            selectedType = TypeFragment.MAP;
            setPointsMapFragment(item);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.main_companies, _menu);
        mMenu = _menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_points_list) {
            if (selectedType == TypeFragment.MAP) {
                setPointsListFragment();
                item.setIcon(R.drawable.ic_map_points);
                selectedType = TypeFragment.LIST;
            } else if (selectedType == TypeFragment.LIST) {
                setPointsMapFragment();
                item.setIcon(R.drawable.ic_list_points);
                selectedType = TypeFragment.MAP;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
