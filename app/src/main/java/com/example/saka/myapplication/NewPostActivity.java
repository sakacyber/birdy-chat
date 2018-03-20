package com.example.saka.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saka.myapplication.model.Post;
import com.example.saka.myapplication.model.myFirebase;

import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
                finish();
            }
        });
    }

    private void submitPost(){
        if (validatePost()) {
            // Disable button so there are no multi-post
            setEditingEnabled(false);
            Toast.makeText(getApplicationContext(), "Posting...", Toast.LENGTH_SHORT).show();

            // Start write new post
            final String uid = myFirebase.getMyUid();
            final String username = myFirebase.getUser(myFirebase.getMyUid()).getName();
            final String title = edtTitle.getText().toString();
            final String body = edtBody.getText().toString();
            writeNewPost(uid, username, title, body);
        }
    }

    private void writeNewPost(String uid, String author, String title, String body) {
        // Create new post at /user-posts/$userId/$postId and at
        // /posts/$postId simultaneously
        String key = myFirebase.getNewPushKey();
        Post post = new Post(uid, author, title, body);
        Map<String, Object> newPost = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/POSTS/" + key, newPost);
        childUpdates.put("/USER-POSTS/" + uid + "/" + key, newPost);
        myFirebase.updateMyChildren(childUpdates);
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

    private boolean validatePost() {
        final String title = edtTitle.getText().toString();
        final String body = edtBody.getText().toString();
        if (TextUtils.isEmpty(title)) {
            edtTitle.setError("Required");
            return false;
        }
        if (TextUtils.isEmpty(body)) {
            edtBody.setError("Required");
            return false;
        }
        return true;
    }

}
