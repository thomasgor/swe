package com.fhaachen.swe.freespace.filter;

/**
 * Created by thomas on 29.11.2016.
 */
public class User {
    public String username;
    public String role;

    public User(String username, String role){
        this.username = username;
        this.role = role;

        System.out.println("New user: " + this.username +  "Role: " + this.role);
    }
}
