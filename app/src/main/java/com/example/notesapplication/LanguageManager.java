package com.example.notesapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context context;
    private SharedPreferences sharedPreferences;

    public LanguageManager(Context c){
        context = c;
        sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);
    }

    public void updateResource(String code){
        Locale locale = new Locale(code);
        locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setLang(code);
    }

    public String getLang(){
        return sharedPreferences.getString("lang", "en");
    }
    public void setLang(String code){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang", code);
        editor.commit();
    }
}
