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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.cookplan.auth.AuthUI.IdpConfig;
import com.cookplan.auth.util.Preconditions;

import java.util.List;

/**
 * Encapsulates the core parameters and data captured during the authentication flow, in
 * a serializable manner, in order to pass data between activities.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class FlowParameters implements Parcelable {

    @NonNull
    public final List<IdpConfig> providerInfo;

    //    public final boolean smartLockEnabled;

    public FlowParameters(
            @NonNull List<IdpConfig> providerInfo) {//,boolean smartLockEnabled)
        this.providerInfo = Preconditions.checkNotNull(providerInfo, "providerInfo cannot be null");
        //        this.smartLockEnabled = smartLockEnabled;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(providerInfo);
        //        dest.writeInt(smartLockEnabled ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlowParameters> CREATOR = new Creator<FlowParameters>() {
        @Override
        public FlowParameters createFromParcel(Parcel in) {
            List<IdpConfig> providerInfo = in.createTypedArrayList(IdpConfig.CREATOR);

            return new FlowParameters(
                    providerInfo);
        }

        @Override
        public FlowParameters[] newArray(int size) {
            return new FlowParameters[size];
        }
    };
}
