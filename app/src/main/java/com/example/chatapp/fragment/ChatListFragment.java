package com.example.chatapp.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatapp.adapter.ChatAdapter;
import com.example.chatapp.common.SharedPreference;
import com.example.chatapp.model.Chat;
import com.example.chatapp.R;
import com.example.chatapp.model.request.UserRequest;
import com.example.chatapp.model.response.GetContactResponse;
import com.example.chatapp.model.response.RegisterResponse;
import com.example.chatapp.model.response.ResponseModel;
import com.example.chatapp.service.APIService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SocketHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListFragment extends Fragment {
    RecyclerView rcChat;
    ChatAdapter chatAdapter;
    List<Chat> chatList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        rcChat = view.findViewById(R.id.recycleViewChatList);

        chatList = new ArrayList<Chat>();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetContacts();
    }

    private void GetContacts() {
        APIService apiService = APIService.getAPIService();
        UserRequest userRequest = new UserRequest();
        userRequest.userID = SharedPreference.getInstance(getContext()).getUser().userID;

        apiService.getContacts(userRequest).enqueue(new Callback<ResponseModel>() {
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

                Type type = new TypeToken<GetContactResponse>(){}.getType();
                GetContactResponse getContactResponse = new Gson().fromJson(new Gson().toJson(response.body().data),type);
                chatList.clear();
                chatList.addAll(getContactResponse.listContactUser);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }
}
