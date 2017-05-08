package com.example.saka.myapplication.model;

/**
 * Created by Saka on 02-May-17.
 */

public class User {

    private String uid;
    private String name;
    private String email;

    public User() {
        //  Default constructor
    }

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
