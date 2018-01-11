package com.cookplan.share.add_users

import com.cookplan.models.Contact
import com.cookplan.models.ShareUserInfo

/**
 * Created by DariaEfimova on 04.05.17.
 */

interface ShareDataView {

    fun setContactList(contactList: List<Contact>)

    fun setError(errorResourceId: Int)
}