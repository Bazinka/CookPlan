package com.cookplan.todo_list.edit_item;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.companies.list.CompanyListActivity;
import com.cookplan.models.Company;
import com.cookplan.models.ToDoCategory;
import com.cookplan.models.ToDoItem;
import com.cookplan.views.ChooseColorView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class EditToDoItemActivity extends BaseActivity implements EditToDoItemView {

    public static final int CHOOSE_COMPANY_REQUEST = 14;
    public static final String COMPANY_KEY = "COMPANY_KEY";
    public static final String TODO_ITEM_KEY = "TODO_ITEM_KEY";

    private EditToDoItemPresenter presenter;
    private ToDoItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do_item);
        setNavigationArrow();
        presenter = new EditToDoItemPresenterImpl(this);
        selectedItem = (ToDoItem) getIntent().getSerializableExtra(TODO_ITEM_KEY);

        View chooseCompanyView = findViewById(R.id.todo_item_choose_company_text);
        chooseCompanyView.setOnClickListener(view -> {
            Intent intent = new Intent(this, CompanyListActivity.class);
            startActivityForResultWithLeftAnimation(intent, CHOOSE_COMPANY_REQUEST);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.getToDoCategoriesList();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_COMPANY_REQUEST) {
            Company company = null;
            if (resultCode == RESULT_OK) {
                company = (Company) data.getSerializableExtra(COMPANY_KEY);
            }
            TextView chooseCompanyTextView = (TextView) findViewById(R.id.todo_item_choose_company_text);
            if (company != null) {
                chooseCompanyTextView.setTag(company);
                chooseCompanyTextView.setText(company.getName());
            } else {
                chooseCompanyTextView.setTag(null);
                chooseCompanyTextView.setText(R.string.choose_default_company_name_title);
            }
        }
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
            EditText nameEditText = (EditText) findViewById(R.id.todo_item_name_edit_text);
            EditText commentEditText = (EditText) findViewById(R.id.todo_item_comment_edit_text);
            String name = nameEditText.getText().toString();
            String errorNameText = null;
            String errorCategoryText = null;
            ToDoCategory category = getCategoryItem();
            if (!name.isEmpty() && isCategoryValid()) {
                String comment = commentEditText.getText().toString();
                View chooseCompanyView = findViewById(R.id.todo_item_choose_company_text);
                Company company = (Company) chooseCompanyView.getTag();
                presenter.saveToDoItem(selectedItem, name, comment, company, category);
            } else {
                if (name.isEmpty()) {
                    errorNameText = getString(R.string.required_field);
                } else {
                    errorCategoryText = getString(R.string.category_required_error);
                }
            }
            TextInputLayout nameLayout = (TextInputLayout) findViewById(R.id.todo_item_name_edit_layout);
            if (nameLayout != null) {
                nameLayout.setError(errorNameText);
                nameLayout.setErrorEnabled(errorNameText != null);
            }
            TextInputLayout categoryLayout = (TextInputLayout) findViewById(R.id.category_edit_layout);
            if (categoryLayout != null) {
                categoryLayout.setError(errorCategoryText);
                categoryLayout.setErrorEnabled(errorCategoryText != null);
                if (errorCategoryText != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToDoCategoriesList(List<ToDoCategory> categoriesList) {
        AutoCompleteTextView categoryEditText = (AutoCompleteTextView) findViewById(R.id.category_edit_text);
        ToDoCategoryListAdapter adapter = new ToDoCategoryListAdapter(this, categoriesList, new ToDoCategoryListAdapter.onLookingForItemListener() {
            @Override
            public void onItemDoesntExist() {
                ViewGroup chooseColorLayout = (ViewGroup) findViewById(R.id.choose_category_color_layout);
                chooseColorLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onItemExist() {
                ViewGroup chooseColorLayout = (ViewGroup) findViewById(R.id.choose_category_color_layout);
                chooseColorLayout.setVisibility(View.GONE);
            }
        });
        categoryEditText.setAdapter(adapter);
        categoryEditText.setOnItemClickListener((parent, arg1, pos, id) -> {
            ViewGroup chooseColorLayout = (ViewGroup) findViewById(R.id.choose_category_color_layout);
            chooseColorLayout.setVisibility(View.GONE);
            ToDoCategory toDoCategory = categoriesList.get(pos);
            if (toDoCategory != null) {
                categoryEditText.setTag(toDoCategory);
            }
        });
        categoryEditText.setOnTouchListener((v, event) -> {
            categoryEditText.showDropDown();
            return false;
        });
    }

    @Override
    public void setSuccessfullSaving() {
        finish();
    }

    @Override
    public void setError(String error) {
        Snackbar.make(findViewById(R.id.main_view), error, Snackbar.LENGTH_LONG).show();
    }

    public ToDoCategory getCategoryItem() {
        AutoCompleteTextView categoryEditText = (AutoCompleteTextView) findViewById(R.id.category_edit_text);
        ToDoCategory categoryItem = (ToDoCategory) categoryEditText.getTag();
        String categoryName = categoryEditText.getText().toString();
        if (categoryItem == null || !categoryItem.getName().equals(categoryName)) {
            ChooseColorView colorView = (ChooseColorView) findViewById(R.id.choose_category_color_view);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (!categoryName.isEmpty() && colorView.getSelectedColor() != null && user != null) {
                String userId = user.getUid();
                categoryItem = new ToDoCategory(userId, categoryName, colorView.getSelectedColor());
            } else {
                categoryItem = null;
            }
        }
        return categoryItem;
    }

    public boolean isCategoryValid() {
        boolean valid = true;
        AutoCompleteTextView categoryEditText = (AutoCompleteTextView) findViewById(R.id.category_edit_text);
        ToDoCategory categoryItem = (ToDoCategory) categoryEditText.getTag();
        String categoryName = categoryEditText.getText().toString();
        if (categoryItem == null || !categoryItem.getName().equals(categoryName)) {
            ChooseColorView colorView = (ChooseColorView) findViewById(R.id.choose_category_color_view);
            if (!categoryName.isEmpty() && colorView.getSelectedColor() == null) {
                valid = false;
            }
        }
        return valid;
    }
}
