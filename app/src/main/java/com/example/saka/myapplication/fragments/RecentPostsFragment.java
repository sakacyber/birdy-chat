package com.example.saka.myapplication.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentPostsFragment extends PostListFragment {

    public RecentPostsFragment() {
        // Default constructor
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // The last 100 posts, these are automatically the 100 most recent posts
        // due to by sorting by push() keys
        return databaseReference.child("posts").limitToFirst(100);
    }
}
