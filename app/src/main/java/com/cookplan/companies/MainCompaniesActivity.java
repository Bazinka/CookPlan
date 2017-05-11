package com.cookplan.companies;

import android.os.Bundle;

import com.cookplan.BaseActivity;
import com.cookplan.R;

public class MainCompaniesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_companies_activity);
        setNavigationArrow();
    }
}
