package com.example.chatapp.Fragments;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;

import com.example.chatapp.Adapters.ContactAdapter;
import com.example.chatapp.Models.Contact;
import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String TAG = "TAG";

    private ContactAdapter contactAdapter;
    private RecyclerView rcContact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            return view;
        }
        List<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                 @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                 @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                 @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d(TAG, "Contact ID: " + contactId + ", Name: " + contactName +", Phone number"+phoneNumber);
                Contact contact = new Contact();
                contact.displayName = contactName;
                contact.phoneNumber = phoneNumber;
                contactList.add(contact);
            }
        }
        Mapping(view);

        contactAdapter = new ContactAdapter(getContext(), contactList);
        rcContact.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        false);
        rcContact.setLayoutManager(layoutManager);
        rcContact.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();

        return view;
    }
    private void  Mapping(View view){
        rcContact = view.findViewById(R.id.rcContact);
    }
}