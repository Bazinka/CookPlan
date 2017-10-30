package com.cookplan.main

import android.support.v4.app.FragmentActivity
import com.cookplan.R
import com.cookplan.auth.provider.GoogleProvider
import com.cookplan.auth.ui.AuthUI
import com.cookplan.auth.ui.FirebaseAuthPresenterImpl
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

/**
 * Created by DariaEfimova on 10.04.17.
 */

class MainPresenterImpl(private val mainView: MainView?, activity: FragmentActivity) : FirebaseAuthPresenterImpl(mainView, activity), MainPresenter, FirebaseAuth.AuthStateListener {

    init {
        this.activity = activity
    }

    override fun signIn() {
        if (FirebaseAuth.getInstance().currentUser?.isAnonymous ?: false) {
            mainView?.showLoadingDialog(R.string.progress_dialog_loading)
            provider = GoogleProvider(activity, googleProvider)
            provider.setAuthenticationCallback(this)
            provider.startLogin(activity)
        }
    }

    override fun signOut() {
        if (!(FirebaseAuth.getInstance().currentUser?.isAnonymous ?: false)) {
            mainView?.showLoadingDialog(R.string.progress_dialog_loading)
            AuthUI.getInstance()
                    .signOut(activity)
                    .addOnCompleteListener { task ->
                        mainView?.dismissDialog()
                        if (task.isSuccessful) {
                            mainView?.signedOut()
                        } else {
                            mainView?.showSnackbar(R.string.sign_out_failed)
                        }
                    }
        } else {
            mainView?.showSnackbar(R.string.sign_out_impossible)
        }
    }


    override fun onSuccess(account: GoogleSignInAccount) {
        val credential = GoogleProvider.createAuthCredential(account)
        val user = FirebaseAuth.getInstance().currentUser
        user?.linkWithCredential(credential)?.addOnFailureListener { e ->
            if (e is FirebaseAuthUserCollisionException) {
                //if other anonymnous has already linked to account, just enter, without matching
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnFailureListener { exp ->
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
            } else {
                mainView?.signedInFailed()
            }
        }?.addOnCompleteListener { task ->
            mainView?.dismissDialog()
            if (task.isSuccessful) {
                mainView?.signedInWithGoogle()
            } else if (task.exception !is FirebaseAuthUserCollisionException) {
                mainView?.signedInFailed()
            }
        }
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser != null) {
            if (firebaseAuth.currentUser?.isAnonymous ?: false) {
                mainView?.signedInWithAnonymous()
            } else {
                mainView?.signedInWithGoogle()
            }
        } else {
            mainView?.signedOut()
        }
    }

}
