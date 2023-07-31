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

        // דרך זו נכין את האפליקציה להיות במצב יומי בלבד, גם אם התוכנית המכשיר מוגדרת למצב ערב
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);

        //אובייקט המטפל במעבר המסך, מקבל פרמטר של זמן דיליי המוגדר למעלה
        int DELAY_TIME = 2000;
        new Handler().postDelayed(() -> {
            // בדיקה אם יש משתמש מחובר כרגע באפליקציה
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            // בהתאם למצב המשתמש נעביר למסך המתאים
            if (currentUser == null){
                // אם אין משתמש מחובר - מעבר למסך ההתחברות (LoginActivity)
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            else{
                // אם קיים משתמש מחובר - מעבר למסך הראשי (MainActivity)
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            // סיום המעבר באמצעות finish(), נועד כדי למנוע חזרה למסך זה בלחיצה על כפתור החזרה
            finish();
        }, DELAY_TIME);
    }
}