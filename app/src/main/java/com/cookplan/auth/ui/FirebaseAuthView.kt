package com.cookplan.auth.ui

import android.support.annotation.StringRes

/**
 * Created by DariaEfimova on 10.04.17.
 */

interface FirebaseAuthView {

    fun showSnackbar(messageRes: Int)

    fun signedInWithGoogle()

    fun signedInFailed()
}
