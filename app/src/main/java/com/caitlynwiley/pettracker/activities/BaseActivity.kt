package com.caitlynwiley.pettracker.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import com.caitlynwiley.pettracker.viewmodel.AppWideViewModel

open class BaseActivity : ComponentActivity() {
    private val appViewModel by viewModels<AppWideViewModel>()

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val user = appViewModel.user.observeAsState()
            PetTrackerTheme {
                if (user.value == null) {
                    LoginActivity()
                } else {
                    MainActivity()
                }
            }
        }
    }
}