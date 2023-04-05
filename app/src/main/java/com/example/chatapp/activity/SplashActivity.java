package com.example.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;

public class SplashActivity extends AppCompatActivity {
    private final  int delayChangeActivity= 2000;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ChangeActivity();
    }

    private  void ChangeActivity(){
        Thread changeActivity = new Thread(){
            public  void run(){
                try{
                    sleep(delayChangeActivity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        changeActivity.start();
    }
    protected void onPause(){
        super.onPause();
        finish();
    }
}
