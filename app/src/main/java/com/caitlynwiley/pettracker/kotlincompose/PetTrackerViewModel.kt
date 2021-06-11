package com.caitlynwiley.pettracker.kotlincompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.caitlynwiley.pettracker.models.Pet

class PetTrackerViewModel : ViewModel() {

    private var currentPetIndex by mutableStateOf(-1)

    var petList: List<Pet> by mutableStateOf(listOf())

    val numPets: Int
        get() = petList.size

    val currentPet: Pet?
        get() = petList.getOrNull(currentPetIndex)

    fun onPetSelected(pet: Pet) {
        currentPetIndex = petList.indexOf(pet)
    }

    fun onPetEdited(pet: Pet) {
        val current = requireNotNull(currentPet)
        require (current.id == pet.id) {
            // no no, what doing?
        }

        petList = petList.toMutableList().also{
            it[currentPetIndex] = pet
        }
    }
}