package com.caitlynwiley.pettracker.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.runtime.livedata.observeAsState
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import com.caitlynwiley.pettracker.view.screens.BaseAppScreen
import com.caitlynwiley.pettracker.view.screens.LoginActivity
import com.caitlynwiley.pettracker.viewmodel.AppWideViewModel

open class BaseActivity : ComponentActivity() {
    private val appViewModel by viewModels<AppWideViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val user = appViewModel.user.observeAsState()
            PetTrackerTheme {
                if (user.value == null) {
                    LoginActivity()
                } else {
                    BaseAppScreen()
                }
            }
        }
    }
}