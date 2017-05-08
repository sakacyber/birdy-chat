package com.example.saka.myapplication.recyclerview_adaper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saka.myapplication.R;
import com.example.saka.myapplication.model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

public class RecyclerViewAdapter extends FirebaseRecyclerAdapter<Post, RecyclerViewAdapter.PostViewHolder> {

    public RecyclerViewAdapter(Class<Post> modelClass, int modelLayout,
                               Class<PostViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
        viewHolder.txtAuthor.setText(model.getAuthor());
        viewHolder.txtTitle.setText(model.getTitle());
        viewHolder.txtBody.setText(model.getBody());
        viewHolder.txtNumStar.setText(String.valueOf(model.getStarCount()));
        //viewHolder.imgStar.setImageResource(R.drawable.ic_star_outline);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtAuthor;
        private TextView txtBody;
        //private ImageView imgStar;
        private TextView txtNumStar;

        public PostViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.card_post_title);
            txtAuthor = (TextView) itemView.findViewById(R.id.card_post_author);
            txtBody = (TextView) itemView.findViewById(R.id.card_post_body);
            //imgStar = (ImageView) itemView.findViewById(R.id.card_post_star);
            txtNumStar = (TextView) itemView.findViewById(R.id.card_post_num_star);
        }
    }

}
