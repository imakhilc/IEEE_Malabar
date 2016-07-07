package com.ieeemalabar.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by AKHIL on 07-Jul-16.
 */
public class HubEventsFragment extends PostListFragment {

    public HubEventsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("hub-posts");
    }
}
