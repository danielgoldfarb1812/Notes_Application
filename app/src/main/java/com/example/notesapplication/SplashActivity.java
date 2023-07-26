package com.example.notesapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// המסך הראשון שנכנסים לאפליקציה. נטען 2 שניות ועובר למסך הראשי
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);

        //אובייקט המטפל במעבר המסך, מקבל פרמטר של זמן דיליי המוגדר למעלה
        int DELAY_TIME = 2000;
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null){
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            else{
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        }, DELAY_TIME);
    }
}