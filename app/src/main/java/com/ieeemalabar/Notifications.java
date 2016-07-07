package com.ieeemalabar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ieeemalabar.models.Notification;
import com.ieeemalabar.viewholder.NotifViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Notifications extends AppCompatActivity {

    DatabaseReference mRef;
    RecyclerView myRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        loadFirebase();

        SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
        String hubmember = settings.getString("hubmember", "");
        if (hubmember.equals("true")) {
            //Button launches NewNotificationActivity
            findViewById(R.id.new_notification).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Notifications.this, NewNotification.class));
                }
            });
        } else {
            findViewById(R.id.new_notification).setVisibility(View.GONE);
        }


        myRecycler = (RecyclerView) findViewById(R.id.mRecycler);
        myRecycler.setNestedScrollingEnabled(false);
        myRecycler.setHasFixedSize(true);

        mRef = FirebaseDatabase.getInstance().getReference().child("notifications");

        FirebaseRecyclerAdapter mAdapter = new FirebaseRecyclerAdapter<Notification, NotifViewHolder>(Notification.class, R.layout.item_notifications, NotifViewHolder.class, mRef) {
            @Override
            public void populateViewHolder(NotifViewHolder viewHolder, Notification notif, int position) {
                viewHolder.setMessage(notif.getTitle(), notif.getMessage(), notif.getDate());
            }
        };
        myRecycler.setAdapter(mAdapter);

        LinearLayoutManager mManager;
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        myRecycler.setLayoutManager(mManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void loadFirebase() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("college")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        String college = "";
                        college = dataSnapshot.getValue().toString();
                        if (college != "") {
                            ProgressBar progressBar = (ProgressBar) findViewById(R.id.NotifprogressBar);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}