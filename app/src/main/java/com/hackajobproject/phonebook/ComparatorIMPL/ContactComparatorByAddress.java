package com.hackajobproject.phonebook.ComparatorIMPL;

import com.hackajobproject.phonebook.Model.Contact;

import java.util.Comparator;

public class ContactComparatorByAddress implements Comparator<Contact> {
    @Override
    public int compare(Contact a, Contact b) {
        return a.getAddress().compareTo(b.getAddress());
    }
}
