package com.cookplan.companies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cookplan.BaseActivity;
import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.companies.list.CompanyListFragment;
import com.cookplan.companies.map.exist_points.ExistingPointsMapFragment;
import com.cookplan.companies.map.search_new_point.SearchNewPointActivity;
import com.cookplan.models.Company;

public class MainCompaniesFragment extends BaseFragment {

    private enum TypeFragment {
        LIST, MAP;
    }

    private TypeFragment selectedType;
    private Menu mMenu;
    private View mainView;

    public static MainCompaniesFragment newInstance() {
        MainCompaniesFragment fragment = new MainCompaniesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_main_companies, container, false);
        selectedType = TypeFragment.MAP;
        setPointsMapFragment();

        FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.add_point_fab);
        fab.setOnClickListener(view -> {
            startNewPointActivity();
        });
        return mainView;
    }

    void startNewPointActivity() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            Intent intent = new Intent(getActivity(), SearchNewPointActivity.class);
            ((BaseActivity) activity).startActivityWithLeftAnimation(intent);
        }
    }

    void setPointsMapFragment(Company selectedCompany) {
        getActivity().setTitle(getString(R.string.points_on_map_title));
        ExistingPointsMapFragment existingPointsMapFragment = ExistingPointsMapFragment.newInstance(selectedCompany);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, existingPointsMapFragment);
        transaction.commit();
    }

    void setPointsMapFragment() {
        getActivity().setTitle(getString(R.string.points_on_map_title));
        ExistingPointsMapFragment existingPointsMapFragment = ExistingPointsMapFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, existingPointsMapFragment);
        transaction.commit();
    }

    void setPointsListFragment() {
        getActivity().setTitle(getString(R.string.list_points_title));
        CompanyListFragment companyListFragment = CompanyListFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, companyListFragment);
        transaction.commit();
        companyListFragment.setOnCompanyClickListener(item -> {
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
    public void onCreateOptionsMenu(Menu _menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_companies, _menu);
        mMenu = _menu;
        super.onCreateOptionsMenu(_menu, inflater);
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
