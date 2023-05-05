package com.example.chatapp.service;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatapp.common.FileType;
import com.example.chatapp.common.SharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseService extends Service {
    private static FirebaseService _instance;

    private String _deviceToken;

    public static FirebaseService getInstance(){
        if(_instance == null)
        {
            _instance = new FirebaseService();
        }
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());

        FirebaseMessaging.getInstance().subscribeToTopic("new_message");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                Log.d("TOKEN", task.getResult());
                _deviceToken = task.getResult();
                SharedPreference.getInstance(getApplicationContext()).setDeviceToken(_deviceToken);
            }
        });
    }

    public  void RegisterOnMessage(String userID, String messageID, ChildEventListener onChildEventListener){
        StringBuilder stringBuilder = new StringBuilder()
                .append("message/")
                        .append(userID)
                                .append("/")
                                        .append(messageID);
        String path = stringBuilder.toString();
        FirebaseDatabase.getInstance().getReference(path).addChildEventListener(onChildEventListener);
    }
    public  void UnRegisterOnMessage(String userID, String messageID, ChildEventListener onChildEventListener){
        StringBuilder stringBuilder = new StringBuilder()
                .append("message/")
                .append(userID)
                .append("/")
                .append(messageID);
        String path = stringBuilder.toString();
        FirebaseDatabase.getInstance().getReference(path).removeEventListener(onChildEventListener);
    }


    public  void UploadFile(Uri uri, FileType type, String id, OnSuccessListener<String> onSuccessListener, OnFailureListener onFailureListener){
        StringBuilder stringBuilder =
                new StringBuilder().append(id)
                        .append("/")
                        .append(type.name())
                        .append("/")
                        .append(uri.getLastPathSegment());
        String path = stringBuilder.toString();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference(path);
        UploadTask uploadTask = storageRef.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        onSuccessListener.onSuccess(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailureListener.onFailure(e);
            }
        });



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
