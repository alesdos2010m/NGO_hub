package com.example.ngohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NGOSignupActivity extends AppCompatActivity {
    TextView name_ngo;
    TextView id_ngo;
    TextView contact_no_ngo;
    TextView email_id_ngo;
    TextView password1_ngo,password2_ngo;
    CheckBox accept_ngo,valid_ngo;
    Button btn_signup_ngo;
    TextView login_ngo; //Already have an account?
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();

    }
    public void registeruser(View view) {
        final String name = name_ngo.getText().toString();
        final String id = id_ngo.getText().toString();
        final String contact_no = contact_no_ngo.getText().toString();
        final String email_id = email_id_ngo.getText().toString();
        String password1 = password1_ngo.getText().toString();
        String password2 = password2_ngo.getText().toString();


        firebaseAuth.createUserWithEmailAndPassword(email_id, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String ngo_id = databaseReference.push().getKey();
                            NGO_Info ngo_info = new NGO_Info(
                                    name,
                                    id,
                                    contact_no,
                                    email_id
                            );
                            databaseReference.child(ngo_id).setValue(ngo_info);
                            Toast.makeText(getApplicationContext(),"HII", Toast.LENGTH_LONG).show();

                        }


                    }



                });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngosignup);
        name_ngo=(TextView)findViewById(R.id.name_input_ngo_signup);
        id_ngo=(TextView)findViewById(R.id.unique_id_input_ngo_signup);
        contact_no_ngo=(TextView)findViewById(R.id.contact_no_input_ngo_signup);
        email_id_ngo=(TextView)findViewById(R.id.email_id_input_ngo_signup);
        password1_ngo=(TextView)findViewById(R.id.password1_input_ngo_signup);
        password2_ngo=(TextView)findViewById(R.id.password2_input_ngo_signup);
        accept_ngo=(CheckBox)findViewById(R.id.checkbox_input_accept_TnC_ngo_signup);
        valid_ngo=(CheckBox)findViewById(R.id.checkbox_input_valid_info_ngo_signup);
        btn_signup_ngo=(Button)findViewById(R.id.app_signup_btn_ngo);
        login_ngo=(TextView)findViewById(R.id.Login_ngo);
        databaseReference= FirebaseDatabase.getInstance().getReference("NGO_Info");
        firebaseAuth=FirebaseAuth.getInstance();
        String ngo_id = databaseReference.push().getKey();


    }
    public void Login(View view)
    {
        Intent intent=new Intent(NGOSignupActivity.this,NGOSigninActivity.class);
        startActivity(intent);
    }




    }

