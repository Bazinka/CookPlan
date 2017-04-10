package com.cookplan.auth.ui;

import android.support.annotation.StringRes;

/**
 * Created by DariaEfimova on 10.04.17.
 */

public interface FirebaseAuthView {

    public void showSnackbar(int messageRes);

    public void dismissDialog();

    public void showLoadingDialog(@StringRes int stringResource);

    public void showLoadingDialog(String message);

    public void signedInWithAnonymous();

    public void signedInWithGoogle();

    public void signedInFailed();
}
