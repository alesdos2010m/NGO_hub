package com.example.ngohub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    TextView text_name;
    Button logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final SessionManager sessionManager=new SessionManager(this);
        sessionManager.checkLogin();
        setContentView(R.layout.activity_home);
        text_name=(TextView)findViewById(R.id.input_name);
        logout_button=(Button)findViewById(R.id.button_logout);
        HashMap<String,String> user=sessionManager.getUserDetail();
        String name=user.get(sessionManager.NAME);
        text_name.setText(name);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });
    }
}
