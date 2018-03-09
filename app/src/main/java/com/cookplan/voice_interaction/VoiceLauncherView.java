package com.cookplan.voice_interaction;

import android.app.VoiceInteractor;

import com.cookplan.BaseView;

/**
 * Created by DariaEfimova on 08.05.17.
 */

public interface VoiceLauncherView extends BaseView {

    void startVoiceTrigger(VoiceInteractor.PickOptionRequest request);

    void setSuccessOperationResult();

    void setErrorString(String string);
}
