package com.caitlynwiley.pettracker.views.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.caitlynwiley.pettracker.activities.MainActivity
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.repository.FirebaseApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ConfirmPetScreen(id: String, navController: NavController) {
    var petInfo: Pet? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val retrofit = Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
    val api = retrofit.create(FirebaseApi::class.java)
    api?.getPet(id)!!.enqueue(object : Callback<Pet?> {
        override fun onResponse(call: Call<Pet?>, response: Response<Pet?>) {
            petInfo = response.body()
        }

        override fun onFailure(call: Call<Pet?>, t: Throwable) {
            // no-op
        }
    })

    /*
    TODO: Check if ID is valid before leaving ID entry screen, and then pass pet
     info to this Composable so we can assume it's never null here and just have to
     display info, no network calls needed.
     */
    if (petInfo == null) {
        InvalidIdError()
    } else {
        ConfirmationView((petInfo as Pet).name, (petInfo as Pet).primaryOwnerName) {
            // onConfirmation -> create association between pet and this user
            FirebaseDatabase.getInstance().reference.child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("pets")
                .child(id)
                .setValue(true)
//    PreferenceManager.getDefaultSharedPreferences(context).edit()
//        .putBoolean("creating_pet", false).apply()

            // once association is created, take user to main screen
            // navController.navigate("mainActivity")
            context.startActivity(Intent(context, MainActivity::class.java))
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