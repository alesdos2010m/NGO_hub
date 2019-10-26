package com.example.ngohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NGOSigninActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignin;
    private TextView text_register_ngo;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean saveLogin;
    CheckBox remember_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngosignin);
        auth = FirebaseAuth.getInstance();

        /*if (auth.getCurrentUser() != null) {
            startActivity(new Intent(NGOSigninActivity.this, MainNGOActivity.class));
            finish();
        } */
        // set the view now

        inputEmail = (EditText) findViewById(R.id.userid_input_ngo_signin);
        inputPassword = (EditText) findViewById(R.id.password_input_ngo_signin);
        btnSignin = (Button) findViewById(R.id.app_ngo_signin_btn);
        text_register_ngo = (TextView) findViewById(R.id.ngo_Register);
        remember_me=(CheckBox)findViewById(R.id.checkbox_input_rememberMe_ngo_signin);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin==true) {
            inputEmail.setText(loginPreferences.getString("email_id", ""));
            remember_me.setChecked(true);
        }
        else if (saveLogin==false) {
            inputEmail.setText(loginPreferences.getString(null,""));
            remember_me.setChecked(false);
        }

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


       text_register_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NGOSigninActivity.this, NGOSignupActivity.class));
            }
       });

       btnSignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (remember_me.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("email_id", email);
                    loginPrefsEditor.apply();
                }
                else if(remember_me.isChecked()==false) {
                    loginPrefsEditor.putBoolean("saveLogin", false);
                    loginPrefsEditor.putString("email_id", null);
                    loginPrefsEditor.apply();
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(NGOSigninActivity.this,"Password too short!", Toast.LENGTH_SHORT).show();
                }

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(NGOSigninActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.

                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NGOSigninActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(NGOSigninActivity.this,"Sorry,invalid credentials!,try again..", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            }
            });
    }
}
