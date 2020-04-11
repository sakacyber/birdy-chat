package com.example.saka.myapplication.adaper;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saka.myapplication.R;
import com.example.saka.myapplication.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by Saka on 04-Jun-17.
 */

public class AddFriendAdapter extends FirebaseRecyclerAdapter<User, AddFriendAdapter.AddFriendViewHolder> {

    private OnAddFriendItemClick recyclerItemClick;

    public AddFriendAdapter(Class<User> modelClass, int modelLayout, Class<AddFriendViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public void setOnItemClickListener(OnAddFriendItemClick recyclerItemClick){
        this.recyclerItemClick = recyclerItemClick;
    }

    @Override
    public AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
        return new AddFriendViewHolder(view);
    }

    @Override
    protected void populateViewHolder(AddFriendViewHolder viewHolder, User model, int position) {
        viewHolder.name.setText(model.getName());
    }

    public class AddFriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProfile;
        private TextView name;

        private AddFriendViewHolder(View itemView) {
            super(itemView);
            imgProfile = (ImageView) itemView.findViewById(R.id.vh_add_friend_img);
            name = (TextView) itemView.findViewById(R.id.vh_add_friend_txt_name);
        }
    }

    public interface OnAddFriendItemClick{
        void onItemClick(View v, int position);
    }
}
