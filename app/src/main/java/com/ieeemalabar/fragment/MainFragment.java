package com.ieeemalabar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.ieeemalabar.LoginActivity;
import com.ieeemalabar.MainActivity;
import com.ieeemalabar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Events");
        viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
        setRetainInstance(true);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new CustomAdapter(getFragmentManager(), getActivity()));

        tabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_event_light);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notification_dark);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_event_dark);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            //onAuthSuccess(mAuth.getCurrentUser());
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_light);
                    getActivity().setTitle("Events");
                }
                else if(tab.getPosition()==1) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_notification_light);
                    getActivity().setTitle("Notifications");
                }
                else if(tab.getPosition()==2) {
                    tabLayout.getTabAt(tab.getPosition()).setIcon(R.drawable.ic_event_light);
                    getActivity().setTitle("Events");
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

}
