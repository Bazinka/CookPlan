package com.cookplan.models

/**
 * Created by DariaEfimova on 12.04.17.
 */

data class ShareUserInfo(var id: String? = null,
                         var ownerUserId: String? = null,
                         var ownerUserName: String? = null,
                         var clientUserEmailList: List<String> = listOf()) {
    constructor(userId: String,
                userName: String,
                userEmailList: List<String>) : this(ownerUserId = userId,
            ownerUserName = userName,
            clientUserEmailList = userEmailList)
}
