package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.theme.PetTrackerTheme

open class BaseActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("logged_in", false)) {
            setContent {
                PetTrackerTheme {
                    MainActivity()
                }
            }
        } else {
            if (PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("creating_pet", false)) {
                startActivity(Intent(this@BaseActivity, NewPetActivity::class.java))
            } else {
//                setContent {
//                    LoginActivity()
//                }
                startActivity(Intent(this@BaseActivity, LoginActivity::class.java))
            }
        }
    }
}