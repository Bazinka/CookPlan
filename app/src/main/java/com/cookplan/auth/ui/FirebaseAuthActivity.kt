package com.cookplan.auth.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Button
import com.cookplan.BaseActivity
import com.cookplan.R
import com.cookplan.main.MainActivity

class FirebaseAuthActivity : BaseActivity(), FirebaseAuthView {


    private var mProgressDialog: ProgressDialog? = null
    private var rootView: View? = null

    private var presenter: FirebaseAuthPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_auth)
        rootView = findViewById(R.id.firebase_auth_root)
        presenter = FirebaseAuthPresenterImpl(this, this)
    }

    override fun onStart() {
        super.onStart()
        presenter?.firstAuthSignIn()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter?.onActivityResult(requestCode, resultCode, data)
    }

    private fun startMainActivity() {
        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivityWithLeftAnimation(intent)
        finish()
    }


    override fun showSnackbar(messageRes: Int) {
        Snackbar.make(rootView!!, messageRes, Snackbar.LENGTH_LONG).show()
    }

    override fun showLoadingDialog(message: String) {
        dismissDialog()
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.isIndeterminate = true
        mProgressDialog?.setTitle("")

        mProgressDialog?.setMessage(message)
        mProgressDialog?.show()
    }

    override fun signedInWithGoogle() {
        startMainActivity()
    }

    override fun signedInFailed() {
        dismissDialog()
        showSnackbar(R.string.unknown_sign_in_response)

        val signInButton = findViewById<View>(R.id.sign_in_google_button) as Button
        signInButton.visibility = View.VISIBLE
        signInButton.setOnClickListener { v ->
            presenter?.firstAuthSignIn()
        }
    }

    override fun showLoadingDialog(@StringRes stringResource: Int) {
        showLoadingDialog(getString(stringResource))
    }

    override fun dismissDialog() {
        mProgressDialog?.dismiss()
        mProgressDialog = null
    }

}