package com.cookplan.main;

import com.cookplan.auth.ui.FirebaseAuthPresenter;
import com.cookplan.models.SharedData;

/**
 * Created by DariaEfimova on 10.04.17.
 */

public interface MainPresenter extends FirebaseAuthPresenter {

    public void signOut();

    public void signIn();

    public void shareData(String userEmail, SharedData data);
}
