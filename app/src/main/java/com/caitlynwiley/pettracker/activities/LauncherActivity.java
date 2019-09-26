package com.caitlynwiley.pettracker.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("logged_in", false)) {
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        } else {
            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("creating_pet", false)) {
                startActivity(new Intent(LauncherActivity.this, AddPetActivity.class));
            } else {
                startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
            }
        }
    }
}
