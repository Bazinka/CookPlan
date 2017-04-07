package com.cookplan.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.cookplan.BaseActivity;
import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.auth.provider.AuthCredentialHelper;
import com.cookplan.auth.provider.GoogleProvider;
import com.cookplan.auth.provider.IdpProvider;
import com.cookplan.auth.ui.TaskFailureLogger;
import com.cookplan.main.MainActivity;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAuthActivity extends BaseActivity implements IdpProvider.IdpCallback {

    private static final int RC_ACCOUNT_LINK = 3;


    private ProgressDialog mProgressDialog;
    private View mRootView;

    private GoogleProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);
        mRootView = findViewById(R.id.firebase_auth_root);
        provider = new GoogleProvider(this, getGoogleProvider());

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startMainActivity();
        } else {
            showLoadingDialog(R.string.progress_dialog_loading);
            provider.setAuthenticationCallback(this);
            provider.startLogin(this);
        }
    }

    private AuthUI.IdpConfig getGoogleProvider() {

        return new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                .setPermissions(getGooglePermissions())
                .build();

    }

    private List<String> getGooglePermissions() {
        List<String> result = new ArrayList<>();

        //add Google drive file
        result.add(Scopes.DRIVE_FILE);
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoogleProvider.RC_SIGN_IN) {

            provider.onActivityResult(requestCode, resultCode, data);
            return;
        } else {
            dismissDialog();
            showSnackbar(R.string.unknown_sign_in_response);
        }
    }

    @Override
    public void onSuccess(final IdpResponse response) {
        AuthCredential credential = AuthCredentialHelper.getAuthCredential(response);
        if (credential != null) {
            RApplication.getFirebaseAuth()
                    .signInWithCredential(credential)
                    .addOnFailureListener(
                            new TaskFailureLogger("FIREBASEAUTH", "Firebase sign in with credential unsuccessful"))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startMainActivity();
                        } else {
                            dismissDialog();
                        }
                    });
        }
    }

    @Override
    public void onFailure(Bundle extra) {
        // stay on this screen
        dismissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        if (provider != null) {
            provider.disconnect();
        }
    }

    private void showSnackbar(int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

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

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
            startMainActivity();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                finish();
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }

            showSnackbar(R.string.unknown_sign_in_response);
        }
    }

    private void startMainActivity() {
        dismissDialog();
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivityWithLeftAnimation(intent);
        finish();
    }

    public void showLoadingDialog(@StringRes int stringResource) {
        showLoadingDialog(getString(stringResource));
    }

    public void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}