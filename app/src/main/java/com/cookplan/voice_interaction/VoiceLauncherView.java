package com.cookplan.voice_interaction;

import android.app.VoiceInteractor;

/**
 * Created by DariaEfimova on 08.05.17.
 */

public interface VoiceLauncherView {

    void startVoiceTrigger(VoiceInteractor.PickOptionRequest request);

    void setSuccessSaveIngredient();

    void setErrorString(String string);
}
