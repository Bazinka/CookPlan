package com.cookplan.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.cookplan.BaseActivity;
import com.cookplan.main.MainActivity;
import com.cookplan.R;
import  com.cookplan.auth.AuthUI;
import com.cookplan.auth.ErrorCodes;
import  com.cookplan.auth.IdpResponse;
import  com.cookplan.auth.ResultCodes;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAuthActivity extends BaseActivity {


    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
    private static final int RC_SIGN_IN = 100;

    View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);
        mRootView = findViewById(R.id.firebase_auth_root);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivityWithLeftAnimation(createIntentToMainActivity(null));
            finish();
        } else {
            startActivityForResult(
                    AuthMethodPickerActivity.createIntent(
                            this,
                            AuthUI.getInstance().createSignInIntentBuilder()
                                    .setTheme(R.style.FirebaseUI)
                                    .setLogo(R.drawable.logo)
                                    .setProviders(getSelectedProviders())
                                    .setTosUrl(getSelectedTosUrl())
                                    .setIsSmartLockEnabled(false).getFlowParams()),
                    RC_SIGN_IN);
        }
    }

    private String getSelectedTosUrl() {
        return GOOGLE_TOS_URL;
//        return FIREBASE_TOS_URL;
    }

    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();

        //Email Provider
        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        //Twitter Provider
        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());
        //Facebook Provider
        selectedProviders.add(
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                        .setPermissions(getFacebookPermissions())
                        .build());
        //Google Provider
        selectedProviders.add(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                        .setPermissions(getGooglePermissions())
                        .build());

        return selectedProviders;
    }

    private List<String> getFacebookPermissions() {
        List<String> result = new ArrayList<>();

        //add Facebook scope friends
        result.add("user_friends");
        //add Facebook scope photos
        result.add("user_photos");
        return result;
    }

    private List<String> getGooglePermissions() {
        List<String> result = new ArrayList<>();

        //add Google drive file
        result.add(Scopes.DRIVE_FILE);
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

        showSnackbar(R.string.unknown_response);
    }

    private void showSnackbar(int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }


    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
            startActivityWithLeftAnimation(createIntentToMainActivity(response));
            finish();
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
        }

        showSnackbar(R.string.unknown_sign_in_response);
    }

    public Intent createIntentToMainActivity(IdpResponse idpResponse) {
        Intent in = IdpResponse.getIntent(idpResponse);
        in.setClass(this, MainActivity.class);
        return in;
    }
}