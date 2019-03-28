package com.cookplan.share.add_users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cookplan.R
import com.cookplan.models.Contact
import com.cookplan.share.add_users.ShareDataAdapter.MainViewHolder

/**
 * Created by DariaEfimova on 18.03.17.
 */

class ShareDataAdapter(private val changeListListener: () -> Unit) : RecyclerView.Adapter<MainViewHolder>() {

    val contactList: MutableList<Contact> = mutableListOf()
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
                changeListListener()
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
        changeListListener()
    }

    fun updateList(contacts: List<Contact>) {
        contactList.clear()
        contactList.addAll(contacts)
        notifyDataSetChanged()
    }
}