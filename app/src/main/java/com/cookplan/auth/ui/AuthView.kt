package com.cookplan.auth.ui

/**
 * Created by DariaEfimova on 10.04.17.
 */

interface AuthView {

    fun showSnackbar(messageRes: Int)

    fun signedInWithGoogle()

    fun signedInFailed()

    fun setError(errorResourceId: Int)
}
