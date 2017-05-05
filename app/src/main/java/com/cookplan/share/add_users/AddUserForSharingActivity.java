package com.cookplan.share.add_users;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Contact;
import com.cookplan.share.contact_list.ContactListActivity;

import java.util.ArrayList;
import java.util.List;

import static com.cookplan.main.MainActivity.SHARE_USER_EMAIL_LIST_KEY;

public class AddUserForSharingActivity extends BaseActivity implements AddUserForSharingView {

    public static final int GET_USER_LIST_FROM_CONTACT_REQUEST = 12;
    public static final String GET_USER_LIST_FROM_CONTACT_KEY = "GET_USER_LIST_FROM_CONTACT_KEY";

    private ContactListAdapter adapter;
    private AddUserForSharingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_for_sharing);
        setNavigationArrow();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactListAdapter(new ArrayList<>(), true);
        recyclerView.setAdapter(adapter);

        EditText emailEditText = (EditText) findViewById(R.id.user_email_editText);
        emailEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String emailText = emailEditText.getText().toString();
                String errorText = null;
                if (!emailText.isEmpty()) {
                    if (!emailText.contains(getString(R.string.gmail_ending_title))) {
                        emailText = emailText + getString(R.string.gmail_ending_title);
                    }
                    Contact newContact = new Contact(emailText);

                    boolean isContractExist = false;
                    for (Contact contact : adapter.getContactList()) {
                        if (newContact.getEmail().equals(contact.getEmail())) {
                            isContractExist = true;
                            break;
                        }
                    }
                    if (!isContractExist) {
                        adapter.addItem(newContact);
                    }
                    emailEditText.setError(null);
                    emailEditText.setText(null);
                } else {
                    errorText = getString(R.string.error_required_field);
                }
                TextInputLayout emailLayout = (TextInputLayout) findViewById(R.id.enter_email_edittext_layout);
                if (emailLayout != null) {
                    emailLayout.setError(errorText);
                    emailLayout.setErrorEnabled(errorText != null);
                }
                return true;
            }
            return false;
        });

        Button getFromContactListButton = (Button) findViewById(R.id.get_from_contact_button);
        getFromContactListButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactListActivity.class);
            startActivityForResultWithLeftAnimation(intent, GET_USER_LIST_FROM_CONTACT_REQUEST);

        });
        presenter = new AddUserForSharingPresenterImpl(this, this);
        presenter.getSharedUsers();
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
            List<Contact> contactList = adapter.getContactList();
            ArrayList<String> emails = new ArrayList<>();
            for (Contact contact : contactList) {
                emails.add(contact.getEmail());
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra(SHARE_USER_EMAIL_LIST_KEY, emails);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContactList(List<Contact> contactList) {
        if (adapter != null) {
            adapter.updateList(contactList);
        }
    }

    public void addContactList(List<Contact> contactList) {
        if (adapter != null) {
            adapter.addItemList(contactList);
        }
    }

    @Override
    public void setError(String error) {
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_USER_LIST_FROM_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                ArrayList<Contact> contacts = data.getParcelableArrayListExtra(GET_USER_LIST_FROM_CONTACT_KEY);
                addContactList(contacts);
            }
        }
    }
}
