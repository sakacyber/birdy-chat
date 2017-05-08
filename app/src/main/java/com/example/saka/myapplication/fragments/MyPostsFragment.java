package com.example.saka.myapplication.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by Saka on 03-May-17.
 */

public class MyPostsFragment extends PostListFragment {

    public MyPostsFragment() {
        // Default constructor
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // Return all my new post
        return databaseReference.child("user-posts").child(getUid()).limitToFirst(50);
    }
}
