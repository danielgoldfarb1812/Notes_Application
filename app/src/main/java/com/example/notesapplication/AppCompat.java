package com.example.notesapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AppCompat extends AppCompatActivity {

    //מחלקה אשר מאפשרת להשתמש בתכונות חדשות ומתקדמות יותר של Android
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateResource(languageManager.getLang());
    }
}
