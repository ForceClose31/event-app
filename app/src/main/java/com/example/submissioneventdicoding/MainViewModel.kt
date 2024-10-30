package com.example.submissioneventdicoding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submissioneventdicoding.ui.setting.SettingPreferences
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainViewModel(
    private val preferences: SettingPreferences
) : ViewModel() {
    private val _isReminderActive = MutableLiveData<Boolean>()
    val isReminderActive: LiveData<Boolean> = _isReminderActive

    init {
        viewModelScope.launch {
            preferences.getReminderSetting()
                .distinctUntilChanged()
                .collect {
                    _isReminderActive.value = it
                }
        }
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.setThemeSetting(isDarkModeActive)
        }
    }
}