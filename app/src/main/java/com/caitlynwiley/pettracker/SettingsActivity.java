package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckedTextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final CheckedTextView darkThemePref = findViewById(R.id.dark_theme_setting);
        darkThemePref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckedTextView darkThemeSetting = (CheckedTextView) v;
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putBoolean("dark_theme_enabled", darkThemeSetting.isChecked()).apply();
            }
        });
    }
}
