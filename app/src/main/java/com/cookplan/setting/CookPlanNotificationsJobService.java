package com.cookplan.setting;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.cookplan.R;
import com.cookplan.RApplication;
import com.cookplan.cooking_plan.list.CookingPlanPresenter;
import com.cookplan.cooking_plan.list.CookingPlanPresenterImpl;
import com.cookplan.cooking_plan.list.CookingPlanView;
import com.cookplan.main.MainActivity;
import com.cookplan.models.CookPlanError;
import com.cookplan.models.Ingredient;
import com.cookplan.models.Recipe;
import com.cookplan.utils.Constants;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Service to handle callbacks from the JobScheduler. Requests scheduled with the JobScheduler
 * ultimately land on this service's "onStartJob" method. It runs jobs for a specific amount of time
 * and finishes them. It keeps the activity updated with changes via a Messenger.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class CookPlanNotificationsJobService extends JobService {

    private static final String TAG = CookPlanNotificationsJobService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scheduleJob(RApplication.getAppContext());
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        BehaviorSubject<List<Object>> subjectObjectList = BehaviorSubject.create();

        CookingPlanPresenter presenter = new CookingPlanPresenterImpl(new CookingPlanView() {
            @Override
            public void setErrorToast(String error) {
                subjectObjectList.onError(new CookPlanError(error));
            }

            @Override
            public void setCookingList(Map<LocalDate, List<Object>> dateToObjectMap) {
                LocalDate tomorrow = new LocalDate().plusDays(1);
                List<Object> resultList = dateToObjectMap.get(tomorrow);
                resultList = resultList == null ? new ArrayList<>() : resultList;
                subjectObjectList.onNext(resultList);
                subjectObjectList.onComplete();
            }

            @Override
            public void setEmptyView() {
                subjectObjectList.onNext(new ArrayList<>());
                subjectObjectList.onComplete();
            }
        });
        presenter.getCookingPlan();
        presenter.onStop();

        subjectObjectList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Object>>() {
                    @Override
                    public void onNext(List<Object> objects) {
                        if (!objects.isEmpty()) {
                            sendNotification(objects);
                        }
                        scheduleJob(RApplication.getAppContext());
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        jobFinished(params, false);
                    }
                });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "on stop job: " + params.getJobId());
        return false;
    }

    private void sendNotification(List<Object> objects) {
        String namesObjects = "";
        for (Object object : objects) {
            if (!namesObjects.isEmpty()) {
                namesObjects = namesObjects + ", ";
            }
            if (object instanceof Recipe) {
                namesObjects = namesObjects + ((Recipe) object).getName();
            } else if (object instanceof Ingredient) {
                namesObjects = namesObjects + ((Ingredient) object).getName();
            }
        }
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
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
                .setContentTitle("На завтра у вас: " + objects.size() + " блюд.")
                .setContentText(namesObjects)
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, builder.build());
    }

    /**
     * Executed when user clicks on SCHEDULE JOB.
     */
    //    public void scheduleJob() {
    //        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, new ComponentName(this, MyJobService.class));
    //
    //        String delay = mDelayEditText.getText().toString();
    //        if (!TextUtils.isEmpty(delay)) {
    //            builder.setMinimumLatency(Long.valueOf(delay) * 1000);
    //        }
    //        String deadline = mDeadlineEditText.getText().toString();
    //        if (!TextUtils.isEmpty(deadline)) {
    //            builder.setOverrideDeadline(Long.valueOf(deadline) * 1000);
    //        }
    //        boolean requiresUnmetered = mWiFiConnectivityRadioButton.isChecked();
    //        boolean requiresAnyConnectivity = mAnyConnectivityRadioButton.isChecked();
    //        if (requiresUnmetered) {
    //            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
    //        } else if (requiresAnyConnectivity) {
    //            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
    //        }
    //        builder.setRequiresDeviceIdle(mRequiresIdleCheckbox.isChecked());
    //        builder.setRequiresCharging(mRequiresChargingCheckBox.isChecked());
    //
    //        // Extras, work duration.
    //        PersistableBundle extras = new PersistableBundle();
    //        String workDuration = mDurationTimeEditText.getText().toString();
    //        if (TextUtils.isEmpty(workDuration)) {
    //            workDuration = "1";
    //        }
    //        extras.putLong(WORK_DURATION_KEY, Long.valueOf(workDuration) * 1000);
    //
    //        builder.setExtras(extras);
    //
    //        // Schedule job
    //        Log.d(TAG, "Scheduling job");
    //        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
    //        tm.schedule(builder.build());
    //    }

    // schedule the start of the service every 10 - 30 seconds
    private void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, CookPlanNotificationsJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(Constants.DELAY_COOKPLAN_NOTIFICATION_JOB); // wait at least
        builder.setOverrideDeadline(Constants.DELAY_COOKPLAN_NOTIFICATION_JOB); // maximum delay
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}
