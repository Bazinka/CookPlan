package com.cookplan.setting;

/**
 * Created by DariaEfimova on 07.07.17.
 */

public interface SettingView {

    void setCookPlanNotificationChanged(boolean turnedOn);

    void setError(String error);
}
