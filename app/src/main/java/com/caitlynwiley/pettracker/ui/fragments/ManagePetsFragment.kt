package com.caitlynwiley.pettracker.ui.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.ui.components.PetInfoEditor

@Composable
fun ManagePetsFragment() {
    var editing by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        PetInfoEditor(editing)

        val actionButton = createRef()
        FloatingActionButton(
            onClick = {
                editing = !editing
            },
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
            modifier = Modifier.constrainAs(actionButton) {
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit_black_24dp),
                contentDescription = "edit button",
                tint = MaterialTheme.colors.onSecondary,
                modifier = Modifier.background(
                    MaterialTheme.colors.secondary,
                    CircleShape
                )
            )
        }
    }
}