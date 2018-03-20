package com.example.saka.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saka.myapplication.model.Post;
import com.example.saka.myapplication.model.myConstant;
import com.example.saka.myapplication.model.myFirebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

public class FragmentNews extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> adapter;
    private String currentUid = myFirebase.getMyUid();
    private int click = 0;

    public FragmentNews() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.news_recycler_view);
        recyclerView.setHasFixedSize(true);

        // Set up layout manager, reverse layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        // Set up FirebaseRecyclerAdapter
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query postsQuery = databaseReference.child(myConstant.POSTS).limitToFirst(100);

        adapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class,
                R.layout.viewholder_post, PostViewHolder.class, postsQuery) {

            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
                return new PostViewHolder(view);
            }

            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final Post model, int position) {
                final DatabaseReference postReference = getRef(position);
                // Set click listener for the whole post view
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
                                    DatabaseReference globalPostRef = databaseReference.child(myConstant.POSTS)
                                            .child(postReference.getKey());
                                    DatabaseReference userPostRef = databaseReference.child(myConstant.USER_POSTS)
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
                if (model.getStars().containsKey(currentUid)) {
                    viewHolder.imgHeart.setImageResource(R.drawable.ic_heart_filled);
                } else {
                    viewHolder.imgHeart.setImageResource(R.drawable.ic_heart_outline);
                }

                viewHolder.bindToPost(model);
            }
        };
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void onStarClicked(DatabaseReference postReference) {
        postReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post = mutableData.getValue(Post.class);
                if (post == null) {
                    return Transaction.success(mutableData);
                }
                if (post.getStars().containsKey(currentUid)) {
                    // unstar the post and remove self from stars
                    post.setStarCount(post.getStarCount() - 1);
                    post.getStars().remove(currentUid);
                } else {
                    // star the post and add self to stars
                    post.setStarCount(post.getStarCount() + 1);
                    post.getStars().put(currentUid, true);
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

    private class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgHeart;
        private TextView txtTitle;
        private TextView txtAuthor;
        private TextView txtBody;
        private TextView txtNumStar;

        private PostViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.card_post_title);
            txtAuthor = (TextView) itemView.findViewById(R.id.card_post_author);
            txtBody = (TextView) itemView.findViewById(R.id.card_post_body);
            imgHeart = (ImageView) itemView.findViewById(R.id.card_post_star);
            txtNumStar = (TextView) itemView.findViewById(R.id.card_post_num_star);
        }

        private void bindToPost(Post post) {
            txtTitle.setText(post.getTitle());
            txtAuthor.setText(post.getAuthor());
            txtBody.setText(post.getBody());
            txtNumStar.setText(String.valueOf(post.getStarCount()));
            //imgHeart.setOnClickListener(clickListener);
        }
    }
}
