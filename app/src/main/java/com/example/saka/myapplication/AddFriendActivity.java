package com.example.saka.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saka.myapplication.model.myConstant;
import com.example.saka.myapplication.model.User;
import com.example.saka.myapplication.model.myFirebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddFriendActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    String myUid = myFirebase.getMyUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.af_layout);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.af_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseRecyclerAdapter<User, FriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, FriendViewHolder>(User.class,
                        R.layout.viewholder_add_friend, FriendViewHolder.class, myFirebase.queryUser()) {

                    @Override
                    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                        return new FriendViewHolder(view);
                    }

                    @Override
                    protected void populateViewHolder(final FriendViewHolder viewHolder,
                                                      final User model, final int position) {
                        final DatabaseReference userRef = getRef(position);

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //// TODO: 05-Jun-17 go to profile activity
                                DatabaseReference yourRef = databaseReference.child(myConstant.USERS)
                                        .child(userRef.getKey());
                                DatabaseReference myRef = databaseReference.child(myConstant.USERS)
                                        .child(myUid);
                                if (viewHolder.request.getText().equals("requested")) {
                                    checkRequestU(yourRef);
                                    checkRequestMe(myRef, yourRef);
                                } else if (viewHolder.request.getText().equals("Click to accept")) {
                                    accept(myRef, yourRef);
                                } else if (viewHolder.request.getText().equals("Click to send request")) {
                                    checkRequestU(yourRef);
                                    checkRequestMe(myRef, yourRef);
                                }
                            }
                        });

                        checkUser(viewHolder, model);
                        viewHolder.bindToFriend(model);

                    }
                };
        recyclerView.setAdapter(adapter);
    }

    private void checkUser(FriendViewHolder viewHolder, User model) {
        // check if you
        if (model.getUid().contentEquals(myUid)) {
            viewHolder.name.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.request.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.request.setText("You");
        }

        // check if you already request
        if (model.getRequestFrom().containsKey(myUid)) {
            viewHolder.request.setText("requested");
        }

        // check if they request you
        if (model.getRequestTo().containsKey(myUid)) {
            viewHolder.request.setText("Click to accept");
        }

        // check if friend
        if (model.getFriend().containsKey(myUid)) {
            viewHolder.request.setText("Friend");
            viewHolder.name.setTextColor(getResources().getColor(R.color.colorPrimary));
            viewHolder.request.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void checkRequestU(DatabaseReference yourRef) {
        yourRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                if (user == null) {
                    return Transaction.success(mutableData);
                }
                if (user.getRequestFrom().containsKey(myUid)) {
                    // cancel request and remove request from user
                    user.setRequestFromCount(user.getRequestFromCount() - 1);
                    user.getRequestFrom().remove(myUid);
                } else {
                    // send request to user and add request
                    user.setRequestFromCount(user.getRequestFromCount() + 1);
                    user.getRequestFrom().put(myUid, true);
                }
                // set value and report transaction success
                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    private void checkRequestMe(DatabaseReference myRef, final DatabaseReference yourRef) {
        myRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                if (user == null) {
                    return Transaction.success(mutableData);
                }
                if (user.getRequestTo().containsKey(yourRef.getKey())) {
                    // cancel request and remove request from user
                    user.setRequestToCount(user.getRequestToCount() - 1);
                    user.getRequestTo().remove(yourRef.getKey());
                } else {
                    // send request to user and add request
                    user.setRequestToCount(user.getRequestToCount() + 1);
                    user.getRequestTo().put(yourRef.getKey(), true);
                }
                // set value and report transaction success
                mutableData.setValue(user);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    private void accept(final DatabaseReference myRef, final DatabaseReference yourRef) {
        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Map<String, Object> friend = new HashMap<>();

                        if (user.getRequestFrom().containsKey(yourRef.getKey())) {
                            user.setRequestFromCount(user.getRequestFromCount() - 1);
                            user.getRequestFrom().remove(yourRef.getKey());
                        }
                        myRef.setValue(user);
                        friend.put("name", myFirebase.getUser(yourRef.getKey()).getName());

                        databaseReference.child(myConstant.USERS).child(myUid).child("friend")
                                .child(yourRef.getKey()).setValue(friend);

                        databaseReference.child(myConstant.USERS).child(myUid).child("zChat")
                                .child(yourRef.getKey()).setValue("");

                        Log.d("onAccept:onDataChange", "update myRef success");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("onAccept", databaseError.getDetails());
                    }
                }
        );
        yourRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Map<String, Object> friend = new HashMap<>();
                        if (user.getRequestTo().containsKey(myRef.getKey())) {
                            user.setRequestToCount(user.getRequestToCount() - 1);
                            user.getRequestTo().remove(myRef.getKey());
                        }
                        yourRef.setValue(user);
                        friend.put("name", myFirebase.getUser(myUid).getName());
                        databaseReference.child(myConstant.USERS).child(yourRef.getKey()).child("friend")
                                .child(myUid).setValue(friend);

                        databaseReference.child(myConstant.USERS).child(yourRef.getKey()).child("zChat")
                                .child(myUid).setValue("");

                        Log.d("onAccept:onDataChange", "update yourRef success");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("onAccept", databaseError.getDetails());
                    }
                }
        );
        Log.d("onAccept", "accept success");
    }

    private class FriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProfile;
        private TextView name;
        private TextView request;

        private FriendViewHolder(View itemView) {
            super(itemView);
            imgProfile = (ImageView) itemView.findViewById(R.id.vh_add_friend_img);
            name = (TextView) itemView.findViewById(R.id.vh_add_friend_txt_name);
            request = (TextView) itemView.findViewById(R.id.vh_add_friend_txt_request);
        }

        private void bindToFriend(User user) {
            name.setText(user.getName());
        }
    }
}
