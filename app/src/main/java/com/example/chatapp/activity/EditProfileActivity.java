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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.common.DialogManager;
import com.example.chatapp.common.FileType;
import com.example.chatapp.common.SharedPreference;
import com.example.chatapp.model.User;
import com.example.chatapp.model.request.UpdateInfoRequest;
import com.example.chatapp.model.response.ResponseModel;
import com.example.chatapp.service.APIService;
import com.example.chatapp.service.FirebaseService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.checkerframework.checker.units.qual.A;

import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity  extends AppCompatActivity {
    ImageView imageViewBack;
    LinearLayout linearLayoutChangeProfile;

    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPhone;
    Button btnUpdate;
    de.hdodenhof.circleimageview.CircleImageView profile_image;

    Uri mUri;
    String avatarUrl;
    public static final int MY_REQUEST_CODE = 100;
    public static String[] storge_permissions = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static String[] storge_permissions_33 =
            {
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_AUDIO,
                    android.Manifest.permission.READ_MEDIA_VIDEO
            };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Mapping();
        SetListener();
        SetData();

    }

    private void SetData() {
        User user = SharedPreference.getInstance(getApplicationContext()).getUser();
        if(user.name!= null) editTextName.setText(user.name);
        if(user.phoneNumber != null) editTextPhone.setText(user.phoneNumber);
        if(user.avatarUrl != null){
            Glide.with(getApplicationContext()).load(user.avatarUrl).into(profile_image);
        }
    }

    private void SetListener() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileActivity.super.onBackPressed();
            }
        });

        linearLayoutChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUri != null){
                    String userID = SharedPreference.getInstance(getApplicationContext()).getUser().userID;
                    OnSuccessListener<String> onSuccessListener = new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String url) {
                            DialogManager.GetInstance(EditProfileActivity.this).HideLoading();
                            avatarUrl= url;
                            CallAPIUpdateProfile();
                            mUri= null;
                        }
                    };
                    OnFailureListener onFailureListener = new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            DialogManager.GetInstance(EditProfileActivity.this).HideLoading();
                        }
                    };
                    DialogManager.GetInstance(EditProfileActivity.this).ShowLoading();
                    FirebaseService.getInstance().UploadFile(mUri, FileType.IMAGE,userID,
                            onSuccessListener,
                            onFailureListener);
                }else {
                    CallAPIUpdateProfile();
                }
            }
        });
    }

    private void CallAPIUpdateProfile() {
        String name = editTextName.getText().toString();
        if(TextUtils.isEmpty(name)){
            editTextName.setError("Please enter your name");
            editTextName.requestFocus();
            return;
        }
        String phoneNumber =editTextPhone.getText().toString();

        UpdateInfoRequest updateInfoRequest = new UpdateInfoRequest();
        if(avatarUrl!= null)
            updateInfoRequest.avatarUrl = avatarUrl;
        updateInfoRequest.name = name;
        updateInfoRequest.phoneNumber =phoneNumber;
        updateInfoRequest.userID = SharedPreference.getInstance(getApplicationContext()).getUser().userID;

        UpdateProfile(updateInfoRequest);
    }

    private void UpdateProfile(UpdateInfoRequest updateInfoRequest) {
        APIService apiService = APIService.getAPIService();
        DialogManager.GetInstance(this).ShowLoading();
        apiService.updateInfo(updateInfoRequest).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                DialogManager.GetInstance(EditProfileActivity.this).HideLoading();
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

                User user = SharedPreference.getInstance(getApplicationContext()).getUser();
                user.name = updateInfoRequest.name;
                user.phoneNumber = updateInfoRequest.phoneNumber;
                if(avatarUrl != null) user.avatarUrl = updateInfoRequest.avatarUrl;

                SharedPreference.getInstance(getApplicationContext()).setUser(user);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                DialogManager.GetInstance(EditProfileActivity.this).HideLoading();
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
        intent.setType("image/*");
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
                        Log.d("IMAGE",mUri.getPath());
                        try {
                            Bitmap bitmap =
                                    MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                            profile_image.setImageBitmap(bitmap);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    private  void EditProfile(){
        Toast.makeText(this,"You press edit profile", Toast.LENGTH_SHORT).show();
    }

    private void Mapping() {
        imageViewBack = findViewById(R.id.imageViewBack);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        btnUpdate = findViewById(R.id.buttonUpdate);
        linearLayoutChangeProfile = findViewById(R.id.linearLayoutChangeProfile);
        profile_image = findViewById(R.id.profile_image);
    }

    protected void onPause(){
        super.onPause();
    }
}
