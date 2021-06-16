package com.caitlynwiley.pettracker.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.caitlynwiley.pettracker.viewmodel.PetTrackerViewModel

@Composable
fun PetInfoEntryScreen(viewModel: PetTrackerViewModel, navController: NavController) {
    PetInfoEditor()
}