package com.cookplan.share;

import java.util.List;

/**
 * Created by DariaEfimova on 01.05.17.
 */

public interface SharePresenter {

    void shareData(List<String> emailsList);

    void turnOffFamilyMode();

    void isFamilyModeTurnOnRequest();
}
