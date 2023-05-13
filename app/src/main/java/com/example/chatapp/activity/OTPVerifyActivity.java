package com.example.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.example.chatapp.model.request.ResendOTPRequest;
import com.example.chatapp.model.request.UserRequest;
import com.example.chatapp.model.response.RegisterResponse;
import com.example.chatapp.model.response.ResendOTPResponse;
import com.example.chatapp.model.response.ResponseModel;
import com.example.chatapp.service.APIService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import kotlin.collections.UArraySortingKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPVerifyActivity extends AppCompatActivity {

    ImageView imageViewBack;
    Button btnVerity;
    TextView textViewResendOTP;

    EditText editTextOTP1;
    EditText editTextOTP2;
    EditText editTextOTP3;
    EditText editTextOTP4;

    private  String otp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        otp = getIntent().getStringExtra(Const.OTP);
        Mapping();
        SetListener();
    }

    private void SetListener() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTPVerifyActivity.super.onBackPressed();
            }
        });
        btnVerity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code1=editTextOTP1.getText().toString();
                String code2=editTextOTP2.getText().toString();
                String code3=editTextOTP3.getText().toString();
                String code4=editTextOTP4.getText().toString();

                if(TextUtils.isEmpty((code1)) || TextUtils.isEmpty((code2))|| TextUtils.isEmpty((code3)) ||TextUtils.isEmpty((code4))){
                    Toast.makeText(getApplicationContext(), "Please fill OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                String otpVerify = code1+code2+code3+code4;
                VerifyOTP(otpVerify);
            }
        });
        textViewResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendOTP();
            }
        });

        editTextOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    editTextOTP2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    editTextOTP3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    editTextOTP4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void ResendOTP() {
        APIService apiService  =APIService.getAPIService();
        ResendOTPRequest resendOTPRequest = new ResendOTPRequest();
        resendOTPRequest.userID = getIntent().getStringExtra(Const.USER_ID);
        resendOTPRequest.email = getIntent().getStringExtra(Const.EMAIL);
        apiService.resendOTP(resendOTPRequest).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
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
                Type type = new TypeToken<ResendOTPResponse>(){}.getType();
                ResendOTPResponse resendOTPResponse = new Gson().fromJson(new Gson().toJson(response.body().data),type);
                otp = resendOTPResponse.otp;
                Toast.makeText(getApplicationContext(),"We resend your OTP, Please check you email",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void VerifyOTP(String otp) {

        APIService apiService = APIService.getAPIService();
        UserRequest userRequest = new UserRequest();
        userRequest.userID = getIntent().getStringExtra(Const.USER_ID);
        DialogManager.GetInstance(this).ShowLoading();
        apiService.authenticate(userRequest).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                DialogManager.GetInstance(OTPVerifyActivity.this).HideLoading();
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

                Intent intent = new Intent(OTPVerifyActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                DialogManager.GetInstance(OTPVerifyActivity.this).HideLoading();
                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Mapping() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        btnVerity = findViewById(R.id.buttonVerify);
        textViewResendOTP = findViewById(R.id.textViewResendOTP);

        editTextOTP1 = findViewById(R.id.editTextOTP1);
        editTextOTP2 = findViewById(R.id.editTextOTP2);
        editTextOTP3 = findViewById(R.id.editTextOTP3);
        editTextOTP4 = findViewById(R.id.editTextOTP4);

    }
}
