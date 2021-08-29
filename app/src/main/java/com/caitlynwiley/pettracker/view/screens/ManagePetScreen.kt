package com.caitlynwiley.pettracker.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ManagePetScreen() {
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
            SaveEditIcon(editing = editing)
        }
    }
}

@Composable
private fun SaveEditIcon(editing: Boolean) {
    if (editing) {
        Icon(
            imageVector = Icons.Outlined.Save,
            contentDescription = "save icon",
            tint = MaterialTheme.colors.onSecondary,
            modifier = Modifier.background(
                MaterialTheme.colors.secondary,
                CircleShape
            )
        )
    } else {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "edit button",
            tint = MaterialTheme.colors.onSecondary,
            modifier = Modifier.background(
                MaterialTheme.colors.secondary,
                CircleShape
            )
        )
    }
}