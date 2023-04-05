package com.example.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapp.R;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    ImageView imageViewBack;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPhone;
    TextView textViewLogin;
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
        editTextPhone = findViewById(R.id.editTextPhone);
        textViewLogin = findViewById(R.id.textViewLogin);
    }
    private  void SetListener(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
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
    }

}
