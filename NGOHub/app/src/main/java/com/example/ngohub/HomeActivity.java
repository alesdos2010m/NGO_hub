package com.example.ngohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//**************************************
public class HomeActivity extends AppCompatActivity {

    SessionManager sessionManager; //For Maintaining Sessions for Volunteer

    //**********************************************************
    private RecyclerView recyclerView_Posts;
    private RecyclerView.LayoutManager layoutManager;

    EventPostsAdapter eventPostsAdapter;
    List<NGO_EventPosts> eventPostsList;
    //Created Firebase objects for maintaining sessions for vo/ngo
    DatabaseReference databaseReference;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this); //Volunteer
        mauth = FirebaseAuth.getInstance();//Vo/NGO


        HashMap<String, String> user = sessionManager.getUserDetail();
        String name = user.get(sessionManager.NAME);
        String email=user.get(sessionManager.EMAIL_ID);

        //******************************************************
        //new code for recycler view and other views starts here
        recyclerView_Posts = findViewById(R.id.recyclerView_posts);
        recyclerView_Posts.setHasFixedSize(true);
        eventPostsList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView_Posts.setLayoutManager(layoutManager);

        //Firebase database reference definition
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
            } else {   //Or he has not logged in at all
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
}