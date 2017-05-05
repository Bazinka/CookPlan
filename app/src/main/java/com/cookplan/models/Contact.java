package com.cookplan.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DariaEfimova on 03.05.17.
 */

public class Contact implements Parcelable {

    private String email;
    private String name;
    private String phoneUrl;

    public Contact() {
    }

    public Contact(String email) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneUrl() {
        return phoneUrl;
    }

    public void setPhoneUrl(String phone_url) {
        this.phoneUrl = phone_url;
    }

    // Parcelling part
    public Contact(Parcel in) {

        name = in.readString();
        email = in.readString();
        phoneUrl = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneUrl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
