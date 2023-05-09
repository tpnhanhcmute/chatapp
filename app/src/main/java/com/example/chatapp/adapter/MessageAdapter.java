package com.example.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.common.FileType;
import com.example.chatapp.common.SharedPreference;
import com.example.chatapp.model.Message;
import com.example.chatapp.R;
import com.example.chatapp.model.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    public final int MESSAGE_RECEIVER =1;
    public  final int MESSAGE_SENDER =2;
    private Context context;
    private List<Message> messageList;
    private  String otherName;
    private  String otherAvatarUrl;

    public MessageAdapter(Context context, List<Message> messageList,String otherName,String avatarUrl) {
        this.context = context;
        this.messageList = messageList;
        this.otherName = otherName;
        this.otherAvatarUrl = avatarUrl;
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
    public  int GetViewType(Message message){
        String userID = SharedPreference.getInstance(context).getUser().userID;
        return message.senderID.equals(userID)?MESSAGE_SENDER:MESSAGE_RECEIVER;

    }
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        int indexPrevMessage = position-1;
        if(holder.nameSender!= null)
            holder.nameSender.setVisibility(View.VISIBLE);
        if(holder.circleImageViewProfileImageInContentChat != null){
            Glide.with(context).load(otherAvatarUrl).into(holder.circleImageViewProfileImageInContentChat);
        }
        if(indexPrevMessage >=0){
            if(holder.nameSender != null){
                if(message.senderID.equals(messageList.get(indexPrevMessage).senderID)){
                    holder.nameSender.setVisibility(View.GONE);
                }
                else {
                    holder.nameSender.setVisibility(View.VISIBLE);
                    holder.nameSender.setText(otherName);
                }
            }
            if(holder.circleImageViewProfileImageInContentChat!= null){
                if(message.senderID.equals(messageList.get(indexPrevMessage).senderID)){
                    holder.circleImageViewProfileImageInContentChat.setImageDrawable(null);
                }
            }
        }
        if(holder.textViewMessageContent != null){
            holder.textViewMessageContent.setVisibility(View.VISIBLE);
            holder.textViewMessageContent.setText(message.content);
        }else {
            holder.textViewMessageContent.setVisibility(View.GONE);
        }
        if(holder.textViewTime != null){
            holder.textViewTime.setText(message.date);

        }

        holder.imageViewPic.setVisibility(View.GONE);
        holder.relativeLayoutFile.setVisibility(View.GONE);
        holder.videoView.setVisibility(View.GONE);
        holder.relativeLayoutFile.setVisibility(View.GONE);
       if(holder.imageViewSending != null){
           if(message.isSending){
               holder.imageViewSending.setVisibility(View.VISIBLE);
           }else {
               holder.imageViewSending.setVisibility(View.GONE);
           }
       }
       if(message.uri != null){
           if(message.fileType.equals(FileType.IMAGE.name())){
               holder.imageViewPic.setVisibility(View.VISIBLE);
               try {
                   Bitmap bitmap =
                           MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(message.uri));
                   holder.imageViewPic.setImageBitmap(bitmap);

               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
                   //holder.imageViewPic.setImageBitmap(bitmap);
           }
           return;
       }
       else {
           if( message.fileUrl != null){
               if(!message.fileUrl.equals(""))
               {
                   if(message.fileType.equals(FileType.IMAGE.name())){
                       holder.imageViewPic.setVisibility(View.VISIBLE);
                       Glide.with(context).load(message.fileUrl).into(holder.imageViewPic);
                   }
                   if(message.fileType.equals((FileType.VIDEO.name()))){

                       Uri videoUri = Uri.parse(message.fileUrl);
                       holder.videoView.setVideoURI(videoUri);
                       holder.videoView.setVisibility(View.VISIBLE);
                       holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                           @Override
                           public void onPrepared(MediaPlayer mp) {
                               holder.videoView.start();
                               holder.videoView.pause();
                           }
                       });
                       holder.videoView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               if (holder.videoView.isPlaying()) {
                                   holder.videoView.pause();
                               } else {
                                   holder.videoView.start();
                               }
                           }
                       });
                   }
                   if(message.fileType.equals(FileType.OTHER.name()))
                   {
                       holder.relativeLayoutFile.setVisibility(View.VISIBLE);
                       holder.textViewFileUrl.setText("Click to download file");
                       holder.imageViewDownload.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               String url = message.fileUrl;
                               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                               context.startActivity(intent);
                           }
                       });
                   }
                   return;
               }
           }
       }


    }

    @Override
    public int getItemCount() {
        return messageList==null?0: messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public   String messageID;
        public  int viewType;
        public TextView nameSender;

        public  TextView textViewMessageContent;
        public TextView textViewTime;
        public  ImageView imageViewPic;
        public LinearLayout relativeLayoutFile;
        public  ImageView imageViewDownload;
        public  TextView textViewFileUrl;
        public de.hdodenhof.circleimageview.CircleImageView circleImageViewProfileImageInContentChat;
        public VideoView videoView;
        //Sender
        public ImageView imageViewSending;

        public MyViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            textViewMessageContent = itemView.findViewById(R.id.textViewMessageContent);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            imageViewPic = itemView.findViewById(R.id.imageViewPic);
            relativeLayoutFile = itemView.findViewById(R.id.relativeLayoutFile);
            imageViewDownload = itemView.findViewById(R.id.imageViewDownload);
            textViewFileUrl = itemView.findViewById((R.id.textViewFileUrl));

            videoView = itemView.findViewById(R.id.videoView);
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
