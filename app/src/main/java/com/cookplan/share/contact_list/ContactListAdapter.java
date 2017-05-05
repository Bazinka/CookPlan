package com.cookplan.share.contact_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;
import com.cookplan.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DariaEfimova on 18.03.17.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MainViewHolder> {

    private ArrayList<Contact> contactList;
    private ArrayList<Contact> selectedContactList;

    public ContactListAdapter(ArrayList<Contact> contactList) {
        this.contactList = contactList;
        selectedContactList = new ArrayList<>();
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

        if (selectedContactList.contains(contact)) {
            holder.mainView.setSelected(true);
        } else {
            holder.mainView.setSelected(false);
        }

        holder.mainView.setTag(contact);
        holder.mainView.setOnClickListener(v -> {
            if (selectedContactList.contains(contact)) {
                selectedContactList.remove(contact);
            } else {
                selectedContactList.add(contact);
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        ViewGroup mainView;

        MainViewHolder(View v) {
            super(v);
            nameTextView = (TextView) v.findViewById(R.id.contact_item_name);
            emailTextView = (TextView) v.findViewById(R.id.contact_item_email);
            mainView = (ViewGroup) v.findViewById(R.id.main_contact_list_item_view);
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

    public void updateList(List<Contact> contacts) {
        contactList.clear();
        contactList.addAll(contacts);
        notifyDataSetChanged();
    }

    public ArrayList<Contact> getSelectedContactList() {
        return selectedContactList;
    }
}