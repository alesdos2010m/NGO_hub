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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//**************************************
public class HomeActivity extends AppCompatActivity {
    //Button logout_button;                                                 //logout button


    //**********************************************************
    private RecyclerView recyclerView_Posts;
    private RecyclerView.LayoutManager layoutManager;

    EventPostsAdapter eventPostsAdapter;
    List<NGO_EventPosts> eventPostsList;
    DatabaseReference databaseReference;                                    //Firebase database reference declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //old code for logout button and old things
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*final SessionManager sessionManager=new SessionManager(this);
        sessionManager.checkLogin();
        setContentView(R.layout.activity_home);

        logout_button=(Button)findViewById(R.id.button_logout);
        HashMap<String,String> user=sessionManager.getUserDetail();
        String name=user.get(sessionManager.NAME);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });
        //old code ends here */
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
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
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

    public void Eventclick(MenuItem view)
    {
        Intent intent = new Intent(HomeActivity.this, NGO_DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}