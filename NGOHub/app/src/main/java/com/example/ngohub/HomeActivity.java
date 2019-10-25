package com.example.ngohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

//**************************************
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
    TextView loggedin_user;
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
    public String NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavigationView navigationView=(NavigationView) findViewById(R.id.navigation);
        View headerview = navigationView.getHeaderView(0);
        loggedin_user=(TextView)headerview.findViewById(R.id.loggedin_user);

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

        if(mauth.getCurrentUser()==null)
        {
            if(sessionManager.isLoggin())
            {
                loggedin_user.setText(f_name);
            }
            else
            {
                loggedin_user.setText("There!");
            }

        }
        else if(mauth.getCurrentUser()!=null)
        {
            String current_NGO_UUID = mauth.getCurrentUser().getUid();
            if (FirebaseDatabase.getInstance().getReference("NgoInformation").child(current_NGO_UUID) != null) {
                DatabaseReference ngo_NgoInformation_dbRef = FirebaseDatabase.getInstance().getReference("NgoInformation");
                ngo_NgoInformation_dbRef.child(current_NGO_UUID).child("NgoInformation").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        NAME = dataSnapshot.child("Name").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            loggedin_user.setText(NAME);
        }

        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.event_posts_list_view_layout, null);
        EventRegistration = view.findViewById(R.id.event_register);

        eventRegister= new EventRegister(f_name,m_name,l_name,gender,phone_no,email);

        if(mauth.getCurrentUser()!=null)
        {
            EventRegistration.setVisibility(EventRegistration.GONE);
        }
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