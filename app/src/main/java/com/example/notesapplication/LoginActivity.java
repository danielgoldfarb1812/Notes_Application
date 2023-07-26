package com.example.notesapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompat {
    EditText emailEditTxt, passwordEditTxt;
    Button loginBtn;
    ProgressBar progressBar;
    ImageButton languageBtn;
    TextView createAccountBtnTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initButtons();
    }

    private void initButtons() {
        loginBtn.setOnClickListener(v -> loginUser());
        createAccountBtnTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            finish();
        });
        languageBtn.setOnClickListener(v -> {
            showLanguageMenu();
        });
    }

    private void showLanguageMenu() {
        PopupMenu popupMenu = new PopupMenu(LoginActivity.this, languageBtn);
        popupMenu.getMenu().add("HE");
        popupMenu.getMenu().add("EN");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String langChoice = item.getTitle().toString();
                switch (langChoice){
                    case "HE":
                        changeToHebrew();
                        break;
                    case "EN":
                        changeToEnglish();
                        break;
                }
                return true;
            }
        });
    }

    private void changeToEnglish() {
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateResource("en");
        recreate();
    }

    private void changeToHebrew() {
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateResource("iw");
        recreate();
    }

    void loginUser(){
        String email = emailEditTxt.getText().toString();
        String password = passwordEditTxt.getText().toString();

        boolean isValidated = validateData(email, password);
        if (!isValidated){
            return;
        }

        loginWithFirebase(email, password);
    }

    private void loginWithFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            changeInProgress(false);
            if (task.isSuccessful()){
                // login is success
                if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()){
                    // go to mainactivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    Utility.showToast(LoginActivity.this, getResources().getString(R.string.email_verification_error));
                }
            }
            else{
                // login failed
                Utility.showToast(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        });

    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password){
        // validate data inputs from the user

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditTxt.setError(getResources().getString(R.string.invalid_email));
            return false;
        }
        if (password.length() < 6){
            passwordEditTxt.setError(getResources().getString(R.string.password_error_length));
            return false;
        }
        return true;
    }

    private void initViews() {
        emailEditTxt = findViewById(R.id.email_edit_text);
        passwordEditTxt = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        languageBtn = findViewById(R.id.language_btn);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);
    }
}