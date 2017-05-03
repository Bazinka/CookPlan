package com.cookplan.share.add_users;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Contact;

import java.util.List;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MainViewHolder> {

    private List<Contact> contactList;
    private boolean isRemovalApprove;

    public ContactListAdapter(List<Contact> contactList, boolean isRemovalApprove) {
        this.contactList = contactList;
        this.isRemovalApprove = isRemovalApprove;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item_layout, parent, false);

        MainViewHolder vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.nameTextView.setText(contact.getName());
        holder.emailTextView.setText(contact.getEmail());
        if (isRemovalApprove) {
            holder.removeItemImageView.setVisibility(View.VISIBLE);
            holder.removeItemImageView.setTag(contact);
            holder.removeItemImageView.setOnClickListener(v -> {
                Contact selectedContact = (Contact) v.getTag();
                if (selectedContact != null) {
                    contactList.remove(selectedContact);
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.removeItemImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        ImageView removeItemImageView;
        ViewGroup mainView;

        MainViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.contact_item_name);
            emailTextView = (TextView) v.findViewById(R.id.contact_item_email);
            removeItemImageView = (ImageView) v.findViewById(R.id.contact_remove_image_view);
            mainView = (ViewGroup) v.findViewById(R.id.main_view);
        }
    }

    public void addItem(Contact contact) {
        contactList.add(contact);
        notifyDataSetChanged();
    }

    public void addItemList(List<Contact> contactList) {
        contactList.addAll(contactList);
        notifyDataSetChanged();
    }

    public List<Contact> getContactList() {
        return contactList;
    }

}