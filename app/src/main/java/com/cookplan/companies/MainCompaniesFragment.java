package com.cookplan.companies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookplan.BaseActivity;
import com.cookplan.BaseFragment;
import com.cookplan.R;
import com.cookplan.companies.list.CompanyListFragment;
import com.cookplan.companies.map.search_new_point.SearchNewPointActivity;
import com.cookplan.companies.review.CompanyReviewActivity;

public class MainCompaniesFragment extends BaseFragment {

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
        setPointsListFragment();

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

    void setPointsListFragment() {
        getActivity().setTitle(getString(R.string.list_points_title));
        CompanyListFragment companyListFragment = CompanyListFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, companyListFragment);
        transaction.commit();
        companyListFragment.setOnCompanyClickListener(company -> {
            Activity activity = getActivity();
            if (activity instanceof BaseActivity) {
                Intent intent = new Intent(activity, CompanyReviewActivity.class);
                intent.putExtra(CompanyReviewActivity.COMPANY_OBJECT_KEY, company);
                ((BaseActivity) activity).startActivityWithLeftAnimation(intent);
            }
        });
    }
}
