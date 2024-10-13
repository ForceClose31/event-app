package com.example.submissioneventdicoding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.submissioneventdicoding.R
import com.example.submissioneventdicoding.ui.home.HomeFragment
import com.example.submissioneventdicoding.ui.event.ActiveEventsFragment
import com.example.submissioneventdicoding.ui.event.CompletedEventsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_active -> selectedFragment = ActiveEventsFragment()
                R.id.navigation_completed -> selectedFragment = CompletedEventsFragment()
            }

            selectedFragment?.let { loadFragment(it) }

            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
