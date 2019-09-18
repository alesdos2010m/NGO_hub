package com.example.ngohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NGOSignupActivity extends AppCompatActivity {
    EditText name_ngo;
    EditText id_ngo;
    EditText contact_no_ngo;
    EditText email_id_ngo;
    EditText password1_ngo,password2_ngo;
    CheckBox accept_ngo,valid_ngo;
    Button btn_signup_ngo;
    TextView login_ngo; //Already have an account?
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngosignup);
        name_ngo=(EditText)findViewById(R.id.name_input_ngo_signup);
        id_ngo=(EditText)findViewById(R.id.unique_id_input_ngo_signup);
        contact_no_ngo=(EditText)findViewById(R.id.contact_no_input_ngo_signup);
        email_id_ngo=(EditText)findViewById(R.id.email_id_input_ngo_signup);
        password1_ngo=(EditText)findViewById(R.id.password1_input_ngo_signup);
        password2_ngo=(EditText)findViewById(R.id.password2_input_ngo_signup);
        accept_ngo=(CheckBox)findViewById(R.id.checkbox_input_accept_TnC_ngo_signup);
        valid_ngo=(CheckBox)findViewById(R.id.checkbox_input_valid_info_ngo_signup);
        btn_signup_ngo=(Button)findViewById(R.id.app_signup_btn_ngo);
        login_ngo=(TextView)findViewById(R.id.Login_ngo);
        databaseReference= FirebaseDatabase.getInstance().getReference("NgoInformation");
        firebaseAuth=FirebaseAuth.getInstance();



    }
    public void RegisterUser(View view) {
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

                                NgoInformation ngoinfo = new NgoInformation(
                                        name,
                                        id,
                                        contact_no,
                                        email_id

                                );

                                FirebaseDatabase.getInstance().getReference("NgoInformation")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(ngoinfo).addOnCompleteListener(NGOSignupActivity.this,new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(NGOSignupActivity.this, "Registration Sucess", Toast.LENGTH_LONG).show();
                                            Intent intent=new Intent(NGOSignupActivity.this,NGOSigninActivity.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(NGOSignupActivity.this, "Registration UnSucessful,Please try again!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(NGOSignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }





    public void Login(View view)
    {
        Intent intent=new Intent(NGOSignupActivity.this,NGOSigninActivity.class);
        startActivity(intent);
    }




    }

