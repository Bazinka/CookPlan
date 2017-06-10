package com.cookplan.recipe_import.web_browser;

import android.os.Bundle;

import com.cookplan.BaseActivity;
import com.cookplan.R;

public class WebBrowserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);
        setNavigationArrow();
        
    }
}
