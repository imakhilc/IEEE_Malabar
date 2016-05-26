package com.ieeemalabar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.ieeemalabar.fragment.SignIn;

public class ActivityMain extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }

        setTitle("Events");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //setRetainInstance(true);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), this));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_event_light);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notification_dark);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_event_dark);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_light);
                    setTitle("Events");
                }
                else if(tab.getPosition()==1) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_notification_light);
                    setTitle("Notifications");
                }
                else if(tab.getPosition()==2) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_light);
                    setTitle("Events");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_dark);
                else if(tab.getPosition()==1)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_notification_dark);
                else if(tab.getPosition()==2)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_dark);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_light);
                else if(tab.getPosition()==1)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_notification_light);
                else if(tab.getPosition()==2)
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_light);
            }
        });
    }

    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments [] = {"","",""};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MainActivity();
                case 1:
                    return new SignIn();
                case 2:
                    return new SignIn();
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

}
