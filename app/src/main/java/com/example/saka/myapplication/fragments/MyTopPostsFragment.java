package com.example.saka.myapplication.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyTopPostsFragment extends PostListFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        //String myUserId = getUid();
        return databaseReference.child("user-posts").child(getUid())
                .orderByChild("starCount");
    }
}
