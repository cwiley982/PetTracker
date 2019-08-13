package com.caitlynwiley.pettracker.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caitlynwiley.pettracker.R;

public class BaseActivity extends AppCompatActivity {

    private boolean useDarkTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        useDarkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme_enabled", false);
        setTheme(useDarkTheme ? R.style.DarkTheme : R.style.LightTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        boolean darkThemePref = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme_enabled", false);
        setTheme(useDarkTheme ? R.style.DarkTheme : R.style.LightTheme);
        if (useDarkTheme != darkThemePref) {
            useDarkTheme = darkThemePref;
            this.recreate();
        }
        super.onResume();
    }
}
