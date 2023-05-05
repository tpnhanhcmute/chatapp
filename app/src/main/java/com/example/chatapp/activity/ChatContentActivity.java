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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                Log.d("BUG", snapshot.getKey());
                Message message = snapshot.getValue(Message.class);
                messageList.add(message);
                if(lastMessage != null)
                {
                    messageList.remove(lastMessage);
                }
                rcMessage.scrollToPosition(messageList.size()-1);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
    protected void onPause() {
        super.onPause();
        User user = SharedPreference.getInstance(getApplicationContext()).getUser();
        FirebaseService.getInstance().UnRegisterOnMessage(user.userID, chat.messageID,childEventListener);
    }

    @RequiresApi(api = 33)
    public  void LoadData(){
        chat = (Chat) getIntent().getSerializableExtra(Const.CHAT);
        textViewDisplayNameInChat.setText(chat.name);
    }
    private  void LoadListMessage(){
        messageList = new ArrayList<Message>();
        messageAdapter = new MessageAdapter(this, messageList,chat.name);
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
                if(TextUtils.isEmpty(content)){return;}
                SendMessage(content);
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

    private void SendMessage(String content) {
        Message message = new Message();
        message.content = content;
        message.senderID = SharedPreference.getInstance(getApplicationContext()).getUser().userID;
        message.isImage = false;
        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.message = message;
        sendMessageRequest.senderID = message.senderID;
        sendMessageRequest.receiverID =chat.userID;
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
        editTextMessage.setText("");
        lastMessage = message;
        message.isSending = true;
        messageList.add(message);
        rcMessage.scrollToPosition(messageList.size()-1);
        messageAdapter.notifyDataSetChanged();
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
                        if(mimeType.startsWith("image/")){
                            fileType =FileType.IMAGE;
                            try {
                                relativeLayoutSelect.setVisibility(View.VISIBLE);
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
                            fileType = FileType.OTHER;
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
