package com.example.ngohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
{
  Button login_btn,signup_btn;
  TextView skip_signin;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_btn =  findViewById(R.id.signin_btn);
        signup_btn = findViewById(R.id.signup_btn);
        skip_signin=findViewById(R.id.skip_signin);
    }
        public void buttonClick(View v)
        {

            if(v.getId() == R.id.signup_btn)
            {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);

            }
            else if(v.getId() == R.id.signin_btn)
            {
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
            }
            else if(v.getId() == R.id.skip_signin)
            {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }

        }



}
