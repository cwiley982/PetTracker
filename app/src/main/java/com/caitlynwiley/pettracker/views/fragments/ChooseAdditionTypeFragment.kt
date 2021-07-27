package com.caitlynwiley.pettracker.views.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.caitlynwiley.pettracker.activities.Screen

@Composable
fun ChooseAdditionTypeScreen(navController: NavController) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Button(onClick = {navController.navigate(Screen.SelectSpecies.route)} ) {
            Text("Create new pet")
        }

        Text("OR")

        Button(onClick = {navController.navigate(Screen.AddPetById.route)} ) {
            Text("Enter a pet ID")
        }
    }
}