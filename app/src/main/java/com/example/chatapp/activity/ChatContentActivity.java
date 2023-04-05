package com.example.chatapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.adapter.MessageAdapter;
import com.example.chatapp.model.Message;
import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatContentActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private MessageAdapter messageAdapter;
    private RecyclerView rcMessage;
    private EditText editTextMessage;
    List<Message> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_content);
        Mapping();
        LoadData();
        SetListener();

    }
    private  void LoadData(){
        messageList = new ArrayList<Message>();
        Message message = new Message();
        Message message1 = new Message();
        message.sender ="nhan";
        messageList.add(message);
        message1.sender="heo";
        messageList.add(message1);
        message1.sender="heo";
        messageList.add(message1);
        message1.sender="heo";
        messageList.add(message1);
        message.sender ="nhan";
        messageList.add(message);
        message.sender ="nhan";
        messageList.add(message);
        message.sender ="nhan";
        messageList.add(message);
        message1.sender="heo";
        messageList.add(message1);
        message1.sender="heo";
        messageList.add(message1);
        message1.sender="heo";
        messageList.add(message1);
        message.sender ="nhan";
        messageList.add(message);
        message.sender ="nhan";
        messageList.add(message);
        message.sender ="nhan";
        messageList.add(message);
        message1.sender="heo";
        messageList.add(message1);
        message1.sender="heo";
        messageList.add(message1);
        message1.sender="heo";
        messageList.add(message1);
        message.sender ="nhan";
        messageList.add(message);
        message.sender ="nhan";
        messageList.add(message);
        message.sender ="nhan";
        messageList.add(message);
        message1.sender="heo";
        messageList.add(message1);
        message1.sender="heo";
        messageList.add(message1);
        message1.sender="heo";
        messageList.add(message1);
        message.sender ="nhan";
        messageList.add(message);
        message.sender ="nhan";
        messageList.add(message);


        messageAdapter = new MessageAdapter(this, messageList);
        rcMessage.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        false);
        rcMessage.setLayoutManager(layoutManager);
        rcMessage.setAdapter(messageAdapter);
        rcMessage.scrollToPosition(messageList.size()-1);
        messageAdapter.notifyDataSetChanged();

    }
    private  void Mapping(){
        imageViewBack= (ImageView)findViewById(R.id.imageViewBack);
        rcMessage = (RecyclerView) findViewById(R.id.recycleViewMessage);
        editTextMessage = findViewById(R.id.editTextMessage);
    }
    private  void SetListener(){
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatContentActivity.super.onBackPressed();
            }
        });
    }
}
