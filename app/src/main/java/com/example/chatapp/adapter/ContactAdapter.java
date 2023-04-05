package com.example.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.model.Contact;
import com.example.chatapp.R;

import java.util.List;

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.MyViewHolder>{
    private Context context;
    private List<Contact> contactList;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_contact,null);
       MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contact contact = contactList.get(position);
    }

    @Override
    public int getItemCount() {
        return contactList!= null?0: contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
