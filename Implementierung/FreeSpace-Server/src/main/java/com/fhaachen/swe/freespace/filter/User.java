package com.fhaachen.swe.freespace.filter;

/**
 * Created by thomas on 29.11.2016.
 */
public class User {
    public String userid;
    public String token;
    public String role;

    public User(String userid, String token){
        this.userid = userid;
        this.token = token;
        System.out.println("New user: " + this.userid +  "Token: " + this.role);
    }
}
