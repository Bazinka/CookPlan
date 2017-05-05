package com.cookplan.share.contact_list;

import com.cookplan.models.Contact;

import java.util.ArrayList;

/**
 * Created by DariaEfimova on 04.05.17.
 */

public interface ContactListView {


    void setContactList(ArrayList<Contact> contactList);

    void setError(String error);
}
