package com.ieeemalabar.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String name;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username) {
        this.name = name;
    }

}
// [END blog_user_class]
