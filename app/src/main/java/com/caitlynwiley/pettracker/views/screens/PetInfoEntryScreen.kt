package com.caitlynwiley.pettracker.views.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.caitlynwiley.pettracker.viewmodel.PetInfoViewModel

@Composable
fun PetInfoEntryScreen(viewModel: PetInfoViewModel, navController: NavController) {
    PetInfoEditor(viewModel)
}

/*
val petId = ref.child("pets").push().key
pet.id = petId ?: ""
AddPetTask().execute(pet)
ref.child("users").child(mUid).child("pets").child(petId!!).setValue(true)
 */