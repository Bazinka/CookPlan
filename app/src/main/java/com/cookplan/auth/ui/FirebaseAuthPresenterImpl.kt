package com.cookplan.auth.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.cookplan.R
import com.cookplan.auth.provider.GoogleProvider
import com.cookplan.auth.provider.IdpProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.Scopes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

/**
 * Created by DariaEfimova on 10.04.17.
 */

open class FirebaseAuthPresenterImpl : FirebaseAuthPresenter, IdpProvider.IdpCallback {

    private var mainView: FirebaseAuthView? = null
    protected var activity: FragmentActivity
    protected var provider: GoogleProvider? = null

    override val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    constructor(mainView: FirebaseAuthView?,
                activity: FragmentActivity) {
        this.mainView = mainView
        this.activity = activity
    }

    override fun onDestroy() {
        provider?.disconnect()
    }

    override fun firstAuthSignIn() {
        mainView?.showLoadingDialog(R.string.progress_dialog_loading)

        if (provider == null) {
            provider = GoogleProvider(activity)
        }
        provider?.setAuthenticationCallback(this)
        provider?.startLogin(activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == GoogleProvider.RC_SIGN_IN) {
            provider?.onActivityResult(requestCode, resultCode, data)
        } else {
            mainView?.signedInFailed()
        }
    }


    override fun onSuccess(account: GoogleSignInAccount) {
        val credential = GoogleProvider.createAuthCredential(account)
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnFailureListener { e ->
                    mainView?.signedInFailed()
                }
                .addOnCompleteListener { task ->
                    mainView?.dismissDialog()
                    if (task.isSuccessful) {
                        mainView?.signedInWithGoogle()
                    } else {
                        mainView?.signedInFailed()
                    }
                }
    }

    override fun onFailure(extra: Bundle) {
        // stay on this screen
        mainView?.signedInFailed()
    }
}
