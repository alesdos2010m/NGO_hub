package com.example.ngohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SignupActivity extends AppCompatActivity {
    EditText edit_first_name,edit_middle_name,edit_last_name,edit_contact_no,edit_email_id,edit_password1,edit_password2;
    RadioGroup radiogrp_gender;
    RadioButton selectedradiobutton;
    Button btn_signup;
    TextView text_login;
    CheckBox check_valid_info,check_accept_TnC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edit_first_name=(EditText)findViewById(R.id.first_name_input_signup);
        edit_middle_name=(EditText)findViewById(R.id.middle_name_input_signup);
        edit_last_name=(EditText)findViewById(R.id.last_name_input_signup);
        edit_contact_no=(EditText)findViewById(R.id.contact_no_input_signup);
        edit_email_id=(EditText)findViewById(R.id.email_id_input_signup);
        edit_password1=(EditText)findViewById(R.id.password1_input_signup);
        edit_password2=(EditText)findViewById(R.id.password2_input_signup);
        check_valid_info=(CheckBox)findViewById(R.id.checkbox_input_valid_info_signup);
        check_accept_TnC=(CheckBox)findViewById(R.id.checkbox_input_accept_TnC_signup);
        radiogrp_gender = (RadioGroup) findViewById(R.id.gender_input_button);
        btn_signup=(Button)findViewById(R.id.app_signup_btn);
        btn_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get the selected RadioButton of the group

                selectedradiobutton  = (RadioButton)findViewById(radiogrp_gender.getCheckedRadioButtonId());

                String first_name = edit_first_name.getText().toString();
                String middle_name = edit_middle_name.getText().toString();
                String last_name = edit_last_name.getText().toString();
                String gender = selectedradiobutton.getText().toString();
                String contact_no = edit_contact_no.getText().toString();
                String email_id = edit_email_id.getText().toString();
                String password1 = edit_password1.getText().toString();
                String password2 = edit_password2.getText().toString();
                String type = "register";

                if(check_valid_info.isChecked() && check_accept_TnC.isChecked()) {
                    if (TextUtils.isEmpty(first_name) || TextUtils.isEmpty(middle_name) || TextUtils.isEmpty(last_name) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(contact_no) || TextUtils.isEmpty(email_id) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
                        Toast.makeText(getApplicationContext(), "Please fill all the entries", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(first_name) && TextUtils.isEmpty(middle_name) && TextUtils.isEmpty(last_name)  && TextUtils.isEmpty(contact_no) && TextUtils.isEmpty(email_id) && TextUtils.isEmpty(password1) && TextUtils.isEmpty(password2)) {
                        Toast.makeText(getApplicationContext(), "Please fill all the entries", Toast.LENGTH_LONG).show();
                    }
                    else if (password1.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    }
                    else if (!(password1.equals(password2))) {
                        Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        SignupActivity.RegisterBackgroundWorker registerbackgroundWorker = new SignupActivity.RegisterBackgroundWorker(SignupActivity.this);
                        registerbackgroundWorker.execute(type, first_name, middle_name, last_name, gender, contact_no, email_id, password1);
                        finish();
                    }
                }
                else if(!check_accept_TnC.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please Accept the Terms and Conditions to continue", Toast.LENGTH_LONG).show();
                }
                else if(!check_valid_info.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please Accept that the information provided by you is valid!", Toast.LENGTH_LONG).show();
                }
                else if(!(check_valid_info.isChecked() && check_accept_TnC.isChecked())) {
                    Toast.makeText(getApplicationContext(), "Please Accept the Terms and Conditions and Verify your credentials to continue", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //Already have an account? Login.
    public void Login(View view)
    {
        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
    }

    public class RegisterBackgroundWorker extends AsyncTask<String,Void,String> {

        Context context;

        public RegisterBackgroundWorker(Context contxt) {
            context = contxt;
        }

        @Override
        protected String doInBackground(String... params) {
            String type=params[0];
            String register_url="http://ioit.acm.org/NGOHub/php/register.php";
            if(type.equals("register"))
            {
                try {
                    String first_name=params[1];
                    String middle_name=params[2];
                    String last_name=params[3];
                    String gender=params[4];
                    String contact_no=params[5];
                    String email_id=params[6];
                    String password=params[7];
                    URL url=new URL(register_url);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
//                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data= URLEncoder.encode("first_name","UTF-8")+"="+URLEncoder.encode(first_name,"UTF-8")+"&"+
                            URLEncoder.encode("middle_name","UTF-8")+"="+URLEncoder.encode(middle_name,"UTF-8")+"&"+
                            URLEncoder.encode("last_name","UTF-8")+"="+URLEncoder.encode(last_name,"UTF-8")+"&"+
                            URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8")+"&"+
                            URLEncoder.encode("contact_no","UTF-8")+"="+URLEncoder.encode(contact_no,"UTF-8")+"&"+
                            URLEncoder.encode("email_id","UTF-8")+"="+URLEncoder.encode(email_id,"UTF-8")+"&"+
                            URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line;
                    while((line=bufferedReader.readLine())!=null)
                    {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            String result1="Email ID already Exists";
            String result2="Registered Successfully";
            if(result.equals(result1)) {
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(intent);
                finish();
            }
            else if(result.equals(result2)) {
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Please Login to continue!", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(intent);
                finish();
            }
            else if(result.equals("NOT Registered Successfully")) {
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SignupActivity.this,SignupActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}