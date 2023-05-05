package com.example.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.activity.ChatContentActivity;
import com.example.chatapp.common.Const;
import com.example.chatapp.model.Chat;
import com.example.chatapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.DataFormatException;

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
        holder.chat = chat;
        holder.textViewTime.setText("");
        if(chat.avatarUrl!= null){
            Glide.with(context).load(chat.avatarUrl).into(holder.imageProfile);
        }
        String lastMessage = chat.lastMessage;
        if(chat.lastMessage != null)
            if(chat.lastMessage.length() > 10)
                lastMessage = lastMessage.substring(0,10) +"...";
        holder.textViewLastMessage.setText(lastMessage);
        holder.textViewDisplayName.setText(chat.name);

       if(chat.date != null){
           SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy, hh:mm:ss a");
           SimpleDateFormat dateFormatHH = new SimpleDateFormat("h:mm:a M/d/yy");
           try
           {
               Date date = dateFormat.parse(chat.date);
               String formattedDate = dateFormatHH.format(date);
               holder.textViewTime.setText(formattedDate);
           } catch (ParseException e) {
               e.printStackTrace();
           }
       }
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
        public Chat chat;
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
                    intent.putExtra(Const.CHAT,chat);
                    context.startActivity(intent);
                }
            });
        }
    }
}
