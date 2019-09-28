package com.example.ngohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
  Button iamngo,iamvolunteer;
  //TextView skip_signin;
  SessionManager sessionManager;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager=new SessionManager(this);
        iamngo =  findViewById(R.id.iam_ngo);
        iamvolunteer = findViewById(R.id.iam_volunteer);
        //skip_signin=findViewById(R.id.skip_signin);
        if(sessionManager.isLoggin()==true)
        {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            sessionManager.checkLogin();
        }
    }
        public void buttonClick(View v)
        {
            if(v.getId() == R.id.iam_ngo)
            {
                Intent intent = new Intent(MainActivity.this, MainNGOActivity.class);
                startActivity(intent);
                //finish();
            }
            else if(v.getId() == R.id.iam_volunteer)
            {
                Intent intent = new Intent(MainActivity.this, MainVolunteerActivity.class);
                startActivity(intent);
                //finish();
            }
          else if(v.getId() == R.id.skip_signin)
            {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }



}
