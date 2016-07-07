package com.ieeemalabar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ieeemalabar.fragment.HubEventsFragment;
import com.ieeemalabar.fragment.PostListFragment;
import com.ieeemalabar.fragment.RecentEventsFragment;
import com.ieeemalabar.fragment.SBEventsFragment;
import com.ieeemalabar.fragment.SignIn;
import com.ieeemalabar.fragment.TopStarredFragment;
import com.ieeemalabar.models.User;

public class ActivityMain extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    public static ActivityMain AM;
    public Boolean close = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();

        AM = this;
        loadFirebase();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        setTitle("All Events");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //setRetainInstance(true);
        viewPager.setOffscreenPageLimit(4);
        setAdapterfn();
    }

    public void setAdapterfn(){
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), this));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_event_light);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_college_dark);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_hub_dark);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_stars_dark);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_light);
                    setTitle("All Events");
                    findViewById(R.id.fab_new_post).setVisibility(View.GONE);
                    //addOnClickSB();

                } else if (tab.getPosition() == 1) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_college_light);
                    setTitle("Our SB Events");
                    addOnClickSB();

                } else if (tab.getPosition() == 2) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_hub_light);
                    setTitle("Hub Events");
                    addOnClickHUB();

                } else if (tab.getPosition() == 3) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_stars_light);
                    setTitle("Top Starred Events");
                    findViewById(R.id.fab_new_post).setVisibility(View.GONE);
                    //addOnClickSB();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_dark);
                else if (tab.getPosition() == 1)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_college_dark);
                else if (tab.getPosition() == 2)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_hub_dark);
                else if (tab.getPosition() == 3)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_stars_dark);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_light);
                else if (tab.getPosition() == 1)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_college_light);
                else if (tab.getPosition() == 2)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_hub_light);
                else if (tab.getPosition() == 3)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_stars_light);
            }
        });

        findViewById(R.id.fab_new_post).setVisibility(View.GONE);
    }

    public void addOnClickSB(){
        SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
        String position = settings.getString("position", "");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_post);
        if (position.equals("Other") || position.equals("Non-execom Member")) {
            fab.setVisibility(View.GONE);
        } else {
        //Button launches NewPostActivity
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(ActivityMain.this, NewPostActivity.class));
                    Intent intent = new Intent(ActivityMain.this, NewPostActivity.class);
                    intent.putExtra("SBorH", "sbevent");
                    intent.putExtra("condition", "new");
                    intent.putExtra("post_key", "null");
                    intent.putExtra("college", "null");
                    startActivity(intent);
                }
            });
        }
    }

    public void addOnClickHUB(){
        SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
        String hub = settings.getString("hubmember", "");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_post);
        if (hub.equals("true")) {
            //Button launches NewPostActivity
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(ActivityMain.this, NewPostActivity.class));
                    Intent intent = new Intent(ActivityMain.this, NewPostActivity.class);
                    intent.putExtra("SBorH", "hubevent");
                    intent.putExtra("condition", "new");
                    intent.putExtra("post_key", "null");
                    intent.putExtra("college", "null");
                    startActivity(intent);
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments[] = {"", "", "", ""};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RecentEventsFragment();
                case 1:
                    return new SBEventsFragment();
                case 2:
                    return new HubEventsFragment();
                case 3:
                    return new TopStarredFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("user", "");
                                editor.putString("pass", "");
                                editor.commit();

                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you really want to log out?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .setTitle("Logout")
                        .show();
                return true;
            case R.id.profile:
                Intent intent = new Intent(ActivityMain.this, Profile.class);
                intent.putExtra("message", "me");
                startActivity(intent);
                return true;
            case R.id.notification:
                startActivity(new Intent(this, Notifications.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (close) {
            SplashScreen.SP.finish();
            super.onBackPressed();
            return;
        }

        this.close = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                close = false;
            }
        }, 2000);
    }

    @Override
    public void onResume(){
        super.onResume();

        SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
        String change = settings.getString("prof_change", "");

        if(change.equals("yes")) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("prof_change", "no");
            editor.commit();
            setAdapterfn();
        }
    }
}
