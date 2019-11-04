package com.example.ngohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

//**************************************
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.Inflater;

//**************************************
public class HomeActivity extends AppCompatActivity {
    TextView EventRegistration;
    SessionManager sessionManager; //For Maintaining Sessions for Volunteer
    TextView loggedin_user,loggedin_user_email;
    EventRegister eventRegister;

    //**********************************************************
    private RecyclerView recyclerView_Posts;
    private RecyclerView.LayoutManager layoutManager;

    EventPostsAdapter eventPostsAdapter;
    List<NGO_EventPosts> eventPostsList;

    //Created Firebase objects for maintaining sessions for vo/ngo
    DatabaseReference databaseReference;
    DatabaseReference eventRegistration_dbRef;                      //FirebaseDatabase Reference declaration
    private DatabaseReference ngo_EventPosts_dbRef;                 //FirebaseDatabase Reference declaration

    private FirebaseAuth mauth;
    String current_Post_UUID;
    public String NAME,Email_NGO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavigationView navigationView=(NavigationView) findViewById(R.id.navigation);
        View headerview = navigationView.getHeaderView(0);
        Menu menu =navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.button_logout);
        MenuItem share=menu.findItem(R.id.nav_share);
        MenuItem help=menu.findItem(R.id.help);

        loggedin_user=(TextView)headerview.findViewById(R.id.loggedin_user);
        loggedin_user_email=(TextView)headerview.findViewById(R.id.email_id_loggedin);

        NgoInformation ngo=new NgoInformation();
        sessionManager = new SessionManager(this); //Volunteer
        mauth = FirebaseAuth.getInstance();//Vo/NGO

        HashMap<String, String> user = sessionManager.getUserDetail();
        String f_name = user.get(sessionManager.F_NAME);
        String m_name=user.get(sessionManager.M_NAME);
        String l_name = user.get(sessionManager.L_NAME);
        String gender=user.get(sessionManager.GENDER);
        String phone_no = user.get(sessionManager.PHONE_NO);
        String email=user.get(sessionManager.EMAIL_ID);

        //This below code gives logic of sharing apk file
        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {

                    ApplicationInfo packageinfo = getApplicationContext().getPackageManager().getApplicationInfo("com.example.ngohub", 0);
                    File file = new File(packageinfo.publicSourceDir);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("application/vnd.android.package-archive");
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(),"com.ngohub.fileprovider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                   HomeActivity.this.grantUriPermission(HomeActivity.this.getPackageManager().toString(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent,"SHARE APP USING"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return true;
            }
        });
        //This below codes is about help.
        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL,Email_NGO);
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ngohub@example.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "NGOHub HelpLine");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, "Send Mail Via"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HomeActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
               return true;
            }
        });
        if(mauth.getCurrentUser()==null) {
            if (sessionManager.isLoggin()) {
                loggedin_user.setText(" " + f_name + " !");
                loggedin_user_email.setText(email);
            } else {
                //Set the visibility of logout as false.
                item.setVisible(false);
                loggedin_user.setText(" There !");
                loggedin_user_email.setText("Please Login");
            }
        }
        //NGo name and email in header.
        else if (mauth.getCurrentUser() != null) {

            FirebaseUser user_ngo = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference ngo_NgoInformation_dbRef = FirebaseDatabase.getInstance().getReference("NgoInformation").child(user_ngo.getUid());
            ngo_NgoInformation_dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    NAME = dataSnapshot.child("Name").getValue(String.class);
                    loggedin_user.setText(" " + NAME+ "!!");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            Email_NGO = user_ngo.getEmail();
            loggedin_user_email.setText(Email_NGO);
        }
        eventRegister= new EventRegister(f_name,m_name,l_name,gender,phone_no,email);
        //******************************************************
        //new code for recycler view and other views starts here
        recyclerView_Posts = findViewById(R.id.recyclerView_posts);
        recyclerView_Posts.setHasFixedSize(true);
        eventPostsList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView_Posts.setLayoutManager(layoutManager);
        current_Post_UUID = "TEMPORARY";
        //Firebase database reference definition
        ngo_EventPosts_dbRef = FirebaseDatabase.getInstance().getReference("NgoInformation").child(current_Post_UUID).child("User_Registered_For_Event");//database reference
        eventRegistration_dbRef = FirebaseDatabase.getInstance().getReference("EventRegister");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("NGO_EventPosts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    NGO_EventPosts ngo_eventPosts = postSnapshot.getValue(NGO_EventPosts.class);
                    eventPostsList.add(ngo_eventPosts);
                }
                //connecting adapter with RecyclerView
                eventPostsAdapter = new EventPostsAdapter(HomeActivity.this, eventPostsList);
                recyclerView_Posts.setAdapter(eventPostsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Oops... Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        //new code ends here
        //******************************************************

    }
    public void Eventclick(MenuItem view) {
        Intent intent = new Intent(HomeActivity.this, NGO_DashboardActivity.class);
        startActivity(intent);
    }

    //To logout
    public void Logout_Volunteer(MenuItem view) {

        if ((mauth.getCurrentUser() == null)) { //If person is not an NGO
            if (sessionManager.isLoggin() == true) { //He has loggined as Volunteer
                sessionManager.logout();
            }
            else {   //Or he has not logged in at all
                Toast.makeText(HomeActivity.this, "Please Login!", Toast.LENGTH_SHORT).show();
            }
        }
        else if (mauth.getCurrentUser() != null) { //If he is an NGO.
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(HomeActivity.this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            this.startActivity(i);
            ((HomeActivity) this).finish();
        }
    }
    public void registerUser(View view) {
        if(mauth.getCurrentUser()==null){
            if(sessionManager.isLoggin())
            {
                temp();
            }
            else
            {
                Toast.makeText(HomeActivity.this, "Please Login First...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
    private void temp()
    {
        String UserRegitered_UUID = UUID.randomUUID().toString();
        eventRegistration_dbRef.child(UserRegitered_UUID).setValue(eventRegister);

        //created reference to EventRegister from NGO_EventPosts
        DatabaseReference pushedEventRegistration_UUID_dbRef = ngo_EventPosts_dbRef.push();
        pushedEventRegistration_UUID_dbRef.setValue(UserRegitered_UUID);

        //created reference to NGO_EventPosts from EventRegister
        DatabaseReference pushedEventPosts_UUID_dbRef = eventRegistration_dbRef.push();
        pushedEventPosts_UUID_dbRef.setValue(current_Post_UUID);

        Toast.makeText(HomeActivity.this, "Registered For Event Successfully...", Toast.LENGTH_LONG).show();
    }
}