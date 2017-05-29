package com.cookplan.companies.list;

import android.content.Intent;
import android.os.Bundle;

import com.cookplan.BaseActivity;
import com.cookplan.R;

import static com.cookplan.todo_list.edit_item.EditToDoItemActivity.COMPANY_KEY;

public class CompanyListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        setNavigationArrow();
        CompanyListFragment companiesFragment = (CompanyListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.company_list_fragment);
        if (companiesFragment != null) {
            companiesFragment.setOnCompanyClickListener(company -> {
                Intent intent = new Intent();
                intent.putExtra(COMPANY_KEY, company);
                setResult(RESULT_OK, intent);
                finish();
            });
        }
    }
}
