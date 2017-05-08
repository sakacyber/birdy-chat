package com.example.saka.myapplication.model;

/**
 * Created by Saka on 02-May-17.
 */

public class Comment {

    public String uid;
    public String author;
    public String comment;

    public Comment() {
        //  Default constructor
    }

    public Comment(String uid, String author, String comment) {
        this.uid = uid;
        this.author = author;
        this.comment = comment;
    }
}
