package com.example.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.activity.ChatContentActivity;
import com.example.chatapp.common.Const;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.Contact;
import com.example.chatapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.MyViewHolder>{

    Context context;
    List<Contact> contactList;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }
    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_contact,null);
        ContactAdapter.MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder holder, int position) {

        Contact contact = contactList.get(position);
        holder.contact = contact;
        holder.textViewDisplayName.setText(contact.name);
        holder.textViewPhoneNumber.setText((contact.phoneNumber));
        if(contact.isTake){
            Glide.with(context).load(contact.avatarUrl).into(holder.profile_image);
        }else {
            holder.profile_image.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return contactList==null?0:contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Contact contact;
        TextView textViewDisplayName;
        TextView textViewPhoneNumber;
        CircleImageView profile_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDisplayName =itemView.findViewById(R.id.textViewDisplayName);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            profile_image = itemView.findViewById(R.id.profile_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(contact.isTake){
                        Chat chat = new Chat();
                        chat.messageID = contact.userID;
                        chat.avatarUrl = contact.avatarUrl;
                        chat.name = contact.name;
                        chat.userID = contact.userID;
                        Intent intent = new Intent(context, ChatContentActivity.class);
                        intent.putExtra(Const.CHAT,chat);
                        context.startActivity(intent);
                    }
                    else {
                        Toast.makeText(context, "User not register app, Send message to invite chat",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
