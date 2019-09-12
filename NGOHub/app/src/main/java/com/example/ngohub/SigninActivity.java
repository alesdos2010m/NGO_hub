package com.example.ngohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SigninActivity extends AppCompatActivity {
    EditText edit_email_id;
    EditText edit_password;
    Button btn_signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        edit_email_id = (EditText) findViewById(R.id.userid_input_signin);
        edit_password = (EditText) findViewById(R.id.password_input_signin);
        btn_signin = (Button) findViewById(R.id.app_signin_btn);


    }

    public void Login(View view) {

        String email_id =edit_email_id.getText().toString();
        String password =edit_password.getText().toString();
        String type = "signin";
        LoginBackgroundWorker loginbackgroundWorker = new LoginBackgroundWorker(this);
        loginbackgroundWorker.execute(type, email_id, password);



    }

}
