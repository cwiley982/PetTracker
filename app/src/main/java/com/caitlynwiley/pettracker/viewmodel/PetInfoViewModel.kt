package com.caitlynwiley.pettracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import kotlinx.coroutines.launch

class PetInfoViewModel constructor(
    repository: PetTrackerRepository,
    petId: String // throw an error here, since a pet id passed to this VM should be valid and never return null
): ViewModel() {

    private val _pet = MutableLiveData(Pet())
    val pet: LiveData<Pet> = _pet

    init {
        viewModelScope.launch(block = {
            val p = repository.getPet(petId)
            _pet.postValue(p)
        })
    }
}