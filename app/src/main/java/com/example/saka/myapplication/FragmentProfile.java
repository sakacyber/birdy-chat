package com.example.saka.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.saka.myapplication.adaper.OptionAdapter;
import com.example.saka.myapplication.model.myFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentProfile extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private String[] options = {"New post", "Friend request", "Friends", "Settings", "About", "Log out"};
    private int[] icons = {R.drawable.ic_new_post, R.drawable.ic_add_circle, R.drawable.ic_friend,
            R.drawable.ic_settings, R.drawable.ic_about, R.drawable.ic_log_out};

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        RelativeLayout profileLayout = (RelativeLayout) rootView.findViewById(R.id.pf_bar);
        ListView listView = (ListView) rootView.findViewById(R.id.pf_list_view);
        TextView myName = (TextView) rootView.findViewById(R.id.pf_txt_name);
        TextView email = (TextView) rootView.findViewById(R.id.pf_txt_email);
        profileLayout.setOnClickListener(this);
        myName.setText(myFirebase.getUser(myFirebase.getMyUid()).getName());
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        OptionAdapter adapter = new OptionAdapter(getContext(), options, icons);
        listView.setDivider(null);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pf_bar) {
            //// TODO: 05-Jun-17 create activity profile
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(), NewPostActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
                break;
            case 2:
                //startActivity(new Intent(getActivity(), FriendListActivity.class));
                break;
            case 5:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LogInActivity.class));
                getActivity().finish();
                break;
        }
    }
}   ///////////////////////////////////////////////////////////////////////////////////////////////
