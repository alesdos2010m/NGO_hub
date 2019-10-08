package com.example.ngohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager=new SessionManager(this); //Volunteer
        mauth = FirebaseAuth.getInstance();  //Vo/NGO

        //Session Management for Volunteer
        if(sessionManager.isLoggin()==true)
        {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        //Session Management for Vo/NGO
        else if (mauth.getCurrentUser() != null) {

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
        public void buttonClick(View v)
        {
            if(v.getId() == R.id.iam_ngo)
            {
                Intent intent = new Intent(MainActivity.this, MainNGOActivity.class);
                startActivity(intent);
              finish();
            }
            else if(v.getId() == R.id.iam_volunteer)
            {
                Intent intent = new Intent(MainActivity.this, MainVolunteerActivity.class);
                startActivity(intent);
                finish();
            }
          else if(v.getId() == R.id.skip_signin)
            {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                //finish();
            }
        }
}