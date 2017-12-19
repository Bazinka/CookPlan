package com.cookplan.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by DariaEfimova on 03.05.17.
 */

data class Contact(var email: String = String(), var name: String = String(), var phoneUrl: String = String()) : Parcelable {

    constructor(emailAddress: String) : this(email = emailAddress)

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(email)
        writeString(name)
        writeString(phoneUrl)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Contact> = object : Parcelable.Creator<Contact> {
            override fun createFromParcel(source: Parcel): Contact = Contact(source)
            override fun newArray(size: Int): Array<Contact?> = arrayOfNulls(size)
        }
    }
}
