package com.cookplan.share.contact_list;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.cookplan.models.Contact;

import java.util.ArrayList;

/**
 * Created by DariaEfimova on 04.05.17.
 */

public class ContactListPresenterImpl implements ContactListPresenter {
    private ContactListView mainView;
    private Context context;

    public ContactListPresenterImpl(ContactListView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
    }

    @Override
    public void getContactList() {
        ArrayList<Contact> contactList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                              null, null, null);
        if (cur != null && cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneUrl = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
                Cursor emailCur = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                String email = null;
                while (emailCur.moveToNext()) {
                    email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Contact contact = new Contact();
                    contact.setEmail(email);
                    contact.setName(name);
                    contact.setPhoneUrl(phoneUrl);
                    contactList.add(contact);
                }
                emailCur.close();

            }
        }


        if (mainView != null) {
            mainView.setContactList(contactList);
        }

    }
}
