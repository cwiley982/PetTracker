package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

open class BaseActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
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
                setContent {
                    Surface {
                        PetTrackerTheme {
                            NewPetScreen {
                                Firebase.auth.signOut()
                                startActivity(Intent(this@BaseActivity, LoginActivity::class.java))
                            }
                        }
                    }
                }
            } else {
//                setContent {
//                    LoginActivity()
//                }
                startActivity(Intent(this@BaseActivity, LoginActivity::class.java))
            }
        }
    }
}