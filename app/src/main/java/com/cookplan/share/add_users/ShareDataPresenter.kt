package com.cookplan.share.add_users

/**
 * Created by DariaEfimova on 03.05.17.
 */

interface ShareDataPresenter {

    fun getSharedUsers()

    fun shareData(emailsList: MutableList<String>)

    fun turnOffFamilyMode()
}