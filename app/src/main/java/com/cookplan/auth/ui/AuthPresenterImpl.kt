package com.cookplan.auth.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.cookplan.R
import com.cookplan.auth.provider.GoogleProvider
import com.cookplan.auth.provider.IdpProvider
import com.cookplan.models.ShareUserInfo
import com.cookplan.providers.FamilyModeProvider
import com.cookplan.providers.impl.FamilyModeProviderImpl
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.MaybeObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by DariaEfimova on 10.04.17.
 */

open class AuthPresenterImpl : AuthPresenter, IdpProvider.IdpCallback {

    private var mainView: AuthView? = null
    protected var activity: FragmentActivity
    protected var provider: GoogleProvider? = null

    private val familyModeProvider: FamilyModeProvider = FamilyModeProviderImpl()

    override val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    constructor(mainView: AuthView?,
                activity: FragmentActivity) {
        this.mainView = mainView
        this.activity = activity
    }

    override fun onDestroy() {
        provider?.disconnect()
    }

    override fun firstAuthSignIn() {

        if (provider == null) {
            provider = GoogleProvider(activity)
        }
        provider?.setAuthenticationCallback(this)
        provider?.startLogin(activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

    override fun isFamilyModeTurnOnRequest() {
        familyModeProvider.getDataSharedByMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : MaybeObserver<ShareUserInfo> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(shareUserInfo: ShareUserInfo) {
                        mainView?.goToNextScreen(true)
                    }

                    override fun onError(e: Throwable) {
                        mainView?.setError(R.string.error_share_data_loading)
                    }

                    override fun onComplete() {
                        mainView?.goToNextScreen(false)
                    }
                })

    }
}
