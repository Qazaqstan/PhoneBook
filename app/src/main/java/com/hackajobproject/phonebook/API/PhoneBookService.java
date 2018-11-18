package com.hackajobproject.phonebook.API;

import com.hackajobproject.phonebook.Model.PhoneBook;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PhoneBookService {
    @GET("581335f71000004204abaf83")
    Call<PhoneBook> getPhoneBook();
}
