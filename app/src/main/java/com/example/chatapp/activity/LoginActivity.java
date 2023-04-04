package com.example.chatapp.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;

public class LoginActivity  extends AppCompatActivity {
    ImageView imageViewBack;
    Button buttonLogin;
    EditText editTextPhone;
    TextView textViewRegister;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Mapping();
        SetListener();
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
                Intent intent = new Intent(v.getContext(), OTPVerifyActivity.class);
                startActivity(intent);
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Mapping() {
        imageViewBack = findViewById(R.id.imageViewBack);
        buttonLogin = findViewById((R.id.buttonLogin));
        editTextPhone= findViewById(R.id.editTextPhone);
        textViewRegister = findViewById(R.id.textViewRegister);
    }
}
