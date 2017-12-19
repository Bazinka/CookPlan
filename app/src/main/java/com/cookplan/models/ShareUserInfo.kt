package com.cookplan.models

/**
 * Created by DariaEfimova on 12.04.17.
 */

data class ShareUserInfo(var id: String? = null,
                         var ownerUserId: String = String(),
                         var ownerUserName: String = String(),
                         var clientUserEmailList: MutableList<String> = mutableListOf<String>())