package com.hackajobproject.phonebook.ComparatorIMPL;

import com.hackajobproject.phonebook.Model.Contact;

import java.util.Comparator;

public class ContactComparatorByName implements Comparator<Contact> {
    @Override
    public int compare(Contact a, Contact b) {
        return a.getName().compareTo(b.getName());
    }
}
