package com.cookplan.main;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.cookplan.R;
import com.cookplan.auth.provider.GoogleProvider;
import com.cookplan.auth.ui.AuthUI;
import com.cookplan.auth.ui.FirebaseAuthPresenterImpl;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by DariaEfimova on 10.04.17.
 */

public class MainPresenterImpl extends FirebaseAuthPresenterImpl implements MainPresenter,
        FirebaseAuth.AuthStateListener {

    private MainView mainView;

    public MainPresenterImpl(MainView mainView, FragmentActivity activity) {
        super(mainView, activity);
        this.mainView = mainView;
        this.activity = activity;
    }

    @Override
    public void signIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null
                && FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
            if (mainView != null) {
                mainView.showLoadingDialog(R.string.progress_dialog_loading);
            }
            provider.setAuthenticationCallback(this);
            provider.startLogin(activity);
        }
    }

    @Override
    public void signOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null
                && !FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
            if (mainView != null) {
                mainView.showLoadingDialog(R.string.progress_dialog_loading);
            }
            AuthUI.getInstance()
                    .signOut(activity)
                    .addOnCompleteListener(task -> {
                        if (mainView != null) {
                            mainView.dismissDialog();
                            if (task.isSuccessful()) {
                                mainView.signedOut();
                            } else {
                                mainView.showSnackbar(R.string.sign_out_failed);
                            }
                        }
                    });
        } else {
            if (mainView != null) {
                mainView.showSnackbar(R.string.sign_out_impossible);
            }
        }
    }


    @Override
    public void onSuccess(GoogleSignInAccount account) {
        AuthCredential credential = GoogleProvider.createAuthCredential(account);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            OnCompleteListener<AuthResult> successTask = task -> {
                if (mainView != null) {
                    mainView.dismissDialog();
                    if (task.isSuccessful()) {
                        mainView.signedInWithGoogle();
                    } else {
                        mainView.signedInFailed();
                    }
                }
            };
            user.linkWithCredential(credential)
                    .addOnFailureListener(e -> {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            //if other anonymnous has already linked to account, just enter, without matching
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                    .addOnFailureListener(exp -> {
                                        if (mainView != null) {
                                            mainView.signedInFailed();
                                        }
                                    })
                                    .addOnCompleteListener(successTask);
                        } else {
                            if (mainView != null) {
                                mainView.signedInFailed();
                            }
                        }
                    })
                    .addOnCompleteListener(successTask);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (mainView != null) {
            if (firebaseAuth.getCurrentUser() != null) {
                if (firebaseAuth.getCurrentUser().isAnonymous()) {
                    mainView.signedInWithAnonymous();
                } else {
                    mainView.signedInWithGoogle();
                }
            } else {
                mainView.signedOut();
            }
        }
    }

}
