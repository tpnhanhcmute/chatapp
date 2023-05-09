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

import com.example.chatapp.R;
import com.example.chatapp.activity.ChatContentActivity;
import com.example.chatapp.common.Const;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.Search;
import com.example.chatapp.model.response.SearchResponse;

import java.util.List;

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    Context context;
    List<Search> listSearch;

    public SearchAdapter(Context context, List<Search> listSearch) {
        this.context = context;
        this.listSearch = listSearch;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_search_item,null);
        MyViewHolder myViewHolder = new SearchAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Search search = listSearch.get(position);
        holder.textViewDisplayName.setText(search.name);
        holder.textViewEmail.setText(search.email);
        holder.textViewPhoneNumber.setText(search.phoneNumber ==null?"":search.phoneNumber);
        holder.search= search;

    }

    @Override
    public int getItemCount() {
        return listSearch==null?0:listSearch.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_image;
        TextView textViewDisplayName;
        TextView textViewEmail;
        TextView textViewPhoneNumber;
        Search search;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            textViewDisplayName = itemView.findViewById(R.id.textViewDisplayName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatContentActivity.class);
                    Chat chat = new Chat();
                    chat.avatarUrl = search.avatarUrl;
                    chat.userID = search.userID;
                    chat.name = search.name;
                    chat.messageID = search.userID;
                    intent.putExtra(Const.CHAT,chat);

                    context.startActivity(intent);
                }
            });
        }
    }
}
