package com.ieeemalabar.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TopStarredFragment extends PostListFragment {

    public TopStarredFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_posts_query]
        // My top posts by number of stars
        Query myTopPostsQuery = databaseReference.child("posts").orderByChild("starCount");
        // [END my_top_posts_query]

        return myTopPostsQuery;
    }
}
