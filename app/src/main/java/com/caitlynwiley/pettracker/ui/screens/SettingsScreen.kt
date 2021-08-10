package com.caitlynwiley.pettracker.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.activities.BaseActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    Column {
        Row {
            Text("Use Dark Theme")
            Switch(checked = false, onCheckedChange = {})
        }

        Row(modifier = Modifier.clickable {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit().remove("logged_in").apply()
            Firebase.auth.signOut()
            context.startActivity(Intent(context, BaseActivity::class.java))
        }) {
            Text("Sign Out")
        }
    }
}