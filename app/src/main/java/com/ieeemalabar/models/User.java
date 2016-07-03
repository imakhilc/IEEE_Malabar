package com.ieeemalabar.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String name;
    public String college;
    public String position;
    public String ieee;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String name, String college, String position, String ieee) {
        this.username = username;
        this.name = name;
        this.college = college;
        this.position = position;
        this.ieee = ieee;
    }

    public User(String username) {
        this.username = username;
    }

}
// [END blog_user_class]
