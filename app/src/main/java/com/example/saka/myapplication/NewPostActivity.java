package com.example.saka.myapplication;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saka.myapplication.model.Post;
import com.example.saka.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends BaseActivity {

    private DatabaseReference databaseReference;
    private EditText edtTitle;
    private EditText edtBody;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        edtTitle = (EditText) findViewById(R.id.np_edt_title);
        edtBody = (EditText) findViewById(R.id.np_edt_body);
        fab = (FloatingActionButton) findViewById(R.id.np_fab);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost(){
        final String title = edtTitle.getText().toString();
        final String body = edtBody.getText().toString();
        if (TextUtils.isEmpty(title)){
            edtTitle.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(body)){
            edtBody.setError("Required");
            return;
        }
        // Disable button so there are no multi-post
        setEditingEnabled(false);
        Toast.makeText(getApplicationContext(), "Posting...", Toast.LENGTH_SHORT).show();

        // Start single_value_read
        final String userId = getUid();
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null){
                            // User is null, error out
                            Toast.makeText(getApplicationContext(), "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, user.getName(), title, body);
                            Toast.makeText(getApplicationContext(),userId+"-"
                            +user.getName()+"-"+title+"-"+body, Toast.LENGTH_SHORT).show();
                        }
                        // Finish this activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),
                                "getUser:onCancel: "+databaseError.toException(),
                                Toast.LENGTH_SHORT).show();
                        setEditingEnabled(true);
                    }
                }
        );
    // End single_value_read
    }

    private void writeNewPost(String userId, String username, String title, String body){
        // Create new post at /user-posts/$userId/$postId and at
        // /posts/$postId simultaneously
        String key = databaseReference.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/"+key, postValues);
        childUpdates.put("/user-posts/"+userId+"/"+key, postValues);
        databaseReference.updateChildren(childUpdates);
    }

    private void setEditingEnabled(boolean enabled){
        edtTitle.setEnabled(enabled);
        edtBody.setEnabled(enabled);
        if (enabled){
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

}
