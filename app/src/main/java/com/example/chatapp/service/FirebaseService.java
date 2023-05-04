package com.example.chatapp.service;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseService extends Service {
    private  FirebaseService _instance;
    private FirebaseDatabase _firebaseDatabase;
    private  FirebaseMessaging _firebaseMessaging;

    public  FirebaseService getInstance(){
        if(_instance == null)
        {
            _instance = new FirebaseService();
        }
        return _instance;
    }
    public  FirebaseDatabase getFirebaseDatabase(){
        return _firebaseDatabase;
    }
    public  FirebaseMessaging getFirebaseMessaging(){return _firebaseMessaging;}

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        _firebaseDatabase = FirebaseDatabase.getInstance();
        _firebaseMessaging = FirebaseMessaging.getInstance();
        _firebaseMessaging.subscribeToTopic("new_message");
        _firebaseMessaging.getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                Log.d("TOKEN", task.getResult());
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
