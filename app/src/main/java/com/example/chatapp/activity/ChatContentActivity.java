package com.example.chatapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.ModelLoader;
import com.example.chatapp.adapter.MessageAdapter;
import com.example.chatapp.common.Const;
import com.example.chatapp.common.FileType;
import com.example.chatapp.common.SharedPreference;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.Message;
import com.example.chatapp.R;
import com.example.chatapp.model.User;
import com.example.chatapp.model.request.SendMessageRequest;
import com.example.chatapp.model.response.ResponseModel;
import com.example.chatapp.service.APIService;
import com.example.chatapp.service.FirebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatContentActivity extends AppCompatActivity {

    public static final int MY_REQUEST_CODE = 100;
    public static String[] storge_permissions = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private final String[] okFileImageExtensions = new String[] {
            "jpg",
            "png",
            "gif",
            "jpeg"
    };

    public static String[] storge_permissions_33 =
            {
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_AUDIO,
                    android.Manifest.permission.READ_MEDIA_VIDEO
            };

    private ImageView imageViewBack;
    private MessageAdapter messageAdapter;
    private RecyclerView rcMessage;
    private EditText editTextMessage;
    private  ImageView imageViewSendMessage;
    private TextView textViewDisplayNameInChat;
    private  ImageView imageViewAttachFile;

    private RelativeLayout relativeLayoutSelect;
    private  ImageView imageViewHolderSelect;
    private  ImageView imageViewCancelSelectImage;
    private  TextView textViewFileName;
    private CircleImageView profile_image;
    List<Message> messageList;

    private int previousHeight = 0;
    Chat chat;
    ChildEventListener childEventListener;
    Uri mUri;
    FileType fileType;
    Message lastMessage;
    @RequiresApi(api = 33)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_content);
        Mapping();
        LoadData();
        LoadListMessage();
        SetListener();
        SetUpFirebaseResponse();
        User user = SharedPreference.getInstance(getApplicationContext()).getUser();
        FirebaseService.getInstance().RegisterOnMessage(user.userID, chat.messageID,childEventListener);
    }
    public void SetUpFirebaseResponse(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.messageID = snapshot.getKey();
                messageList.add(message);
                if(lastMessage != null)
                {
                    messageList.remove(lastMessage);
                }
                rcMessage.scrollToPosition(messageList.size()-1);
                messageAdapter.notifyDataSetChanged();
            }


            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.messageID = snapshot.getKey();
                Optional<Message>  oldMessage =  messageList.stream().filter(x->x.messageID.equals(message.messageID)).findFirst();
                if(oldMessage.isPresent()){
                    oldMessage.get().isRecall = message.isRecall;
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        User user = SharedPreference.getInstance(getApplicationContext()).getUser();
        FirebaseService.getInstance().UnRegisterOnMessage(user.userID, chat.messageID,childEventListener);
    }

    @RequiresApi(api = 33)
    public  void LoadData(){
        chat = (Chat) getIntent().getSerializableExtra(Const.CHAT);
        textViewDisplayNameInChat.setText(chat.name);
        Glide.with(getApplicationContext()).load(chat.avatarUrl).into(profile_image);
    }
    private  void LoadListMessage(){
        messageList = new ArrayList<Message>();
        messageAdapter = new MessageAdapter(this, messageList,chat.name, chat.avatarUrl,chat.userID, this);
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
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        textViewDisplayNameInChat = findViewById(R.id.textViewDisplayNameInChat);
        imageViewAttachFile = findViewById(R.id.imageViewAttachFile);
        relativeLayoutSelect =findViewById(R.id.relativeLayoutSelect);
        imageViewHolderSelect =findViewById(R.id.imageViewHolderSelect);
        imageViewCancelSelectImage =findViewById(R.id.imageViewCancelSelectImage);
        textViewFileName = findViewById(R.id.textViewFileName);
        profile_image = findViewById(R.id.profile_image);
    }
    private  void SetListener(){
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatContentActivity.super.onBackPressed();
            }
        });
        ViewTreeObserver vto = getWindow().getDecorView().getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int currentHeight = rcMessage.getHeight();
                if (previousHeight != 0 && currentHeight < previousHeight) {
                    // Keyboard is showing
                    rcMessage.smoothScrollToPosition(messageList.size()-1);
                }
                previousHeight = currentHeight;
            }
        });
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editTextMessage.getText().toString();
                if(TextUtils.isEmpty(content) && mUri == null){return;}

                SendMessageRequest sendMessageRequest = new SendMessageRequest();
                sendMessageRequest.senderID = SharedPreference.getInstance(getApplicationContext()).getUser().userID;
                sendMessageRequest.receiverID = chat.userID;
                Message message = new Message();
                message.content = content;
                sendMessageRequest.message = message;
                message.senderID = sendMessageRequest.senderID;
                if(fileType != null){
                    message.fileType =fileType.name();
                }
                if(mUri != null){

                    OnSuccessListener<String> onSuccessListener = new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String url) {
                            message.fileUrl = url;
                            SendMessage(sendMessageRequest);
                        }
                    };
                    OnFailureListener onFailureListener = new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    };
                    FirebaseService.getInstance().UploadFile(mUri,fileType,sendMessageRequest.senderID, onSuccessListener, onFailureListener);
                }else {

                    SendMessage(sendMessageRequest);
                }
                editTextMessage.setText("");
                relativeLayoutSelect.setVisibility(View.GONE);

                try {
                    lastMessage = message.clone();
                    if(mUri != null){
                        lastMessage.uri = mUri.toString();
                    }
                    lastMessage.fileUrl = null;
                    lastMessage.isSending = true;
                    lastMessage.senderID = sendMessageRequest.senderID;
                    messageList.add(lastMessage);
                    rcMessage.scrollToPosition(messageList.size()-1);
                    messageAdapter.notifyDataSetChanged();

                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                mUri = null;

            }
        });
        imageViewAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });
        imageViewCancelSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUri = null;
                relativeLayoutSelect.setVisibility(View.GONE);
            }
        });
    }

    private void SendMessage(SendMessageRequest sendMessageRequest) {
        sendMessageRequest.message.isSending = false;
        sendMessageRequest.message.uri = null;
        APIService apiService = APIService.getAPIService();
        apiService.sendMessage(sendMessageRequest).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(!response.isSuccessful()) {
                    if(response.body()!= null){
                        Toast.makeText(getApplicationContext(),response.body().message, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if(response.body().isError) {
                    Toast.makeText(getApplicationContext(),response.body().message, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

            }
        });
    }

    private void UploadImage() {
        CheckPermission();
    }

    private void CheckPermission() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            openGallery();
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else {
            requestPermissions(permissions(),MY_REQUEST_CODE);
        }
    }
    private  void openGallery(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }
    private ActivityResultLauncher<Intent>
            mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e("TAG", "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        mUri = data.getData();
                        String mimeType = getContentResolver().getType(mUri);
                        imageViewHolderSelect.setVisibility(View.GONE);
                        textViewFileName.setVisibility(View.GONE);
                        if(mimeType.startsWith("image/")){
                            fileType = FileType.IMAGE;
                            try {
                                relativeLayoutSelect.setVisibility(View.VISIBLE);
                                imageViewHolderSelect.setVisibility(View.VISIBLE);
                                Bitmap bitmap =
                                        MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                                imageViewHolderSelect.setImageBitmap(bitmap);

                            } catch (FileNotFoundException e) {

                                e.printStackTrace();
                            } catch (IOException e) {
                                relativeLayoutSelect.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        }
                        else {
                            relativeLayoutSelect.setVisibility(View.VISIBLE);
                            textViewFileName.setVisibility(View.VISIBLE);
                            File file = new File(mUri.getPath());
                            String fileName = file.getName();
                            textViewFileName.setText(fileName);
//                            if(mimeType.startsWith("audio/")){
//
//                                fileType = FileType.AUDIO;
//                                return;
//                            }
                            if(mimeType.startsWith("video")){

                                fileType = FileType.VIDEO;
                                return;
                            }
                            fileType =FileType.OTHER;
                        }
                        Log.d("IMAGE",mUri.getPath());
                    }
                }
            }
    );
    public static String[] permissions() {
        String[] p;
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.TIRAMISU){
            p = storge_permissions_33;
        }else {
            p= storge_permissions;
        }
        return p;
    }

}
