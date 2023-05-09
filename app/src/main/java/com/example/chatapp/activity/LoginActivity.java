package com.example.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.common.DialogManager;
import com.example.chatapp.common.SharedPreference;
import com.example.chatapp.model.User;
import com.example.chatapp.model.request.LoginRequest;
import com.example.chatapp.model.response.ResponseModel;
import com.example.chatapp.service.APIService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity  extends AppCompatActivity {
    ImageView imageViewBack;
    Button buttonLogin;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewRegister;
    ImageView imageViewEye;
    boolean isHintPassword = true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Mapping();
        SetListener();
        LoginWithExitedAccount();
        isHintPassword = true;
    }

    private void LoginWithExitedAccount() {
        User  user = SharedPreference.getInstance(getApplicationContext()).getUser();
        if(user!= null)
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void SetListener() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.super.onBackPressed();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword .getText().toString();
                if(TextUtils.isEmpty(email)){
                    editTextEmail.setError("Please enter your email");
                    editTextEmail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    editTextPassword.setError("Please enter your password");
                    editTextPassword.requestFocus();
                    return;
                }
                Login(email, password);
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        imageViewEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHintPassword){
                    editTextPassword.setTransformationMethod(null);
                    editTextPassword.setSelection(editTextPassword.getText().toString().length());
                    imageViewEye.setImageResource(R.drawable.ic_baseline_remove_red_eye_active);
                }
                else {
                    editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                    editTextPassword.setSelection(editTextPassword.getText().toString().length());
                    imageViewEye.setImageResource(R.drawable.ic_baseline_remove_red_eye_24_negative);
                }
                isHintPassword= !isHintPassword;
            }
        });
    }

    private void Login(String email, String password) {
        APIService apiService = APIService.getAPIService();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.email = email;
        loginRequest.password =  password;
        loginRequest.deviceToken = SharedPreference.getInstance(getApplicationContext()).getDeviceToken();
        if(loginRequest.deviceToken == null || loginRequest.deviceToken =="") {
            Toast.makeText(getApplicationContext(), "Cant get device token",Toast.LENGTH_SHORT).show();
            return;
        }
        DialogManager.GetInstance(LoginActivity.this).ShowLoading();
        apiService.login(loginRequest).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                DialogManager.GetInstance(LoginActivity.this).HideLoading();
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

                Type type = new TypeToken<User>(){}.getType();
                User user = new Gson().fromJson(new Gson().toJson(response.body().data),type);
                SharedPreference.getInstance(getApplicationContext()).setUser(user);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                DialogManager.GetInstance(LoginActivity.this).HideLoading();
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void Mapping() {
        imageViewBack = findViewById(R.id.imageViewBack);
        buttonLogin = findViewById((R.id.buttonLogin));
        editTextEmail= findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
        imageViewEye = findViewById(R.id.imageViewEye);
    }
}
