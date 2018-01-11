package com.cookplan.auth.ui

import android.content.Intent

import com.google.firebase.auth.FirebaseUser

/**
 * Created by DariaEfimova on 10.04.17.
 */

interface AuthPresenter {
    val currentUser: FirebaseUser?

    fun onDestroy()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun firstAuthSignIn()
}
