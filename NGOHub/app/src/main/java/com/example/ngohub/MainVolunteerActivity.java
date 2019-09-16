package com.example.ngohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainVolunteerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_volunteer);
    }
    public void buttonClick(View view) {
        if (view.getId() == R.id.signup_btn) {
            Intent intent = new Intent(MainVolunteerActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        }
        else if (view.getId() == R.id.signin_btn) {
            Intent intent = new Intent(MainVolunteerActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        }
        else if (view.getId() == R.id.skip_signin) {
            Intent intent = new Intent(MainVolunteerActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

