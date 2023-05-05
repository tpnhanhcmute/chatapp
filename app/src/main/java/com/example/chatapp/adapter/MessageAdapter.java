package com.example.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.common.SharedPreference;
import com.example.chatapp.model.Message;
import com.example.chatapp.R;
import com.example.chatapp.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    public final int MESSAGE_RECEIVER =1;
    public  final int MESSAGE_SENDER =2;
    private Context context;
    private List<Message> messageList;
    private  String otherName;

    public MessageAdapter(Context context, List<Message> messageList,String otherName) {
        this.context = context;
        this.messageList = messageList;
        this.otherName = otherName;
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
        String userID = SharedPreference.getInstance(context).getUser().userID;
        Message message = messageList.get(position);
        return message.senderID.equals(userID)?MESSAGE_SENDER:MESSAGE_RECEIVER;

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
                if(message.senderID.equals(messageList.get(indexPrevMessage).senderID)){
                    holder.nameSender.setVisibility(View.GONE);
                }
            }
            if(holder.circleImageViewProfileImageInContentChat!= null){
                if(message.senderID.equals(messageList.get(indexPrevMessage).senderID)){
                    holder.circleImageViewProfileImageInContentChat.setImageDrawable(null);
                }
            }
        }

        if(holder.textViewMessageContent != null){
            holder.textViewMessageContent.setText(message.content);
        }
        if(holder.textViewTime != null){
            holder.textViewTime.setText(message.date);

        }
       if(holder.imageViewSending != null){
           if(message.isSending){
               holder.imageViewSending.setVisibility(View.VISIBLE);
           }else {
               holder.imageViewSending.setVisibility(View.GONE);
           }
       }
       if( message.fileUrl != null){
            if(!message.fileUrl.equals(""))
            {
                if(message.isImage){
                    holder.imageViewPic.setVisibility(View.VISIBLE);
                    Glide.with(context).load(message.fileUrl).into(holder.imageViewPic);
                }else {
                    holder.relativeLayoutFile.setVisibility(View.VISIBLE);
                    holder.textViewFileUrl.setText(message.fileUrl);
                }
                return;
            }
       }

        holder.imageViewPic.setVisibility(View.GONE);
        holder.relativeLayoutFile.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return messageList==null?0: messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        private  String messageID;
        private  int viewType;
        private TextView nameSender;
        private  TextView textViewMessageContent;
        private TextView textViewTime;
        private  ImageView imageViewPic;
        private LinearLayout relativeLayoutFile;
        private  ImageView imageViewDownload;
        private  TextView textViewFileUrl;
        private de.hdodenhof.circleimageview.CircleImageView circleImageViewProfileImageInContentChat;

        //Sender
        private ImageView imageViewSending;

        public MyViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            textViewMessageContent = itemView.findViewById(R.id.textViewMessageContent);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            imageViewPic = itemView.findViewById(R.id.imageViewPic);
            relativeLayoutFile = itemView.findViewById(R.id.relativeLayoutFile);
            imageViewDownload = itemView.findViewById(R.id.imageViewDownload);
            textViewFileUrl = itemView.findViewById((R.id.textViewFileUrl));
             switch (viewType){
                 case MESSAGE_RECEIVER:
                     nameSender = (TextView)itemView.findViewById(R.id.senderName);
                     circleImageViewProfileImageInContentChat = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.profile_image_in_content_chat);
                     break;
                 case MESSAGE_SENDER:
                     imageViewSending = itemView.findViewById(R.id.imageViewSending);
                     break;
             }
        }
    }
}
