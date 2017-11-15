package com.cookplan.main

import android.support.v4.app.FragmentActivity
import com.cookplan.auth.provider.GoogleProvider
import com.cookplan.auth.ui.FirebaseAuthPresenterImpl
import com.cookplan.auth.ui.FirebaseAuthView
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

/**
 * Created by DariaEfimova on 10.04.17.
 */

class MainPresenterImpl(private val firebaseAuthView: FirebaseAuthView?, activity: FragmentActivity) : FirebaseAuthPresenterImpl(firebaseAuthView, activity), FirebaseAuth.AuthStateListener {

    init {
        this.activity = activity
    }

    override fun onSuccess(account: GoogleSignInAccount) {
        val credential = GoogleProvider.createAuthCredential(account)
        val user = FirebaseAuth.getInstance().currentUser
        user?.linkWithCredential(credential)?.addOnFailureListener { e ->
            if (e is FirebaseAuthUserCollisionException) {
                //if other anonymnous has already linked to account, just enter, without matching
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnFailureListener { exp ->
                            firebaseAuthView?.signedInFailed()
                        }
                        .addOnCompleteListener { task ->
                            firebaseAuthView?.dismissDialog()
                            if (task.isSuccessful) {
                                firebaseAuthView?.signedInWithGoogle()
                            } else {
                                firebaseAuthView?.signedInFailed()
                            }
                        }
            } else {
                firebaseAuthView?.signedInFailed()
            }
        }?.addOnCompleteListener { task ->
            firebaseAuthView?.dismissDialog()
            if (task.isSuccessful) {
                firebaseAuthView?.signedInWithGoogle()
            } else if (task.exception !is FirebaseAuthUserCollisionException) {
                firebaseAuthView?.signedInFailed()
            }
        }
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        firebaseAuthView?.signedInWithGoogle()
    }

}
