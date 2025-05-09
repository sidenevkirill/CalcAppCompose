package ru.lisdevs.calcapp.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ru.lisdevs.calcapp.App

object ThemeUtils {

    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_THEME_MODE = "theme_mode"

    fun setThemeMode(mode: Int) {
        // Сохранение состояния в SharedPreferences
        val sharedPreferences = App.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt(KEY_THEME_MODE, mode)
            apply()
        }

        // Установка режима темы
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun getThemeMode(): Int {
        val sharedPreferences = App.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_NO) // По умолчанию светлая тема
    }
}