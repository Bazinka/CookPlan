package com.cookplan.main;

import com.cookplan.auth.ui.FirebaseAuthPresenter;

/**
 * Created by DariaEfimova on 10.04.17.
 */

public interface MainPresenter extends FirebaseAuthPresenter {

    public void signOut();

    public void signIn();
}
