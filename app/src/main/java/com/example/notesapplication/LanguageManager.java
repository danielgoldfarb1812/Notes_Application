package com.example.notesapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context context;
    private SharedPreferences sharedPreferences;

    // יצירת מנהל שפה עם ההתחברות למסד הנתונים
    public LanguageManager(Context c){
        context = c;
        sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
    }

    // עדכון השפה במשאבי האפליקציה לפי שפה מסוימת
    public void updateResource(String code){
        Locale locale = new Locale(code);
        locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        // שמירת השפה במשתנה SharedPreferences על מנת לשמור אותה בזיכרון
        setLang(code);
    }

    // החזרת השפה הנוכחית מתוך משתנה SharedPreferences
    public String getLang(){
        return sharedPreferences.getString("lang", "en");
    }

    // עדכון השפה במשתנה SharedPreferences
    public void setLang(String code){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang", code);
        editor.commit();
    }
}
