package com.cookplan.setting;

import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import static com.cookplan.RApplication.saveCookPlanNotificationChanged;

/**
 * Created by DariaEfimova on 07.07.17.
 */

public class SettingsPresenterImpl implements SettingsPresenter {
    private SettingView mainView;
    private Context context;

    public SettingsPresenterImpl(SettingView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
    }

    @Override
    public void saveCookPlanNotificationChange(boolean isNeedToTurnOn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isNeedToTurnOn) {
                Intent startServiceIntent = new Intent(context, CookPlanNotificationsJobService.class);
                context.startService(startServiceIntent);
            } else {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.cancelAll();
                context.stopService(new Intent(context, CookPlanNotificationsJobService.class));
            }
            saveCookPlanNotificationChanged(isNeedToTurnOn);
            if (mainView != null) {
                mainView.setCookPlanNotificationChanged(isNeedToTurnOn);
            }
        } else {
            if (mainView != null) {
                mainView.setError("Чтобы включить напоминания, установите последнюю версию системы на телефон");
                mainView.setCookPlanNotificationChanged(!isNeedToTurnOn);
            }
        }
    }
}
