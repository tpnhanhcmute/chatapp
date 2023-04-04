package com.example.chatapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.Adapters.ChatAdapter;
import com.example.chatapp.Models.Chat;
import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {
    RecyclerView rcChat;
    ChatAdapter chatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        rcChat = view.findViewById(R.id.recycleViewChatList);
        List<Chat> chatList = new ArrayList<Chat>();
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());
        chatList.add(new Chat());


        chatAdapter = new ChatAdapter(getContext(), chatList);
        rcChat.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity().getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        false);
        rcChat.setLayoutManager(layoutManager);
        rcChat.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

        return  view;
    }
}
