package com.cookplan.product_list;

import android.os.Bundle;

import com.cookplan.BaseActivity;
import com.cookplan.R;

public class VocabularyListActivity extends BaseActivity {

    private VocabularyListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setNavigationArrow();
        setTitle(getString(R.string.product_vocabulary_title));
    }
}
