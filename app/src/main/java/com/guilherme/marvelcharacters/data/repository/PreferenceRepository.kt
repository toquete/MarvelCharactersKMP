package com.guilherme.marvelcharacters.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private const val PREFERENCE_NIGHT_MODE = "PREFERENCE_NIGHT_MODE"
private const val PREFERENCE_NIGHT_MODE_DEFAULT = AppCompatDelegate.MODE_NIGHT_NO

class PreferenceRepository(private val sharedPreferences: SharedPreferences) {

    val nightMode
        get() = sharedPreferences.getInt(PREFERENCE_NIGHT_MODE, PREFERENCE_NIGHT_MODE_DEFAULT)

    var isDarkTheme: Boolean = false
        get() = nightMode == AppCompatDelegate.MODE_NIGHT_YES
        set(value) {
            sharedPreferences.edit().putInt(
                PREFERENCE_NIGHT_MODE,
                if (value) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            ).apply()
            field = value
        }

    private val _nightModeLive: MutableLiveData<Int> = MutableLiveData()
    val nightModeLive: LiveData<Int>
        get() = _nightModeLive

    private val _isDarkThemeLive: MutableLiveData<Boolean> = MutableLiveData()
    val isDarkThemeLive: LiveData<Boolean>
        get() = _isDarkThemeLive

    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                PREFERENCE_NIGHT_MODE -> {
                    _nightModeLive.value = nightMode
                    _isDarkThemeLive.value = isDarkTheme
                }
            }
        }

    init {
        // Init preference LiveData objects.
        _nightModeLive.value = nightMode
        _isDarkThemeLive.value = isDarkTheme

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }
}