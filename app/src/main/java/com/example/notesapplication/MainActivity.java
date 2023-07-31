package com.example.notesapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    // משתני עזר לפעילות
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // הגדרת התצוגה הנדרשת בפעילות
        initViews();

        // הגדרת פעולות הכפתורים
        initButtons();

        // הגדרת תצוגת הריסייקלר והאדפטר שלו
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        // שאילתת האזמורות מהפיירבייס והגדרת התצוגה בהתאם לזמן ההוספה
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // התחלת צפייה בנתוני האזמורות בפיירבייס כשהפעילות פותחת
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // הפסקת הצפייה בנתוני האזמורות בפיירבייס כשהפעילות נסגרת
        noteAdapter.stopListening();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // ניתוב מחדש של המסך כאשר הוא נפתח מחדש לאחר סגירת פעילות נוספת
        noteAdapter.notifyDataSetChanged();
    }

    private void initButtons() {
        // הגדרת פעולת הכפתור הגדול (FloatingActionButton) להוספת פתק חדש
        addNoteBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NoteDetailsActivity.class));
        });
        // הגדרת פעולת הכפתור המציג תפריט נפתח עם אפשרות להתנתק (לוגאוט)
        menuBtn.setOnClickListener(v -> {
            showMenu();
        });
    }

    private void showMenu() {
        // הצגת תפריט נפתח כשלוחצים על הכפתור המציג אותו
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuBtn);

        // הוספת פריט לתפריט הנפתח - לוגאוט
        popupMenu.getMenu().add(getResources().getString(R.string.logout));

        // הצגת התפריט על המסך
        popupMenu.show();

        // הגדרת התגובה עם הפריט הנבחר בתפריט
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals(getResources().getString(R.string.logout))){
                    // התנתקות מהפיירבייס והעברה לפעילות ההתחברות (LoginActivity)
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    private void initViews() {
        // חיבור המשתנים בקוד לפריטים ב-layout של הפעילות
        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);
    }

}