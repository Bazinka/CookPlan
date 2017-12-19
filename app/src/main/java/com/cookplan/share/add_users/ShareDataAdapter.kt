package com.cookplan.share.add_users

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cookplan.R
import com.cookplan.models.Contact
import com.cookplan.share.add_users.ShareDataAdapter.MainViewHolder

/**
 * Created by DariaEfimova on 18.03.17.
 */

class ShareDataAdapter(val contactList: MutableList<Contact> = mutableListOf()) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MainViewHolder =
            MainViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.user_list_for_sharing_item_layout, parent, false))

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val contact = contactList[position]
        holder.nameTextView.text = contact.name
        holder.emailTextView.text = contact.email
        with(holder.removeItemImageView) {
            visibility = View.VISIBLE
            tag = contact
            setOnClickListener {
                contactList.remove(contact)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var nameTextView: TextView = v.findViewById<TextView>(R.id.contact_item_name)
        var emailTextView: TextView = v.findViewById<TextView>(R.id.contact_item_email)
        var removeItemImageView: ImageView = v.findViewById<ImageView>(R.id.contact_remove_image_view)
        var mainView: ViewGroup = v.findViewById<ViewGroup>(R.id.main_view)

    }

    fun addItem(contact: Contact) {
        contactList.add(contact)
        notifyDataSetChanged()
    }

    fun addItemList(contacts: List<Contact>) {
        for (newContact in contacts) {
            var isContactAlreadyAdded = false
            for ((email) in contactList) {
                if (email == newContact.email) {
                    isContactAlreadyAdded = true
                    break
                }
            }
            if (!isContactAlreadyAdded) {
                contactList.add(newContact)
            }
        }
        notifyDataSetChanged()
    }

    fun updateList(contacts: List<Contact>) {
        contactList.clear()
        contactList.addAll(contacts)
        notifyDataSetChanged()
    }
}