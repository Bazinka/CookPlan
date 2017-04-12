package com.cookplan.main;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.cookplan.R;
import com.cookplan.auth.provider.GoogleProvider;
import com.cookplan.auth.ui.AuthUI;
import com.cookplan.auth.ui.FirebaseAuthPresenterImpl;
import com.cookplan.models.ShareUserInfo;
import com.cookplan.models.SharedData;
import com.cookplan.utils.DatabaseConstants;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
            provider = new GoogleProvider(activity, getGoogleProvider());
            provider.setAuthenticationCallback(this);
            provider.startLogin(activity);
        }
    }

    @Override
    public void shareData(String userEmail, SharedData data) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String myUid = user.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query sharedItems = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE)
                    .orderByChild(DatabaseConstants.DATABASE_CLIENT_USER_EMAIL_FIELD)
                    .equalTo(userEmail);
            sharedItems.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<ShareUserInfo> shareUserInfos = new ArrayList<>();
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            ShareUserInfo userInfo = itemSnapshot.getValue(ShareUserInfo.class);
                            if (userInfo.getSharedData() == data
                                    && userInfo.getOwnerUserId().equals(myUid)) {
                                shareUserInfos.add(userInfo);
                            }
                        }
                    }
                    if (shareUserInfos.isEmpty()) {
                        DatabaseReference userShareRef = database.child(DatabaseConstants.DATABASE_SHARE_TO_GOOGLE_USER_TABLE);
                        userShareRef.push().setValue(new ShareUserInfo(myUid, user.getDisplayName(), userEmail, data),
                                                     (databaseError, reference) -> {
                                                         if (databaseError != null) {
                                                             if (mainView != null) {
                                                                 mainView.showSnackbar(R.string.error_share_title);
                                                             }
                                                         } else {
                                                             if (mainView != null) {
                                                                 mainView.showSnackbar(R.string.share_success_title);
                                                             }
                                                         }
                                                     });
                    } else {
                        if (mainView != null) {
                            mainView.showSnackbar(R.string.have_already_shared);
                        }
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        mainView.showSnackbar(R.string.error_share_title);
                    }
                }
            });
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
                        } else {
                            if (mainView != null) {
                                mainView.signedInFailed();
                            }
                        }
                    })
                    .addOnCompleteListener(task -> {
                        if (mainView != null) {
                            mainView.dismissDialog();
                            if (task.isSuccessful()) {
                                mainView.signedInWithGoogle();
                            } else if (!(task.getException() instanceof FirebaseAuthUserCollisionException)) {
                                mainView.signedInFailed();
                            }
                        }
                    });
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
