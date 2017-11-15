package com.cookplan.auth.ui

import android.support.annotation.StringRes

/**
 * Created by DariaEfimova on 10.04.17.
 */

interface FirebaseAuthView {

    fun showSnackbar(messageRes: Int)

    fun dismissDialog()

    fun showLoadingDialog(@StringRes stringResource: Int)

    fun showLoadingDialog(message: String)

    fun signedInWithGoogle()

    fun signedInFailed()
}
