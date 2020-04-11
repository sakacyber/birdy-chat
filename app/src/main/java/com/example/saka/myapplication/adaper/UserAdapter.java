package com.example.saka.myapplication.adaper;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saka.myapplication.R;
import com.example.saka.myapplication.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

public class UserAdapter extends FirebaseRecyclerAdapter<User, UserAdapter.UserViewHolder> {

    private OnRecyclerItemClick recyclerItemClick;

    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public UserAdapter(Class<User> modelClass, int modelLayout, Class<UserViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
        viewHolder.name.setText(model.getName());
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(mModelLayout, parent, false);
        return new UserViewHolder(rootView);
    }

    public void setOnItemClickListener(OnRecyclerItemClick recyclerItemClick){
        this.recyclerItemClick = recyclerItemClick;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgProfile;
        private TextView name;
        private TextView message;
        private TextView timeStamp;

        private UserViewHolder(View itemView) {
            super(itemView);
            imgProfile = (ImageView) itemView.findViewById(R.id.vh_user_img);
            name = (TextView) itemView.findViewById(R.id.vh_user_name);
            message = (TextView) itemView.findViewById(R.id.vh_user_msg);
            timeStamp = (TextView) itemView.findViewById(R.id.vh_user_time_stamp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (recyclerItemClick != null){
                recyclerItemClick.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnRecyclerItemClick{
        void onItemClick(View v, int position);
    }
}
