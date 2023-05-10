package com.example.saka.myapplication.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import com.example.saka.myapplication.FragmentChat
import com.example.saka.myapplication.FragmentNews
import com.example.saka.myapplication.FragmentProfile
import com.example.saka.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var bottomNavigation: BottomNavigationView? = null
    private val itemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, FragmentProfile())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_news -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, FragmentNews())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_chat -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, FragmentChat())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById<View>(R.id.main_bottom_navigation) as BottomNavigationView
        bottomNavigation!!.setOnNavigationItemSelectedListener(itemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onStart() {
        super.onStart()
        bottomNavigation!!.selectedItemId = R.id.nav_news
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return true
    }
}
