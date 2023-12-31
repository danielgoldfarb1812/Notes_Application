package com.example.notesapplication;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

//מחלקת עזר לשימוש בפונקציות שחוזרות על עצמן
public class Utility {

    //הצגת הודעה למשתמש
    static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //החזרת אוסף הפתקים לפי משתמש
    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes").
                document(currentUser.getUid()).collection("my_notes");
    }

    //המרת זמן למחרוזת
    static String timestampToString(Timestamp ts){
        return new SimpleDateFormat("dd/MM/yyyy").format(ts.toDate());
    }
}
