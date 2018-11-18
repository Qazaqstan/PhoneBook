package com.hackajobproject.phonebook.ComparatorIMPL;

import com.hackajobproject.phonebook.Model.Contact;

import java.util.Comparator;

public class ContactComparatorByPhone implements Comparator<Contact> {
    @Override
    public int compare(Contact a, Contact b) {
        return a.getPhone_number().compareTo(b.getPhone_number());
    }
}
