package com.cookplan.models

import com.google.firebase.auth.FirebaseUser

/**
 * Created by DariaEfimova on 19.12.2017.
 */
object ShareUserInfoFactory {

    fun createShareUserInfo(user: FirebaseUser?,
                            userEmailList: MutableList<String> = mutableListOf<String>()): ShareUserInfo? {
        if (user != null) {
            return ShareUserInfo(id = null,
                    ownerUserId = user.uid,
                    ownerUserName = user.displayName ?: String(),
                    clientUserEmailList = userEmailList)
        }
        return null
    }
}