package com.cookplan.share.add_users;

import com.cookplan.models.Contact;

import java.util.List;

/**
 * Created by DariaEfimova on 04.05.17.
 */

public interface AddUserForSharingView {

    void setContactList(List<Contact> contactList);

    void setError(String error);
}
