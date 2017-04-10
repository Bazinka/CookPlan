package com.cookplan.main;

import com.cookplan.auth.ui.FirebaseAuthView;

/**
 * Created by DariaEfimova on 10.04.17.
 */

public interface MainView extends FirebaseAuthView {

    public void signedOut();
}
