package com.example.chatapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;

public class OTPVerifyActivity extends AppCompatActivity {

    ImageView imageViewBack;
    Button btnVerity;
    TextView textViewResendOTP;

    EditText editTextOTP1;
    EditText editTextOTP2;
    EditText editTextOTP3;
    EditText editTextOTP4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
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
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        textViewResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"You click resend OTP",Toast.LENGTH_LONG).show();
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
