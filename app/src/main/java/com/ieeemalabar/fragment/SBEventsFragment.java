package com.ieeemalabar.fragment;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.ieeemalabar.R;
import com.ieeemalabar.models.User;

public class SBEventsFragment extends PostListFragment {

    public SBEventsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        SharedPreferences settings = getActivity().getSharedPreferences("com.ieeemalabar", getActivity().MODE_PRIVATE);
        settings = getActivity().getSharedPreferences("com.ieeemalabar", getActivity().MODE_PRIVATE);
        String college = settings.getString("college", "");
        // All my posts
        return databaseReference.child("user-posts")
                .child(college);
    }
}
