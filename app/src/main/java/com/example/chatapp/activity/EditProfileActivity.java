package com.example.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.Touch;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;

public class EditProfileActivity  extends AppCompatActivity {
    ImageView imageViewBack;
    TextView textViewEditProfile;
    ImageView imageViewEditProfile;

    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPhone;

    Button btnUpdate;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Mapping();
        SetListener();

    }

    private void SetListener() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileActivity.super.onBackPressed();
            }
        });

        textViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile();
            }
        });
        imageViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"You press update profie", Toast.LENGTH_SHORT).show();
                EditProfileActivity.super.onBackPressed();
            }
        });
    }

    private  void EditProfile(){
        Toast.makeText(this,"You press edit profile", Toast.LENGTH_SHORT).show();
    }

    private void Mapping() {
        imageViewBack = findViewById(R.id.imageViewBack);
        textViewEditProfile = findViewById(R.id.textViewEditProfile);
        imageViewEditProfile = findViewById(R.id.imageViewEditProfile);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        btnUpdate = findViewById(R.id.buttonUpdate);
    }

    protected void onPause(){
        super.onPause();
        finish();
    }
}
