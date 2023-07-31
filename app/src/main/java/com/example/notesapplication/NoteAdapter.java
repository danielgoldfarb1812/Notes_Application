package com.example.notesapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import io.grpc.okhttp.internal.Util;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    Context context;

    // בנאי המקבל פרמטרים את ה-options וה-context
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        // מילוי הנתונים של הפתק בתיבות הטקסט בתצוגה
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));

        // הגדרת אירוע הקלקה לפריט בריסייקלר
        holder.itemView.setOnClickListener((v) -> {
            // יצירת אינטנט להעברה לפעילות של עריכת הפתק (NoteDetailsActivity)
            Intent intent = new Intent(context, NoteDetailsActivity.class);

            // שימור נתוני הפתק (כותרת, תוכן ומזהה של הפתק) כנתוני נוסעים לפעילות הבאה
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);

            // קבלת מזהה הפתק מסיפור ה-snapshot ושמירתו כנתון נוסע לפעילות הבאה
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);

            // הפעלת האינטנט והעברה לפעילות הראשונית
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // יצירת התצוגה של הפריט בריסייקלר (recycler_note_item.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent,false);

        // יצירת האובייקט המחזיק לתוכו את ה-View המתאים לפריט
        return new NoteViewHolder(view);
    }

    // מחלקה שמקבלת את התצוגה של כל פריט בריסייקלר
    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, contentTextView, timestampTextView;

        // בנאי המקבל את התצוגה ומאתחל את התצוגות למשתני המחלקה
        public NoteViewHolder(@NonNull View itemView){
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);
        }
    }
}
