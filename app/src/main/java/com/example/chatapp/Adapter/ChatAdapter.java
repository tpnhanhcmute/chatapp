package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Activity.ChatContentActivity;
import com.example.chatapp.Models.Chat;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatContentBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{
    private Context context;
    private List<Chat> chatList;
    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }
    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chat,null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        Chat chat = chatList.get(position);
    }

    @Override
    public int getItemCount() {
        return chatList ==null?0:chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageProfile;
        public TextView textViewDisplayName;
        public  TextView textViewLastMessage;
        public   TextView textViewTime;
        public String chatID;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.profile_image);
            textViewDisplayName = itemView.findViewById(R.id.textViewDisplayName);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(view.getContext(), ChatContentActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
