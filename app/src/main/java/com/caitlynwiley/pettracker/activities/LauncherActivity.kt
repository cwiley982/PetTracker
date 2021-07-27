package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceManager

class LauncherActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("logged_in", false)) {
            startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
        } else {
            if (PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("creating_pet", false)) {
                startActivity(Intent(this@LauncherActivity, NewPetActivity::class.java))
            } else {
                startActivity(Intent(this@LauncherActivity, LoginActivity::class.java))
            }
        }
    }
}