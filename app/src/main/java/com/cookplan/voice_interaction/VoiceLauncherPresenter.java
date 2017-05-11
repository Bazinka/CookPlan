package com.cookplan.voice_interaction;

/**
 * Created by DariaEfimova on 08.05.17.
 */

public interface VoiceLauncherPresenter {

    void handleText(String text);

    void onStop();
}
