package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Models.Message;
import com.example.chatapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    public final int MESSAGE_RECEIVER =1;
    public  final int MESSAGE_SENDER =2;
    private Context context;
    private List<Message> messageList;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= null;
        switch (viewType){
            case MESSAGE_RECEIVER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_receiver_message_text,null);
                break;
            case MESSAGE_SENDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_sender_message_text,null);
                break;
        }
        MessageAdapter.MyViewHolder myViewHolder = new MessageAdapter.MyViewHolder(view,viewType);
        return myViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
     // check if is user and type content
        SharedPreferences sharedPreferences =  context.getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        Message message = messageList.get(position);
        return message.sender.equals(sharedPreferences.getString("localUser",""))?MESSAGE_SENDER:MESSAGE_RECEIVER;

    }
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        int indexPrevMessage = position-1;
        if(holder.nameSender!= null)
            holder.nameSender.setVisibility(View.VISIBLE);

        if(holder.circleImageViewProfileImageInContentChat!= null){
            holder.circleImageViewProfileImageInContentChat.setImageResource(R.drawable.ic_launcher_background);
        }
        if(indexPrevMessage >=0){
            if(holder.nameSender != null){
                if(message.sender.equals(messageList.get(indexPrevMessage).sender)){
                    holder.nameSender.setVisibility(View.GONE);
                }
            }
            if(holder.circleImageViewProfileImageInContentChat!= null){
                if(message.sender.equals(messageList.get(indexPrevMessage).sender)){
                    holder.circleImageViewProfileImageInContentChat.setImageDrawable(null);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return messageList==null?0: messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        private  int messageID;
        private  int viewType;
        private TextView nameSender;
        private de.hdodenhof.circleimageview.CircleImageView circleImageViewProfileImageInContentChat;

        public MyViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
             switch (viewType){
                 case MESSAGE_RECEIVER:
                     nameSender = (TextView)itemView.findViewById(R.id.senderName);
                     circleImageViewProfileImageInContentChat = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.profile_image_in_content_chat);
                     break;
                 case MESSAGE_SENDER:

                     break;
             }

        }
    }
}
