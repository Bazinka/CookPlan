package com.cookplan.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cookplan.R;
import com.cookplan.auth.provider.GoogleProvider;
import com.cookplan.auth.provider.IdpProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 10.04.17.
 */

public class FirebaseAuthPresenterImpl implements FirebaseAuthPresenter, IdpProvider.IdpCallback {

    private FirebaseAuthView mainView;
    protected FragmentActivity activity;
    protected GoogleProvider provider;
    private boolean isAnonymnousPossible;

    public FirebaseAuthPresenterImpl(FirebaseAuthView mainView,
                                     FragmentActivity activity,
                                     boolean isAnonymnousPossible) {
        this.mainView = mainView;
        this.activity = activity;
        this.isAnonymnousPossible = isAnonymnousPossible;
    }

    public FirebaseAuthPresenterImpl(FirebaseAuthView mainView,
                                     FragmentActivity activity) {
        this.mainView = mainView;
        this.activity = activity;
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onCreate() {
        provider = new GoogleProvider(activity, getGoogleProvider());
    }

    @Override
    public void onDestroy() {
        if (provider != null) {
            provider.disconnect();
        }
    }

    @Override
    final public void firstAuthSignIn() {
        if (!isAnonymnousPossible) {
            if (mainView != null) {
                mainView.showLoadingDialog(R.string.progress_dialog_loading);
            }
            provider.setAuthenticationCallback(this);
            provider.startLogin(activity);
        } else {
            FirebaseAuth.getInstance().signInAnonymously()
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            if (mainView != null) {
                                mainView.signedInWithAnonymous();
                            }
                        } else {
                            if (mainView != null) {
                                mainView.signedInFailed();
                            }
                        }
                    });
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
        if (requestCode == GoogleProvider.RC_SIGN_IN) {
            provider.onActivityResult(requestCode, resultCode, data);
        } else if (mainView != null) {
            mainView.signedInFailed();
        }
    }


    @Override
    public void onSuccess(GoogleSignInAccount account) {
        AuthCredential credential = GoogleProvider.createAuthCredential(account);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnFailureListener(e -> {
                        if (mainView != null) {
                            mainView.signedInFailed();
                        }
                    })
                    .addOnCompleteListener(task -> {
                        if (mainView != null) {
                            mainView.dismissDialog();
                            if (task.isSuccessful()) {
                                mainView.signedInWithGoogle();
                            } else {
                                mainView.signedInFailed();
                            }
                        }
                    });
        }
    }

    @Override
    public void onFailure(Bundle extra) {
        // stay on this screen
        if (mainView != null) {
            mainView.signedInFailed();
        }
    }
}
