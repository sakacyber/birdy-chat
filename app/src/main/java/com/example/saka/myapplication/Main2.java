package com.example.saka.myapplication;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.saka.myapplication.fragments.MyPostsFragment;
import com.example.saka.myapplication.fragments.MyTopPostsFragment;
import com.example.saka.myapplication.fragments.PostListFragment;
import com.example.saka.myapplication.fragments.RecentPostsFragment;
import com.example.saka.myapplication.model.Post;
import com.example.saka.myapplication.recyclerview_adaper.RecyclerViewAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2 extends BaseActivity {

    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);

        // Create the adapter that will return a fragment for each section
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] fragments = new Fragment[]{
                    new RecentPostsFragment(), new MyPostsFragment(), new MyTopPostsFragment(),
            };
            private final String[] fragmentNames = new String[]{
                    "RECENT POST", "MY POST", "PROFILE"
            };

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentNames[position];
            }
        };

        // Set up the viewpager with the selection adapter
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        findViewById(R.id.main_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewPostActivity.class));
            }
        });

//
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(
//                Post.class,
//                R.layout.item_card_post,
//                RecyclerViewAdapter.PostViewHolder.class,
//                databaseReference.child("posts"));
//        recyclerView.setAdapter(adapter);
    }
}

