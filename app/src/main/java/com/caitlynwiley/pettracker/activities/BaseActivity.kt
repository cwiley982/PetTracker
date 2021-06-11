package com.caitlynwiley.pettracker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.R

open class BaseActivity : AppCompatActivity() {
    private var mUseDarkTheme : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        mUseDarkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme_enabled", false)
        setTheme(if (mUseDarkTheme) R.style.DarkTheme else R.style.LightTheme)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        val darkThemePref = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme_enabled", false)
        if (mUseDarkTheme != darkThemePref) {
            mUseDarkTheme = darkThemePref
            setTheme(if (mUseDarkTheme) R.style.DarkTheme else R.style.LightTheme)
            this.recreate()
        }
        super.onResume()
    }
}