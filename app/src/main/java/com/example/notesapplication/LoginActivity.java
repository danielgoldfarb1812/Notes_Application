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
    // תיבות הטקסט וכפתורים בפעילות
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
        // הגדרת הפעולות של הכפתורים בפעילות
        loginBtn.setOnClickListener(v -> loginUser());
        createAccountBtnTextView.setOnClickListener(v -> {
            // עברת לפעילות יצירת חשבון חדש
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            finish();
        });
        languageBtn.setOnClickListener(v -> {
            // תפתח תפריט הנפתח עבור בחירת שפה
            showLanguageMenu();
        });
    }

    private void showLanguageMenu() {
        // תפריט הנפתח עבור בחירת שפה
        PopupMenu popupMenu = new PopupMenu(LoginActivity.this, languageBtn);

        // הוספת פריטי השפות לתפריט
        popupMenu.getMenu().add("HE");
        popupMenu.getMenu().add("EN");
        popupMenu.show();

        // הגדרת תגובה עם השפה הנבחרת
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String langChoice = item.getTitle().toString();
                switch (langChoice){
                    case "HE":
                        // שינוי לשפה עברית
                        changeToHebrew();
                        break;
                    case "EN":
                        // שינוי לשפה אנגלית
                        changeToEnglish();
                        break;
                }
                return true;
            }
        });
    }

    private void changeToEnglish() {
        // שינוי השפה לאנגלית והרענון של הפעילות
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateResource("en");
        recreate();
    }

    private void changeToHebrew() {
        // שינוי השפה לעברית והרענון של הפעילות
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateResource("iw");
        recreate();
    }

    void loginUser(){
        // קבלת הקלט מתיבת הטקסט לאימייל ולסיסמה
        String email = emailEditTxt.getText().toString();
        String password = passwordEditTxt.getText().toString();

        // אימות תקינות הקלט
        boolean isValidated = validateData(email, password);
        if (!isValidated){
            return;
        }

        // התחברות עם האימייל והסיסמה לפיירבייס
        loginWithFirebase(email, password);
    }

    private void loginWithFirebase(String email, String password) {
        // התחברות לפיירבייס באמצעות אימייל וסיסמה
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            // בסיום התהליך, שינוי התצוגה בהתאם לתוצאה
            changeInProgress(false);
            if (task.isSuccessful()){
                // התחברות הצליחה
                if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()){
                    // האימייל מאומת - עבור למסך הראשי
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    // האימייל אינו מאומת - הודעה למשתמש
                    Utility.showToast(LoginActivity.this, getResources().getString(R.string.email_verification_error));
                }
            }
            else{
                // התחברות נכשלה - הודעת שגיאה למשתמש
                Utility.showToast(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        });

    }

    void changeInProgress(boolean inProgress){
        // פונקציה לשינוי תצוגת הפעילות כאשר התהליך בתהליך בפעולת הכניסה מתבצע
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
        // אימות הקלט שהוזן על ידי המשתמש

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
        // חיבור המשתנים בקוד לפריטים ב-layout של הפעילות
        emailEditTxt = findViewById(R.id.email_edit_text);
        passwordEditTxt = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        languageBtn = findViewById(R.id.language_btn);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);
    }
}