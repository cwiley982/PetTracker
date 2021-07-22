package com.caitlynwiley.pettracker.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewModelScope
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import kotlinx.coroutines.launch

class PetTrackerViewModel(
    petTrackerRepository: PetTrackerRepository
): ViewModel() {

    private var currentPetIndex by mutableStateOf(0)

    private val _petList = MutableLiveData<List<Pet>>()
    val petList: LiveData<List<Pet>> = _petList

    val numPets: Int
        get() = petList.value?.size ?: 0

    val currentPet: Pet?
        get() = petList.value?.getOrNull(currentPetIndex)

    init {
        viewModelScope.launch(block = {
            // if (uid != null) { // get pet list with uid
            _petList.postValue(petTrackerRepository.getPets(""))
        })
    }

    fun onPetSelected(pet: Pet) {
        currentPetIndex = petList.value?.indexOf(pet) ?: -1
    }

    fun onPetEdited(pet: Pet) {
        val current = requireNotNull(currentPet)
        require (current.id == pet.id) {
            // no no, what doing?
        }

        _petList.postValue(
            petList.value?.toMutableList().also{
                it?.set(currentPetIndex, pet)
            }
        )
    }
}

class PTViewModelFactory(val repository: PetTrackerRepository) : Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PetTrackerViewModel(repository) as T
    }
}