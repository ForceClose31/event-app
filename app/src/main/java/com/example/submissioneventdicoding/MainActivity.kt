package com.example.submissioneventdicoding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.submissioneventdicoding.ui.home.HomeFragment
import com.example.submissioneventdicoding.ui.event.ActiveEventsFragment
import com.example.submissioneventdicoding.ui.event.CompletedEventsFragment
import com.example.submissioneventdicoding.ui.favorite.FavoriteEventsFragment
import com.example.submissioneventdicoding.ui.setting.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkTheme = sharedPreferences.getBoolean("dark_theme", false)
        setTheme(if (isDarkTheme) R.style.Theme_SubmissionEventDicodingDark else R.style.Theme_SubmissionEventDicoding)
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
                R.id.navigation_favorite -> selectedFragment = FavoriteEventsFragment()
                R.id.navigation_settings -> selectedFragment = SettingsFragment()
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
