package com.example.submissioneventdicoding.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.submissioneventdicoding.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val themePreference: SwitchPreferenceCompat? = findPreference("dark_theme")
        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkTheme = newValue as Boolean
            setTheme(isDarkTheme)
            true
        }
    }

    private fun setTheme(isDark: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.edit().putBoolean("dark_theme", isDark).apply()

        activity?.recreate()
    }
}
