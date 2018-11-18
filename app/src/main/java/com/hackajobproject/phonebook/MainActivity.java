package com.hackajobproject.phonebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;

import com.hackajobproject.phonebook.API.PhoneBookService;
import com.hackajobproject.phonebook.ComparatorIMPL.ContactComparatorByAddress;
import com.hackajobproject.phonebook.ComparatorIMPL.ContactComparatorByName;
import com.hackajobproject.phonebook.ComparatorIMPL.ContactComparatorByPhone;
import com.hackajobproject.phonebook.Model.Contact;
import com.hackajobproject.phonebook.Model.PhoneBook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, Toolbar.OnMenuItemClickListener {
    private PhoneBook phoneBookData = new PhoneBook();

    private String searchQuery = "";
    private String orderBy = "name";
    private boolean orderAscending = true;

    Menu menu;
    RecyclerView recyclerView;
    SearchView searchView;
    PhoneBookAdapter phoneBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        MenuInflater inflater = getMenuInflater();
        menu = toolbar.getMenu();
        inflater.inflate(R.menu.menu, menu);
        toolbar.setOnMenuItemClickListener(this);

        /** Search */
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconified(true);
        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        /** RecyclerView */
        phoneBookAdapter = new PhoneBookAdapter(MainActivity.this, phoneBookData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.phoneBookList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(phoneBookAdapter);

        /** HTTP Request */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.mocky.io/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PhoneBookService service = retrofit.create(PhoneBookService.class);
        Call<PhoneBook> call = service.getPhoneBook();
        call.enqueue(new Callback<PhoneBook>() {
            @Override
            public void onResponse(Call<PhoneBook> call, Response<PhoneBook> response) {
                if (response.isSuccessful()) {
                    phoneBookData = response.body();
                    updatePhoneBook();
                }
            }

            @Override
            public void onFailure(Call<PhoneBook> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.sort_by_name || itemId == R.id.sort_by_address || itemId == R.id.sort_by_phone) {
            String updatedOrderBy = item.getTitle().toString().toLowerCase();

            if (!orderBy.equals(updatedOrderBy)) {
                orderAscending = true;
            } else {
                orderAscending = !orderAscending;
            }

            orderBy = updatedOrderBy;

            clearIconAttributeOfItems();
            setIconToItem(item);
            updatePhoneBook();
        }
        return true;
    }

    private void clearIconAttributeOfItems() {
        SubMenu sortingSubMenu = menu.findItem(R.id.sorting).getSubMenu();
        for (int i = 0; i < sortingSubMenu.size(); i++) {
            sortingSubMenu.getItem(i).setIcon(null);
        }
    }

    private void setIconToItem(MenuItem item) {
        item.setIcon(R.drawable.ic_down);
        if (!orderAscending)
            item.setIcon(R.drawable.ic_up);
    }

    @Override
    public boolean onClose() {
        searchQuery = "";
        searchView.setQuery("", false);
        updatePhoneBook();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        searchQuery = s;
        updatePhoneBook();
        return true;
    }

    private void updatePhoneBook() {
        List<Contact> filteredContacts = new ArrayList<>();
        for (Contact contact : phoneBookData.getContacts()) {
            String searchQuery = this.searchQuery.toLowerCase();
            if (contact.getName().toLowerCase().contains(searchQuery) || contact.getPhone_number().contains(searchQuery) || contact.getAddress().toLowerCase().contains(searchQuery)) {
                filteredContacts.add(contact);
            }
        }

        sortCollectionOrderBy(orderBy, filteredContacts);

        if (!orderAscending) {
            Collections.reverse(filteredContacts);
        }

        phoneBookAdapter.setPhoneBook(new PhoneBook(filteredContacts));
    }

    private void sortCollectionOrderBy(String orderBy, List<Contact> filteredContacts) {
        switch (orderBy) {
            case "name":
                Collections.sort(filteredContacts, new ContactComparatorByName());
                break;
            case "phone":
                Collections.sort(filteredContacts, new ContactComparatorByPhone());
                break;
            default:
                Collections.sort(filteredContacts, new ContactComparatorByAddress());
                break;
        }
    }

}