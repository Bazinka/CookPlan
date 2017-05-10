package com.cookplan.todo_list.edit_item;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.ToDoItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditToDoItemActivity extends BaseActivity implements EditToDoItemView {

    public static final String TODO_ITEM_KEY = "TODO_ITEM_KEY";

    private EditToDoItemPresenter presenter;
    private ToDoItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do_item);

        presenter = new EditToDoItemPresenterImpl(this);
        selectedItem = (ToDoItem) getIntent().getSerializableExtra(TODO_ITEM_KEY);
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
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            EditText nameEditText = (EditText) findViewById(R.id.todo_item_name_edit_text);
            EditText commentEditText = (EditText) findViewById(R.id.todo_item_comment_edit_text);
            if (user != null) {
                String name = nameEditText.getText().toString();
                String errorText = null;
                if (!name.isEmpty()) {
                    String comment = commentEditText.getText().toString();
                    if (selectedItem != null) {
                        selectedItem.setName(name);
                        selectedItem.setComment(comment);
                    } else {
                        selectedItem = new ToDoItem(user.getUid(), name, comment);
                    }
                    presenter.saveToDoItem(selectedItem);
                } else {
                    errorText = getString(R.string.required_field);
                }

                TextInputLayout nameLayout = (TextInputLayout) findViewById(R.id.todo_item_name_edit_layout);
                if (nameLayout != null) {
                    nameLayout.setError(errorText);
                    nameLayout.setErrorEnabled(errorText != null);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSuccessfullSaving() {
        finish();
    }

    @Override
    public void setError(String error) {
        Snackbar.make(findViewById(R.id.main_view), error, Snackbar.LENGTH_LONG).show();
    }
}
