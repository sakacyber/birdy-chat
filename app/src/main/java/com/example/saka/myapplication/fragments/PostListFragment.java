package com.example.saka.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saka.myapplication.PostDetailActivity;
import com.example.saka.myapplication.R;
import com.example.saka.myapplication.model.Post;
import com.example.saka.myapplication.recyclerview_adaper.RecyclerViewAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

public abstract class PostListFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> adapter;
    private int click = 0;

    public PostListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_post, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.pl_recycler_view);
        recyclerView.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up layout manager, reverse layout
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // Set up FirebaseRecyclerAdapter
        Query postsQuery = getQuery(databaseReference);
        adapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class,
                R.layout.item_card_post, PostViewHolder.class, postsQuery) {

            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new PostViewHolder(view);
            }

            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final Post model, int position) {
                final DatabaseReference postReference = getRef(position);
                // Set click listener for the whole post view
                final String postKey = postReference.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click++;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (click == 2) {
                                    // need to write to both place the post is stored
                                    DatabaseReference globalPostRef = databaseReference.child("posts")
                                            .child(postReference.getKey());
                                    DatabaseReference userPostRef = databaseReference.child("user-posts")
                                            .child(model.getUid()).child(postReference.getKey());
                                    // run two transaction
                                    onStarClicked(globalPostRef);
                                    onStarClicked(userPostRef);
                                }
                                click = 0;
                            }
                        }, 500);
                    }
                });


                // Determine if the current user has like this post and UI accordingly
                if (model.getStars().containsKey(getUid())) {
                    viewHolder.imgStar.setImageResource(R.drawable.ic_heart_filled);
                } else {
                    viewHolder.imgStar.setImageResource(R.drawable.ic_heart_outline);
                }

                // Bind Post to ViewHolder , setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click++;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (click == 1) {
                                    Toast.makeText(getContext(), "heart clicked", Toast.LENGTH_SHORT).show();
                                } else if (click == 2) {
                                    // need to write to both place the post is stored
                                    DatabaseReference globalPostRef = databaseReference.child("posts")
                                            .child(postReference.getKey());
                                    DatabaseReference userPostRef = databaseReference.child("user-posts")
                                            .child(model.getUid()).child(postReference.getKey());
                                    // run two transaction
                                    onStarClicked(globalPostRef);
                                    onStarClicked(userPostRef);
                                }
                                click = 0;
                            }
                        }, 500);

                    }
                });
            }
        };

        RecyclerViewAdapter adapter2 = new RecyclerViewAdapter(
                Post.class,
                R.layout.item_card_post,
                RecyclerViewAdapter.PostViewHolder.class,
                databaseReference.child("posts").limitToFirst(100)
        );

        recyclerView.setAdapter(adapter);
    }

    private void onStarClicked(DatabaseReference postReference) {
        postReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post = mutableData.getValue(Post.class);
                if (post == null) {
                    return Transaction.success(mutableData);
                }
                if (post.getStars().containsKey(getUid())) {
                    // unstar the post and remove self from stars
                    post.setStarCount(post.getStarCount() - 1);
                    post.getStars().remove(getUid());
                } else {
                    // star the post and add self to stars
                    post.setStarCount(post.getStarCount() + 1);
                    post.getStars().put(getUid(), true);
                }
                // set value and report transaction success
                mutableData.setValue(post);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction complete
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    private class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtAuthor;
        private TextView txtBody;
        private ImageView imgStar;
        private TextView txtNumStar;

        private PostViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.card_post_title);
            txtAuthor = (TextView) itemView.findViewById(R.id.card_post_author);
            txtBody = (TextView) itemView.findViewById(R.id.card_post_body);
            imgStar = (ImageView) itemView.findViewById(R.id.card_post_star);
            txtNumStar = (TextView) itemView.findViewById(R.id.card_post_num_star);
        }

        private void bindToPost(Post post, View.OnClickListener clickListener) {
            txtTitle.setText(post.getTitle());
            txtAuthor.setText(post.getAuthor());
            txtBody.setText(post.getBody());
            txtNumStar.setText(String.valueOf(post.getStarCount()));

            imgStar.setOnClickListener(clickListener);
        }
    }

    private class GestureListener implements GestureDetector.OnDoubleTapListener {


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }
}
