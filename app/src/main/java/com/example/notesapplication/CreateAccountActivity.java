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
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class CreateAccountActivity extends AppCompat {

    EditText emailEditTxt, passwordEditTxt, confirmPasswordEditTxt;
    Button createAccountBtn;
    ProgressBar progressBar;
    ImageButton languageBtn;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initViews();
        initButtons();
    }

    void createAccount() {
        String email = emailEditTxt.getText().toString();
        String password = passwordEditTxt.getText().toString();
        String confirmPassword = confirmPasswordEditTxt.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if (!isValidated){
            return;
        }

        createAccountInFirebase(email, password);

    }

    void createAccountInFirebase(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                task -> {
                    changeInProgress(false);
                    if (task.isSuccessful()){
                        // account created successfully
                        Utility.showToast(CreateAccountActivity.this, getResources().getString(R.string.account_create_success_verify));
                        Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification();
                        firebaseAuth.signOut();
                        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                        finish();
                    }
                    else{
                        // account creation failed
                        Utility.showToast(CreateAccountActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());
                    }
                });
    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password, String confirmPassword){
        // validate data inputs from the user

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditTxt.setError(getResources().getString(R.string.invalid_email));
            return false;
        }
        if (password.length() < 6){
            passwordEditTxt.setError(getResources().getString(R.string.password_error_length));
            return false;
        }
        if (!password.equals(confirmPassword)){
            confirmPasswordEditTxt.setError(getResources().getString(R.string.password_error_match));
            return false;
        }
        return true;
    }

    private void initViews() {
        emailEditTxt = findViewById(R.id.email_edit_text);
        passwordEditTxt = findViewById(R.id.password_edit_text);
        confirmPasswordEditTxt = findViewById(R.id.password_confirm_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        languageBtn = findViewById(R.id.language_btn);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);
    }

    private void initButtons() {
        createAccountBtn.setOnClickListener(v -> createAccount());
        loginBtnTextView.setOnClickListener(v -> {
            startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            finish();
        });
        languageBtn.setOnClickListener(v -> {
            showLanguageMenu();
        });
    }

    private void showLanguageMenu() {
        PopupMenu popupMenu = new PopupMenu(CreateAccountActivity.this, languageBtn);
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
}