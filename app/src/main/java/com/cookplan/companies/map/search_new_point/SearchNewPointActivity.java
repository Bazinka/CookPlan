package com.cookplan.companies.map.search_new_point;

import android.os.Bundle;

import com.cookplan.BaseActivity;
import com.cookplan.R;

public class SearchNewPointActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_point);
        setNavigationArrow();
        setTitle(getString(R.string.search_new_place_title));
    }
}
