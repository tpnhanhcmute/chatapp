package com.example.chatapp.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chatapp.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SharedPreference {
    private static SharedPreference _instance;
    private static Context _context;
    public  static  final  String SHARED_PREF_NAME = "ChatApp";
    public  static final String DEVICE_TOKEN = "DeviceToken";
    public  static final String USER = "User";

    public static SharedPreference getInstance(Context context){
        if(_instance == null){
            _context = context;
            _instance = new SharedPreference();
        }
        return _instance;
    }

    public void setDeviceToken(String deviceToken){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString(DEVICE_TOKEN, deviceToken);
        editor.apply();
    }
    public  String getDeviceToken(){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String deviceToken = sharedPreferences.getString(DEVICE_TOKEN, "");
        return deviceToken;
    }

    public  User getUser(){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userJsonString = sharedPreferences.getString(USER, "");
        if(userJsonString =="") return null;
        Type type = new TypeToken<User>(){}.getType(); //
        return new Gson().fromJson(userJsonString, type);
    }

    public void setUser(User user){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userString = new Gson().toJson(user);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putString(USER, userString);
        editor.apply();
    }
}
