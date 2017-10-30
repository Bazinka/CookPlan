package com.cookplan.main

import com.cookplan.auth.ui.FirebaseAuthPresenter

/**
 * Created by DariaEfimova on 10.04.17.
 */

interface MainPresenter : FirebaseAuthPresenter {

    fun signOut()

    fun signIn()
}
