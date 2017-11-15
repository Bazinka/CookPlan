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

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IdpProvider {

    val providerId: String

    /**
     * Retrieves the name of the IDP, for display on-screen.
     */
    fun getName(context: Context): String

    fun setAuthenticationCallback(callback: IdpCallback)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)

    fun startLogin(activity: Activity)

    interface IdpCallback {
        fun onSuccess(account: GoogleSignInAccount)

        fun onFailure(extra: Bundle)
    }
}
