package com.example.chatapp.fragment;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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
import android.widget.Toast;

import com.example.chatapp.adapter.ContactAdapter;
import com.example.chatapp.model.Contact;
import com.example.chatapp.R;
import com.example.chatapp.model.User;
import com.example.chatapp.model.request.MapPhoneNumberRequest;
import com.example.chatapp.model.request.MapPhoneNumberResponse;
import com.example.chatapp.model.response.ResponseModel;
import com.example.chatapp.service.APIService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String TAG = "TAG";
    private  List<Contact> contactList;

    private ContactAdapter contactAdapter;
    private RecyclerView rcContact;
    private  List<String> phoneNumberList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        Mapping(view);
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            return view;
        }
        phoneNumberList = new ArrayList<>();
        contactList = new ArrayList<Contact>();
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                 @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                 @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                 @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d(TAG, "Contact ID: " + contactId + ", Name: " + contactName +", Phone number"+phoneNumber);
                Contact contact = new Contact();
                contact.name = contactName;
                contact.phoneNumber = phoneNumber;
                contactList.add(contact);
                phoneNumberList.add(phoneNumber);
            }
        }

        contactAdapter = new ContactAdapter(getContext(), contactList);
        rcContact.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        false);
        rcContact.setLayoutManager(layoutManager);
        rcContact.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();


        CallMapPhoneNumber();

        return view;
    }

    private void CallMapPhoneNumber() {
        APIService apiService = APIService.getAPIService();
        MapPhoneNumberRequest request = new MapPhoneNumberRequest();
        request.phoneNumberList = phoneNumberList;

        apiService.mapPhoneNumber(request).enqueue(new Callback<ResponseModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(!response.isSuccessful()) {
                    if(response.body()!= null){
                        Toast.makeText(getContext(),response.body().message, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if(response.body().isError) {
                    Toast.makeText(getContext(),response.body().message, Toast.LENGTH_SHORT).show();
                    return;
                }

                Type type = new TypeToken<MapPhoneNumberResponse>(){}.getType();
                MapPhoneNumberResponse  mapPhoneNumberResponse = new Gson().fromJson(new Gson().toJson(response.body().data), type);

                contactList.forEach( x->{
                    Contact contact =  mapPhoneNumberResponse.mapPhoneNumber.get(x.phoneNumber);
                    if(contact!= null){
                        x.isTake= true;
                        x.email= contact.email;
                        x.avatarUrl = contact.avatarUrl;
                        x.userID = contact.userID;
                        x.phoneNumber = contact.phoneNumber;
                    }
                });
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

    private void  Mapping(View view){
        rcContact = view.findViewById(R.id.rcContact);
    }
}