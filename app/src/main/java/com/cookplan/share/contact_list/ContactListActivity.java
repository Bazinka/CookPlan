package com.cookplan.share.contact_list;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.models.Contact;
import com.cookplan.utils.PermissionUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ContactListActivity extends BaseActivity implements ContactListView {

    private ContactListAdapter adapter;

    private static final int RC_READ_CONTACTS_PERMS = 103;
    private static final String[] permission = new String[]{Manifest.permission.READ_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        setNavigationArrow();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.isPermissionsGranted(this, permission)) {
                PermissionUtils.requestPermissions(this, RC_READ_CONTACTS_PERMS, permission);
            } else {
                getContactList();
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
            ArrayList<Contact> contactList = adapter.getSelectedContactList();
            Intent intent = new Intent();
            //            intent.putParcelableArrayListExtra(Companion.getGET_USER_LIST_FROM_CONTACT_KEY(), contactList);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContactList(ArrayList<Contact> contactList) {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactListAdapter(contactList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setError(String error) {
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_READ_CONTACTS_PERMS: {
                boolean permGranded = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        permGranded = false;
                    }
                }
                if (grantResults.length == permission.length && permGranded) {
                    getContactList();
                } else {
                    setError(getString(R.string.permission_denied));
                }
            }
        }
    }

    private void getContactList() {
        if (PermissionUtils.isPermissionsGranted(this, permission)) {
            ContactListPresenter presenter = new ContactListPresenterImpl(this, this);
            presenter.getContactList();
        }
    }
}
