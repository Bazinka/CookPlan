package com.cookplan.auth.ui;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by DariaEfimova on 10.04.17.
 */

public interface FirebaseAuthPresenter {
    public FirebaseUser getCurrentUser();

    public void onDestroy();

    public void onActivityResult(int requestCode, int resultCode, Intent data);

    public void firstAuthSignIn();
}
