package com.hackajobproject.phonebook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackajobproject.phonebook.Model.PhoneBook;


public class PhoneBookAdapter extends RecyclerView.Adapter<PhoneBookAdapter.PhoneBookViewHolder> {
    private PhoneBook phoneBookData;
    private Context context;

    public PhoneBookAdapter(Context context, PhoneBook phoneBookData) {
        this.context = context;
        this.phoneBookData = phoneBookData;
    }

    public void setPhoneBook(PhoneBook phoneBookData) {
        this.phoneBookData = phoneBookData;
        synchronized (this) {
            this.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public PhoneBookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_phonebook_item, viewGroup, false);
        return new PhoneBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneBookViewHolder phoneBookViewHolder, int i) {
        phoneBookViewHolder.name.setText(phoneBookData.getContacts().get(i).getName());
        phoneBookViewHolder.phoneNumber.setText(phoneBookData.getContacts().get(i).getPhone_number());
        phoneBookViewHolder.address.setText(phoneBookData.getContacts().get(i).getAddress());
    }

    @Override
    public int getItemCount() {
        return phoneBookData.getContacts().size();
    }

    class PhoneBookViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phoneNumber;
        TextView address;

        public PhoneBookViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            address = itemView.findViewById(R.id.address);
        }
    }
}
