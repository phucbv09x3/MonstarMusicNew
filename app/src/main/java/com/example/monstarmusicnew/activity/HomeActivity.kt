package com.example.monstarmusicnew.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.example.monstarmusicnew.fragment.OfflineFragment
import com.example.monstarmusicnew.R
import com.example.monstarmusicnew.fragment.OnlineFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private var mOfflineFragment: OfflineFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        navigationView.setNavigationItemSelectedListener(this)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_Frame, OfflineFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
            when (p0.itemId) {
                R.id.list_music_of_nav -> {
                    drawer_layout.closeDrawers()
                    mOfflineFragment = OfflineFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.content_Frame, OfflineFragment())
                        .commit()
                }
                R.id.home_music_online_of_nav -> {
                    drawer_layout.closeDrawers()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.content_Frame, OnlineFragment())
                        .commit()
                }
            }
            return true
    }
}