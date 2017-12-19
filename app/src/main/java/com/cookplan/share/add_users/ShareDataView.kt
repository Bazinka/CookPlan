package com.cookplan.share.add_users

import com.cookplan.models.Contact

/**
 * Created by DariaEfimova on 04.05.17.
 */

interface ShareDataView {

    fun setContactList(contactList: List<Contact>)

    fun setError(error: String)

    fun setShareSuccess(isFamilyModeTurnOn: Boolean)

    fun setShareError(errorResourceId: Int)
}