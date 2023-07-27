package com.example.notesapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView, deleteNoteTextViewBtn;
    String title, content, docId;
    boolean isEditMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        initViews();
        initButtons();

        // receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        // בדיקה האם נלחץ הכפתור של הוספת פתק או עריכה
        // אם בלחיצה על כפתור כלשהו התקבל id - סימן שאנחנו במצב עריכה
        // אחרת אנחנו במצב הוספה
        if (docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode){
            pageTitleTextView.setText(getResources().getString(R.string.edit_note));
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

    }

    private void initButtons() {
        saveNoteBtn.setOnClickListener((v) -> saveNote());
        deleteNoteTextViewBtn.setOnClickListener((v) -> deleteNoteFromFirebase());
    }

    private void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // note is deleted
                    Utility.showToast(NoteDetailsActivity.this, getResources().getString(R.string.note_delete_success));
                    finish();
                }
                else{
                    // note is not added
                    Utility.showToast(NoteDetailsActivity.this, getResources().getString(R.string.note_delete_fail));
                }
            }
        });
    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if (noteTitle == null || noteTitle.isEmpty()){
            titleEditText.setError(getResources().getString(R.string.title_require));
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

     void saveNoteToFirebase(Note note) {
         DocumentReference documentReference;
         if (isEditMode){
             documentReference = Utility.getCollectionReferenceForNotes().document(docId);
         }
         else{
             documentReference = Utility.getCollectionReferenceForNotes().document();
         }
         documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 if (task.isSuccessful()){
                     // note is added
                     Utility.showToast(NoteDetailsActivity.this, getResources().getString(R.string.note_add_success));
                     finish();
                 }
                 else{
                     // note is not added
                     Utility.showToast(NoteDetailsActivity.this, getResources().getString(R.string.note_add_fail));
                 }
             }
         });
    }


    private void initViews() {
        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);
    }
}