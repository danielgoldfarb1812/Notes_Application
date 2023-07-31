package com.example.notesapplication;

import com.google.firebase.Timestamp;

public class Note {
    // משתני המחלקה שמייצגים את נתוני הפתק
    String title;
    String content;
    Timestamp timestamp;

    // בנאי ברירת מחדל, מתבצע כאשר יש צורך ליצור אובייקט מהמחלקה ולא מועברים לו נתונים
    public Note() {
    }

    // פונקציות ה-get וה-set מאפשרות לגשת ולשנות את הערכים של המשתנים במחלקה
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
