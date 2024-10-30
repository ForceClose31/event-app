//package com.example.submissioneventdicoding
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.submissioneventdicoding.ui.home.HomeFragment
//import com.example.submissioneventdicoding.ui.event.ActiveEventsFragment
//import com.example.submissioneventdicoding.ui.event.CompletedEventsFragment
//import com.example.submissioneventdicoding.ui.favorite.FavoriteEventsFragment
//import com.example.submissioneventdicoding.ui.setting.MainViewModel
//import com.example.submissioneventdicoding.ui.setting.SettingPreferences
//import com.example.submissioneventdicoding.ui.setting.SettingsFragment
//import com.example.submissioneventdicoding.ui.setting.ViewModelFactory
//import com.example.submissioneventdicoding.ui.setting.dataStore
//import com.google.android.material.bottomnavigation.BottomNavigationView
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var mainViewModel: MainViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val pref = SettingPreferences.getInstance(application.dataStore)
//        mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(MainViewModel::class.java)
//
//        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
//            if (isDarkModeActive) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//        }
//
//        setContentView(R.layout.activity_main)
//
//        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
//
//        if (savedInstanceState == null) {
//            loadFragment(HomeFragment())
//        }
//
//        bottomNavigationView.setOnItemSelectedListener { item ->
//            var selectedFragment: Fragment? = null
//
//            when (item.itemId) {
//                R.id.navigation_home -> selectedFragment = HomeFragment()
//                R.id.navigation_active -> selectedFragment = ActiveEventsFragment()
//                R.id.navigation_completed -> selectedFragment = CompletedEventsFragment()
//                R.id.navigation_favorite -> selectedFragment = FavoriteEventsFragment()
//                R.id.navigation_settings -> selectedFragment = SettingsFragment()
//            }
//
//            selectedFragment?.let { loadFragment(it) }
//
//            true
//        }
//    }
//
//    private fun setAppTheme(isDarkTheme: Boolean) {
//        setTheme(if (isDarkTheme) R.style.Theme_SubmissionEventDicodingDark else R.style.Theme_SubmissionEventDicoding)
//    }
//
//    private fun loadFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .commit()
//    }
//}

package com.example.submissioneventdicoding

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.submissioneventdicoding.R
import com.example.submissioneventdicoding.databinding.ActivityMainBinding
import com.example.submissioneventdicoding.ui.setting.MainViewModel
import com.example.submissioneventdicoding.ui.setting.SettingPreferences
import com.example.submissioneventdicoding.ui.setting.ViewModelFactory
import com.example.submissioneventdicoding.ui.setting.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(
            applicationContext,
            SettingPreferences.getInstance(applicationContext.dataStore)
        )
    }
    private lateinit var navController: NavController

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(
                    this,
                    "Permission is required to show notifications",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = binding.bottomNavigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_active,
                R.id.navigation_completed,
                R.id.navigation_favorite,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) else setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if(navController.currentDestination?.id == R.id.eventDetailFragment) {
            navController.popBackStack(R.id.navigation_home, false)
        } else {
            super.onBackPressed()
        }
    }
}
