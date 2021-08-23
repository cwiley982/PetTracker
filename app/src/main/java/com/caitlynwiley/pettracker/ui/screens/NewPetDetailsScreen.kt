package com.caitlynwiley.pettracker.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun NewPetDetailsScreen(goToConfirmDetails: () -> Unit) {
    ConstraintLayout(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
    ) {
        val actionButton = createRef()
        PetInfoEditor()
        Button(onClick = goToConfirmDetails,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            modifier = Modifier.constrainAs(actionButton) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
        {
            Text("Save", fontSize = 16.sp, color = MaterialTheme.colors.onSecondary)
        }
    }
}