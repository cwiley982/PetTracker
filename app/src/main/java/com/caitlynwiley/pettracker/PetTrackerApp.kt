package com.caitlynwiley.pettracker

import android.app.Application
import com.google.firebase.FirebaseApp

class PetTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}