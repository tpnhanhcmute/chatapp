package com.example.chatapp.model;

import android.net.Uri;

import java.util.Date;

public class Message implements  Cloneable {
    public String senderID;
    public  String content;
    public  String fileType;
    public  String fileUrl;
    public String date;
    public  boolean isRecall;

    public  boolean isSending =false;
    public String uri;

    public Message clone() throws CloneNotSupportedException {
        return (Message) super.clone();
    }
}
