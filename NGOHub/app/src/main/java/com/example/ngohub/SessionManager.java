package com.example.ngohub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL_ID = "EMAIL_ID";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String email_id,String name){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL_ID, email_id);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){

        if (!this.isLoggin()){
            Intent i = new Intent(context, HomeActivity.class);
            context.startActivity(i);
            ((SigninActivity) context).finish();
        }
    }
    public HashMap<String,String> getUserDetail(){

        HashMap<String,String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL_ID, sharedPreferences.getString(EMAIL_ID, null));
        return user;
    }
    public void logout(){

        editor.clear();
        editor.commit();
        Toast.makeText(context, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
        ((HomeActivity) context).finish();

    }
}