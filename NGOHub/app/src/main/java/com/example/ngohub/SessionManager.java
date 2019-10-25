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
    public static final String F_NAME = "FIRST_NAME";
    public static final String M_NAME = "MIDDLE_NAME";
    public static final String L_NAME = "LAST_NAME";
    public static final String GENDER = "GENDER";
    public static final String PHONE_NO = "PHONE_NO";
    public static final String EMAIL_ID = "EMAIL_ID";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String f_name,String m_name,String l_name,String gender,String phone_no,String email_id){
        editor.putBoolean(LOGIN, true);
        editor.putString(F_NAME, f_name);
        editor.putString(M_NAME, m_name);
        editor.putString(L_NAME, l_name);
        editor.putString(GENDER, gender);
        editor.putString(PHONE_NO, phone_no);
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
        user.put(F_NAME, sharedPreferences.getString(F_NAME, null));
        user.put(M_NAME, sharedPreferences.getString(M_NAME, null));
        user.put(L_NAME, sharedPreferences.getString(L_NAME, null));
        user.put(GENDER, sharedPreferences.getString(GENDER, null));
        user.put(PHONE_NO, sharedPreferences.getString(PHONE_NO, null));
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