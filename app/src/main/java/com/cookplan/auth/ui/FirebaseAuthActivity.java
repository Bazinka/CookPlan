package com.cookplan.auth.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.main.MainActivity;

public class FirebaseAuthActivity extends BaseActivity implements FirebaseAuthView {

    public static final String IS_ANONYMNOUS_POSSIBLE_KEY = "IS_ANONYMNOUS_POSSIBLE_KEY";

    private ProgressDialog mProgressDialog;
    private TextView authTextView;
    private View rootView;

    private FirebaseAuthPresenter presenter;

    private boolean isAnonymnousPossible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);
        authTextView = (TextView) findViewById(R.id.auth_textView);
        rootView = findViewById(R.id.firebase_auth_root);

        isAnonymnousPossible = getIntent().getBooleanExtra(IS_ANONYMNOUS_POSSIBLE_KEY, true);
        Button signInButton = (Button) findViewById(R.id.sign_in_google_button);
        if (isAnonymnousPossible) {
            authTextView.setVisibility(View.VISIBLE);
            authTextView.setText(R.string.anonymnous_auth);
            signInButton.setVisibility(View.GONE);
        } else {
            authTextView.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            signInButton.setOnClickListener(v -> {
                if (presenter != null) {
                    presenter.firstAuthSignIn();
                }
            });
        }
        presenter = new FirebaseAuthPresenterImpl(this, this, isAnonymnousPossible);
        presenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            if (presenter.getCurrentUser() == null) {
                if (isAnonymnousPossible) {
                    presenter.firstAuthSignIn();
                }
            } else {
                signedInWithAnonymous();
            }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivityWithLeftAnimation(intent);
        finish();
    }


    @Override
    public void showSnackbar(int messageRes) {
        Snackbar.make(rootView, messageRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingDialog(String message) {
        dismissDialog();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setTitle("");
        }

        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    @Override
    public void signedInWithAnonymous() {
        startMainActivity();
    }

    @Override
    public void signedInWithGoogle() {
        startMainActivity();
    }

    @Override
    public void signedInFailed() {
        dismissDialog();
        showSnackbar(R.string.unknown_sign_in_response);
    }

    @Override
    public void showLoadingDialog(@StringRes int stringResource) {
        showLoadingDialog(getString(stringResource));
    }

    @Override
    public void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

}