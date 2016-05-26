package com.ieeemalabar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ieeemalabar.LoginActivity;
import com.ieeemalabar.NewPostActivity;
import com.ieeemalabar.R;
import com.ieeemalabar.fragment.MyPostsFragment;
import com.ieeemalabar.fragment.MyTopPostsFragment;
import com.ieeemalabar.fragment.RecentPostsFragment;
import com.ieeemalabar.fragment.SignIn;

public class MainActivity extends Fragment {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    Spinner mOptionField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOptionField = (Spinner) getView().findViewById(R.id.select_option);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.events, R.layout.college_list);
        mOptionField.setAdapter(adapter);

        mOptionField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 0) {
                    RecentPostsFragment mainfrag = new RecentPostsFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getChildFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, mainfrag);
                    fragmentTransaction.commit();
                } else if (position == 1) {
                    MyPostsFragment mainfrag = new MyPostsFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getChildFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, mainfrag);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        // Button launches NewPostActivity
        getView().findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewPostActivity.class));
                //getActivity().finish();
            }
        });
    }
}
