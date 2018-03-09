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

package com.cookplan.geofence.responce;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.companies.review.CompanyReviewActivity;
import com.cookplan.geofence.GeofenceErrorMessages;
import com.cookplan.main.MainActivity;
import com.cookplan.models.Company;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import static com.cookplan.companies.review.CompanyReviewActivity.COMPANY_OBJECT_KEY;

/**
 * Listener for geofence transition changes.
 * <p>
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a notification
 * as the output.
 */
public class GeofenceTransitionsIntentService extends IntentService implements GeofenceResponseView {

    protected static final String TAG = "GeofenceTransitionsIS";

    private GeofenceResponsePresenter presenter;

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            int errorMessage = GeofenceErrorMessages.getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, getApplicationContext().getString(errorMessage));
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            presenter = new GeofenceResponsePresenterImpl(this);
            for (Geofence geofence : triggeringGeofences) {
                presenter.getCompanyById(geofence.getRequestId());
            }

        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }


    private void sendNotification(Company company) {
        Intent notificationIntent = new Intent(getApplicationContext(), CompanyReviewActivity.class);
        notificationIntent.putExtra(COMPANY_OBJECT_KEY, company);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.drawable.main_drawable)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                                           R.drawable.main_drawable))
                .setColor(getResources().getColor(R.color.primary, getTheme()))
                .setContentTitle(getString(R.string.you_entered_title) + company.getName())
                .setContentText(getString(R.string.open_from_notification_title))
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, builder.build());
    }

    @Override
    public void setCompany(Company company) {
        sendNotification(company);
    }
}
