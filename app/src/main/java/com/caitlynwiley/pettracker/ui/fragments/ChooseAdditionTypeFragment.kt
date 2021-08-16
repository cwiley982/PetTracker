package com.caitlynwiley.pettracker.ui.fragments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ChooseAdditionTypeScreen(addById: () -> Unit, createNewPet: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Button(onClick = createNewPet ) {
            Text("Create new pet")
        }

        Text("OR")

        Button(onClick = addById ) {
            Text("Enter a pet ID")
        }
    }
}