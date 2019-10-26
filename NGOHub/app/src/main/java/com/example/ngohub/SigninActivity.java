package com.example.ngohub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class SigninActivity extends AppCompatActivity {
    EditText edit_email_id;
    EditText edit_password;
    Button btn_signin;
    CheckBox remember_me;
    TextView text_register;
    SessionManager sessionManager;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        sessionManager=new SessionManager(this);
        edit_email_id = (EditText) findViewById(R.id.userid_input_signin);
        edit_password = (EditText) findViewById(R.id.password_input_signin);
        btn_signin = (Button) findViewById(R.id.app_signin_btn);
        text_register=(TextView)findViewById(R.id.Register);
        remember_me=(CheckBox)findViewById(R.id.checkbox_input_rememberMe_signin);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin==true) {
            edit_email_id.setText(loginPreferences.getString("email_id", ""));
            remember_me.setChecked(true);
        }
        else if (saveLogin==false) {
            edit_email_id.setText(loginPreferences.getString(null,""));
            remember_me.setChecked(false);
        }
    }
    //Not Registered? Register.
    public void Register(View view)
    {
        Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
    //Validates the Login credentials from the database upon clicking the Signin button
    public void Login(View view) {
        String email_id =edit_email_id.getText().toString();
        String password =edit_password.getText().toString();
        String type = "signin";

        if (remember_me.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("email_id", email_id);
            loginPrefsEditor.apply();
        }
        else if(remember_me.isChecked()==false) {
            loginPrefsEditor.putBoolean("saveLogin", false);
            loginPrefsEditor.putString("email_id", null);
            loginPrefsEditor.apply();
        }

        if(TextUtils.isEmpty(email_id)&&TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "Email id and Password cannot be blank", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(email_id))
        {
            Toast.makeText(getApplicationContext(), "Email_id cannot be blank", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "Password cannot be blank", Toast.LENGTH_LONG).show();
        }
        else
            {
            LoginBackgroundWorker loginbackgroundWorker = new LoginBackgroundWorker(this);
            loginbackgroundWorker.execute(type, email_id, password);
        }
    }
    public class LoginBackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        //String email;
        String First_name,Middle_name,Last_name,Gender,Phone_no,Email_id;



        public LoginBackgroundWorker(Context contxt) {
            context = contxt;
        }
        @Override
        protected String doInBackground(String... params) {
            String type=params[0];
            String login_url="http://ioit.acm.org/NGOHub/php/login.php";
            if(type.equals("signin"))
            {
                try {
                    String email_id=params[1];
                    String password=params[2];
                    URL url=new URL(login_url);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data= URLEncoder.encode("email_id","UTF-8")+"="+URLEncoder.encode(email_id,"UTF-8")+"&"+
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

        protected void onPostExecute(String result){

            String result1="Sorry,the Email ID does not exist,Please Register Yourself.";
            String result2="Invalid Email id or password,Please try again";
            if(result.equals(result1)) {
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SigninActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if(result.equals(result2)) {
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SigninActivity.this,SigninActivity.class);
                startActivity(intent);
                finish();
            }
            //Login Successful
            else {

                Toast.makeText(getApplicationContext(),"Login Successful!", Toast.LENGTH_LONG).show();

                try { //Creating json object to fetch data from php to java objects.
                    JSONArray array = new JSONArray(result);


                        //getting product object from json array
                        JSONObject first_name = array.getJSONObject(0);
                         First_name=first_name.getString("First_name");

                        JSONObject middle_name = array.getJSONObject(0);
                         Middle_name=middle_name.getString("Middle_name");

                        JSONObject last_name = array.getJSONObject(0);
                         Last_name=last_name.getString("Last_name");

                        JSONObject gender = array.getJSONObject(0);
                         Gender=gender.getString("Gender");

                        JSONObject contact_no  = array.getJSONObject(0);
                         Phone_no=contact_no.getString("Phone_no");

                        JSONObject email_id = array.getJSONObject(0);
                         Email_id=email_id.getString("Email_ID");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

                sessionManager.createSession(First_name,Middle_name,Last_name,Gender,Phone_no,Email_id);
                Intent intent=new Intent(SigninActivity.this,HomeActivity.class);
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