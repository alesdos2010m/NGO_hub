package com.example.ngohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainNGOActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ngo);
    }
    public void buttonClick(View view)
    {
        if(view.getId() == R.id.signup_btn_ngo)
        {
            Intent intent = new Intent(MainNGOActivity.this, NGOSignupActivity.class);
            startActivity(intent);
            finish();
        }
        else if(view.getId() == R.id.signin_btn_ngo)
        {
            Intent intent = new Intent(MainNGOActivity.this, NGOSigninActivity.class);
            startActivity(intent);
            finish();
        }
    }
}