package com.cookplan.setting;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.cookplan.BaseActivity;
import com.cookplan.R;

import static com.cookplan.RApplication.isCookPlanNotificationTurnedOn;

public class SettingsActivity extends BaseActivity implements SettingView {

    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_cooking_plan);
        setNavigationArrow();
        setTitle(getString(R.string.settings_title));

        presenter = new SettingsPresenterImpl(this, this);
        SwitchCompat switchCookPlanNotification = (SwitchCompat) findViewById(R.id.notification_switch_compat);
        switchCookPlanNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (presenter != null) {
                presenter.saveCookPlanNotificationChange(isChecked);
            }
        });
        switchCookPlanNotification.setChecked(isCookPlanNotificationTurnedOn());
    }

    @Override
    public void setCookPlanNotificationChanged(boolean turnedOn) {
        SwitchCompat switchCookPlanNotification = (SwitchCompat) findViewById(R.id.notification_switch_compat);
        switchCookPlanNotification.setChecked(turnedOn);
    }

    @Override
    public void setError(String error) {
        View mainView = findViewById(R.id.main_view);
        if (mainView != null) {
            Snackbar.make(mainView, error, Snackbar.LENGTH_LONG).show();
        }
    }
}