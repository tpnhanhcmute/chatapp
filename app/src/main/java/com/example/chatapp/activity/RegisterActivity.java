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
import com.example.chatapp.common.Const;
import com.example.chatapp.common.DialogManager;
import com.example.chatapp.model.request.RegisterRequest;
import com.example.chatapp.model.response.RegisterResponse;
import com.example.chatapp.model.response.ResponseModel;
import com.example.chatapp.service.APIService;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    ImageView imageViewBack;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewLogin;
    ImageView imageViewEye;
    boolean isHintPassword = true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Mapping();
        SetListener();
    }
    private  void Mapping(){
        btnRegister = findViewById(R.id.btnRegister);
        imageViewBack = findViewById(R.id.imageViewBack);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewLogin = findViewById(R.id.textViewLogin);
        imageViewEye = findViewById(R.id.imageViewEye);
    }

    private  void SetListener(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email  = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String name = editTextName.getText().toString();
                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Please enter your name");
                    editTextName.requestFocus();
                    return;
                }
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

                Register(name, email, password);
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                RegisterActivity.super.onBackPressed();
            }
        });
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
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

    private void Register(String name, String email, String password) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.name = name;
        registerRequest.email = email;
        registerRequest.password = password;
        APIService apiService = APIService.getAPIService();
        DialogManager.GetInstance(this).ShowLoading();
        apiService.register(registerRequest).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                DialogManager.GetInstance(RegisterActivity.this).HideLoading();
                if(!response.isSuccessful()) {
                    if(response.body()!= null){
                        Toast.makeText(getApplicationContext(),response.body().message, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if(response.body().isError){
                    Toast.makeText(getApplicationContext(),response.body().message, Toast.LENGTH_SHORT).show();
                    return;
                }
                Type type = new TypeToken<RegisterResponse>(){}.getType();
                RegisterResponse registerResponse = new Gson().fromJson(new Gson().toJson(response.body().data),type);
                String otp = registerResponse.otp;
                Intent intent = new Intent(RegisterActivity.this, OTPVerifyActivity.class);
                intent.putExtra(Const.OTP,otp);
                intent.putExtra(Const.USER_ID, registerResponse.user.userID);
                intent.putExtra(Const.EMAIL, registerResponse.user.email);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                DialogManager.GetInstance(RegisterActivity.this).HideLoading();
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
