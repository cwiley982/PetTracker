package com.caitlynwiley.pettracker.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewPetDetailsScreen() {
    Box(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
    ) {
        PetInfoEditor(ActionButton = {
            Button(
                onClick = { }, // onClick passed separately to PetInfoEditor
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Text("Save", fontSize = 16.sp, color = MaterialTheme.colors.onSecondary)
            }
        }) {
            // TODO: Need way to get Pet info from view model in PetInfoEditor to submit here
            //  Can I get the pet info from the VM by creating a new instance of the PetInfoViewModel?
            //  ie. If in same-ish scope, will a new VM have the same contents?
            submitPetDetails()
        }
    }
}

private fun submitPetDetails() {
    // submit new pet info (well, tell view model to do so)
    // PetInfoViewModel::createPet
}

@Composable
fun PetCreationResults() {
    // display results once they're available

    // when user is done viewing the results, tapping "ok" should navigate to the main app screen
}