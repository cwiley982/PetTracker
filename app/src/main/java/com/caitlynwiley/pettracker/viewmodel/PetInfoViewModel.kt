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

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _birthMonth = MutableLiveData("")
    val birthMonth: LiveData<String> = _birthMonth

    private val _birthYear = MutableLiveData("")
    val birthYear: LiveData<String> = _birthYear

    private val _age = MutableLiveData("")
    val age: LiveData<String> = _age

    private val _breed = MutableLiveData("")
    val breed: LiveData<String> = _breed

    private val _gender = MutableLiveData(Pet.Gender.UNKNOWN)
    val gender: LiveData<Pet.Gender> = _gender

    init {
        viewModelScope.launch(block = {
            val p = repository.getPet(petId)
            _pet.postValue(p)
            _name.postValue(p.name)
            _birthMonth.postValue(p.birthMonth)
            _birthYear.postValue(p.birthYear)
            _breed.postValue(p.breed)
            _age.postValue(p.age)
            _gender.postValue(p.gender)
        })
    }

    fun setName(name: String) {
        _name.postValue(name)
    }

    fun setBirthMonth(month: String) {
        _birthMonth.postValue(month)
    }

    fun setBirthYear(year: String) {
        _birthYear.postValue(year)
    }

    fun setBreed(breed: String) {
        _breed.postValue(breed)
    }

    fun updateGender(newGender: Pet.Gender) {
        _gender.postValue(newGender)
    }

    class Factory(private val repo: PetTrackerRepository, private val petId: String): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PetInfoViewModel(repository = repo, petId = petId) as T
        }

    }
}