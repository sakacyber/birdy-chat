package com.example.saka.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saka.myapplication.adaper.UserAdapter;
import com.example.saka.myapplication.model.User;
import com.example.saka.myapplication.model.myFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentChat extends Fragment {

    private UserAdapter adapter;

    public FragmentChat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new UserAdapter(User.class, R.layout.viewholder_user_chat,
                UserAdapter.UserViewHolder.class,
                myFirebase.queryFriend(myFirebase.getMyUid()));
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.setOnItemClickListener(new UserAdapter.OnRecyclerItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // go to chat place
                    startActivity(new Intent(getActivity(), ChatActivity.class)
                            .putExtra("chatWith", adapter.getRef(position).getKey()));
                }
            }
        });
    }
}
