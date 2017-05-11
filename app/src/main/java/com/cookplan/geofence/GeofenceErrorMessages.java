/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cookplan.geofence;

import com.cookplan.R;
import com.google.android.gms.location.GeofenceStatusCodes;

public class GeofenceErrorMessages {
    /**
     * Prevents instantiation.
     */
    private GeofenceErrorMessages() {
    }

    /**
     * Returns the error string for a geofencing error code.
     */
    public static int getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return R.string.geofence_not_available;
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return R.string.geofence_too_many_geofences;
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return R.string.geofence_too_many_pending_intents;
            default:
                return R.string.unknown_geofence_error;
        }
    }
}
