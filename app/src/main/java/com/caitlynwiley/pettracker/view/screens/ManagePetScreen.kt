package com.caitlynwiley.pettracker.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ManagePetScreen() {
    var editing by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        PetInfoEditor(
            ActionButton = {
                FloatingActionButton(
                    onClick = {}, // onClick passed separately to PetInfoEditor
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.align(Alignment.BottomEnd)) {
                    SaveEditIcon(editing = editing)
                }
            },
            editing = editing
        ) {
            if (editing) {
                // somehow save info via view model
                // PetInfoViewModel::updatePetInfo
            }
            editing = !editing
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