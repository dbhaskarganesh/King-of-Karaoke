package com.ganesh_project.kingofkaraoke.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ganesh_project.kingofkaraoke.R;
import com.ganesh_project.kingofkaraoke.login.LoginActivity;
import com.ganesh_project.kingofkaraoke.main.MainActivity;
import com.ganesh_project.kingofkaraoke.utils.SharedPreference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initUI();
    }

    private void initUI() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                SharedPreference sharedPreference= new SharedPreference(SplashActivity.this);

                if(sharedPreference.getAccessToken() != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 5000);
    }
}