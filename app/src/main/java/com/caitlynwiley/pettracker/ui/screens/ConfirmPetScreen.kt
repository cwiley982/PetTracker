package com.caitlynwiley.pettracker.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.activities.BaseActivity
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking

@Composable
fun ConfirmPetScreen(id: String, navController: NavController) {
    var petInfo: Pet? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val repo = PetTrackerRepository()
    runBlocking {
        petInfo = repo.getPet(id)
    }

    /*
    TODO: Check if ID is valid before leaving ID entry screen, and then pass pet
     info to this Composable so we can assume it's never null here and just have to
     display info, no network calls needed.
     */
    if (petInfo == null) {
        InvalidIdError()
    } else {
        ConfirmationView(petInfo?.name ?: "", petInfo?.primaryOwnerName ?: "") {
            // onConfirmation -> create association between pet and this user
            FirebaseDatabase.getInstance().reference.child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("pets")
                .child(id)
                .setValue(true)

            PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean("creating_pet", false).apply()

            // once association is created, take user to main screen
            // navController.navigate("mainActivity")
            context.startActivity(Intent(context, BaseActivity::class.java))
        }
    }
}

@Composable
fun InvalidIdError() {
    Text("Hmm, looks like that ID is invalid. Please go back and make sure the ID you entered is correct and try again.")
}

@Composable
fun ConfirmationView(petName: String, ownerName: String, onConfirm: () -> Unit) {
    Column {
        Text("Please confirm that the information below matches the pet you're trying to add.")

        Text("Pet's name: $petName")
        Text("Owner's name: $ownerName")

        Button(onClick = onConfirm) {
            Text("Add")
        }
    }
}