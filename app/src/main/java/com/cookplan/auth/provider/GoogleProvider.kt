/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cookplan.auth.provider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import com.cookplan.R
import com.cookplan.auth.util.GoogleApiConstants
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class GoogleProvider @JvmOverloads constructor(activity: FragmentActivity, email: String? = null) : IdpProvider, OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleApiClient: GoogleApiClient? = null
    private val mActivity: Activity
    private var mIDPCallback: IdpProvider.IdpCallback? = null

    override val providerId: String
        get() = GoogleAuthProvider.PROVIDER_ID

    init {
        mActivity = activity
        val clientId = activity.getString(R.string.default_web_client_id)

        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(clientId)

        if (activity.resources.getIdentifier(
                "google_permissions", "array", activity.packageName) != 0) {
            Log.w(TAG, "DEVELOPER WARNING: You have defined R.array.google_permissions but that is"
                    + " no longer respected as of FirebaseUI 1.0.0. Please see README for IDP scope"
                    + " configuration instructions.")
        }

        // Add additional scopes
        builder.requestScopes(Scope(Scopes.DRIVE_FILE))

        if (!TextUtils.isEmpty(email)) {
            builder.setAccountName(email)
        }

        if (!(mGoogleApiClient?.isConnected ?: false)) {
            mGoogleApiClient = GoogleApiClient.Builder(activity)
                    .enableAutoManage(activity, GoogleApiConstants.AUTO_MANAGE_ID0, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, builder.build())
                    .build()
            mGoogleApiClient?.connect()
        }
    }

    override fun getName(context: Context): String {
        return context.resources.getString(R.string.idp_name_google)
    }

    override fun setAuthenticationCallback(callback: IdpProvider.IdpCallback) {
        mIDPCallback = callback
    }

    fun disconnect() {
        mGoogleApiClient?.disconnect()
        mGoogleApiClient = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null) {
                val account: GoogleSignInAccount? = result.signInAccount
                if (result.isSuccess && account != null) {
                    mIDPCallback?.onSuccess(account)
                } else {
                    onError(result)
                }
            } else {
                onError("No result found in intent")
            }
        }
    }

    override fun startLogin(activity: Activity) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun onError(result: GoogleSignInResult) {
        val errorMessage = result.status.statusMessage
        onError(result.status.statusCode.toString() + " " + errorMessage)
    }

    private fun onError(errorMessage: String) {
        Log.e(TAG, "Error logging in with Google. " + errorMessage)
        val extra = Bundle()
        extra.putString(ERROR_KEY, errorMessage)
        mIDPCallback?.onFailure(extra)
    }

    override fun onClick(view: View) {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
        startLogin(mActivity)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.w(TAG, "onConnectionFailed:" + connectionResult)
    }

    companion object {
        private val TAG = "GoogleProvider"
        val RC_SIGN_IN = 20
        private val ERROR_KEY = "error"

        fun createAuthCredential(account: GoogleSignInAccount): AuthCredential {
            return GoogleAuthProvider.getCredential(account.idToken, null)
        }
    }
}

