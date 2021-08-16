package com.caitlynwiley.pettracker.viewmodel

import androidx.lifecycle.*
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import kotlinx.coroutines.launch

class PetInfoViewModel(
    val repository: PetTrackerRepository,
    val petId: String // throw an error here, since a pet id passed to this VM should be valid and never return null
): ViewModel() {

    private val _pet = MutableLiveData(Pet())
    val pet: LiveData<Pet> = _pet

    init {
        viewModelScope.launch(block = {
            val p = repository.getPet(petId)
            _pet.postValue(p)
        })
    }

    class Factory(private val repo: PetTrackerRepository, private val petId: String): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PetInfoViewModel(repository = repo, petId = petId) as T
        }

    }
}