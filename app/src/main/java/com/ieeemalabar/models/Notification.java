package com.ieeemalabar.models;

/**
 * Created by AKHIL on 01-Jul-16.
 */
public class Notification {

    String title;
    String message;
    String date;

    public Notification() {
    }

    public Notification(String title, String message, String date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}