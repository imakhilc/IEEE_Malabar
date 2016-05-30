package com.ieeemalabar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.batch.android.Batch;
import com.batch.android.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

        AM = this;
        loadFirebase();

        Batch.Push.setGCMSenderId("972033953090");
        Batch.setConfig(new Config("DEV574A78655733CB7AF4474E32F62")); //Dev
        //Batch.setConfig(new Config("574A78655589D5CA100B99DA0A86F5")); //Live

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        setTitle("All Events");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //setRetainInstance(true);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), this));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_event_light);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_college_dark);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_stars_dark);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_light);
                    setTitle("All Events");
                } else if (tab.getPosition() == 1) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_college_light);
                    setTitle("Our SB Events");
                } else if (tab.getPosition() == 2) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_stars_light);
                    setTitle("Top Starred Events");
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
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_stars_light);
            }
        });

        /*FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("college")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        String college = dataSnapshot.toString();
                        SharedPreferences settings = getSharedPreferences("com.ieeemalabar", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("college", college);
                        editor.commit();
                        Toast.makeText(ActivityMain.this, college, Toast.LENGTH_SHORT).show();
                        pd.hide();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

        // Button launches NewPostActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMain.this, NewPostActivity.class));
            }
        });

    }

    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments[] = {"", "", ""};

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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Batch.onStart(this);
    }

    @Override
    protected void onStop()
    {
        Batch.onStop(this);

        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Batch.onDestroy(this);

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        Batch.onNewIntent(this, intent);

        super.onNewIntent(intent);
    }

    public void loadFirebase(){
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
    public void onBackPressed(){
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
}
