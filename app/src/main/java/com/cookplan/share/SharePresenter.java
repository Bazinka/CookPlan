package com.cookplan.share;

/**
 * Created by DariaEfimova on 01.05.17.
 */

public interface SharePresenter {

    void shareData(String userEmail);

    void turnOffFamilyMode();

    void isFamilyModeTurnOnRequest();
}
