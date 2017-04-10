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

package com.cookplan.auth.ui;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cookplan.auth.util.CredentialsApiHelper;
import com.cookplan.auth.util.GoogleApiClientTaskHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class AuthUI {

    public static final String GOOGLE_PROVIDER = GoogleAuthProvider.PROVIDER_ID;


    /**
     * The set of authentication providers supported in Firebase Auth UI.
     */
    public static final Set<String> SUPPORTED_PROVIDERS =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                    GOOGLE_PROVIDER
            )));

    private static final IdentityHashMap<FirebaseApp, AuthUI> INSTANCES = new IdentityHashMap<>();

    private final FirebaseApp mApp;
    private final FirebaseAuth mAuth;

    private AuthUI(FirebaseApp app) {
        mApp = app;
        mAuth = FirebaseAuth.getInstance(mApp);
    }

    /**
     * Retrieves the {@link AuthUI} instance associated with the default app, as returned by
     * {@code FirebaseApp.getInstance()}.
     *
     * @throws IllegalStateException if the default app is not initialized.
     */
    public static AuthUI getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    /**
     * Retrieves the {@link AuthUI} instance associated the the specified app.
     */
    public static AuthUI getInstance(FirebaseApp app) {
        AuthUI authUi;
        synchronized (INSTANCES) {
            authUi = INSTANCES.get(app);
            if (authUi == null) {
                authUi = new AuthUI(app);
                INSTANCES.put(app, authUi);
            }
        }
        return authUi;
    }

    public Task<Void> signOut(@NonNull Activity activity) {
        // Get helper for Google Sign In and Credentials API
        GoogleApiClientTaskHelper taskHelper = GoogleApiClientTaskHelper.getInstance(activity);
        taskHelper.getBuilder()
                .addApi(Auth.CREDENTIALS_API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, GoogleSignInOptions.DEFAULT_SIGN_IN);

        // Get Credentials Helper
        CredentialsApiHelper credentialsHelper = CredentialsApiHelper.getInstance(taskHelper);

        // Firebase Sign out
        mAuth.signOut();

        // Disable credentials auto sign-in
        Task<Status> disableCredentialsTask = credentialsHelper.disableAutoSignIn();

        // Google sign out
        Task<Void> googleSignOutTask = taskHelper.getConnectedGoogleApiClient()
                .continueWith(new Continuation<GoogleApiClient, Void>() {
                    @Override
                    public Void then(@NonNull Task<GoogleApiClient> task) throws Exception {
                        if (task.isSuccessful()) {
                            Auth.GoogleSignInApi.signOut(task.getResult());
                        }
                        return null;
                    }
                });

        // Wait for all tasks to complete
        return Tasks.whenAll(disableCredentialsTask, googleSignOutTask);
    }

    /**
     * Configuration for an identity provider.
     * <p>
     * In the simplest case, you can supply the provider ID and build the config like this:
     * {@code new IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()}
     */
    public static class IdpConfig implements Parcelable {
        private final String mProviderId;
        private final List<String> mScopes;

        private IdpConfig(@NonNull String providerId, List<String> scopes) {
            mScopes = scopes;
            mProviderId = providerId;
        }

        private IdpConfig(Parcel in) {
            mProviderId = in.readString();
            mScopes = in.createStringArrayList();
        }

        public String getProviderId() {
            return mProviderId;
        }

        public List<String> getScopes() {
            return mScopes;
        }

        public static final Creator<IdpConfig> CREATOR = new Creator<IdpConfig>() {
            @Override
            public IdpConfig createFromParcel(Parcel in) {
                return new IdpConfig(in);
            }

            @Override
            public IdpConfig[] newArray(int size) {
                return new IdpConfig[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(mProviderId);
            parcel.writeStringList(mScopes);
        }

        public static class Builder {
            private String mProviderId;
            private List<String> mScopes = new ArrayList<>();


            /**
             * Builds the configuration parameters for an identity provider.
             *
             * @param providerId An ID of one of the supported identity providers. e.g.
             *                   {@link AuthUI#GOOGLE_PROVIDER}. See {@link AuthUI#SUPPORTED_PROVIDERS} for the
             *                   complete list of supported Identity providers
             */
            public Builder(@NonNull String providerId) {
                if (!SUPPORTED_PROVIDERS.contains(providerId)) {
                    throw new IllegalArgumentException("Unkown provider: " + providerId);
                }
                mProviderId = providerId;
            }

            /**
             * Specifies the additional permissions that the application will request for this
             * identity provider.
             * <p>
             * For Facebook permissions see:
             * https://developers.facebook.com/docs/facebook-login/android
             * https://developers.facebook.com/docs/facebook-login/permissions
             * <p>
             * For Google permissions see:
             * https://developers.google.com/identity/protocols/googlescopes
             * <p>
             * Twitter permissions are only configurable through the Twitter developer console.
             */
            public Builder setPermissions(List<String> permissions) {
                mScopes = permissions;
                return this;
            }

            public IdpConfig build() {
                return new IdpConfig(mProviderId, mScopes);
            }
        }
    }

}
