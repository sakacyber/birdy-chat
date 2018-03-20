package com.example.saka.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private BottomNavigationView bottomNavigation;
    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_profile:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_container, new FragmentProfile())
                                    .commit();
                            return true;
                        case R.id.nav_news:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_container, new FragmentNews())
                                    .commit();
                            return true;
                        case R.id.nav_chat:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_container, new FragmentChat())
                                    .commit();
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(itemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigation.setSelectedItemId(R.id.nav_news);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}
