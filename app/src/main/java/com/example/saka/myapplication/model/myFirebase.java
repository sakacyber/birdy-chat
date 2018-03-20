package com.example.saka.myapplication.model;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by Saka on 04-Jun-17.
 */

public class myFirebase {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private static User user = new User();

    public static String getMyUid(){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            return auth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    public static String getNewPushKey(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        return databaseReference.push().getKey();
    }

    public static void signMeIn(String email, String password){
        auth.signInWithEmailAndPassword(email, password);
    }

    public static void signMeOut(){
        auth.signOut();
    }

    public static Query queryUser(){
        return databaseReference.child(myConstant.USERS).limitToFirst(50);
    }

    public static Query queryFriend(String uid){
        return databaseReference.child(myConstant.USERS).child(uid)
                .child(myConstant.FRIEND).limitToFirst(50);
    }

    public static Query queryRecentPost(){
        return databaseReference.child(myConstant.POSTS).limitToFirst(50);
    }

    public static void updateMyChildren(Map<String, Object> childUpdate){
        databaseReference.updateChildren(childUpdate);
    }

    public static User getUser(String uid){
        databaseReference.child(myConstant.USERS).child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        if (u != null){
                            user = u;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("birdy", "can not get user: "+databaseError.getDetails());
                    }
                }
        );
        return user;
    }

    public static void writeNewUser(String uid, User userValue){
        databaseReference.child(myConstant.USERS).child(uid).setValue(userValue);
    }
}
