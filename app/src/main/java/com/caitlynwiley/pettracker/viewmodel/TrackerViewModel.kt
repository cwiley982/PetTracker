package com.caitlynwiley.pettracker.viewmodel

import androidx.lifecycle.*
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.models.TrackerItem
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import kotlinx.coroutines.launch
import java.util.*

class TrackerViewModel(private val repository: PetTrackerRepository, petId: String) : ViewModel() {
    var gmtTimeZone: TimeZone = TimeZone.getTimeZone("Europe/London")

    private val _trackerItems = MutableLiveData<List<TrackerItem>>()
    val trackerItems: LiveData<List<TrackerItem>> = _trackerItems

    private val _pet = MutableLiveData<Pet>()
    val pet: LiveData<Pet> = _pet

    private val _emptyLabelVisible = MutableLiveData<Boolean>()
    val emptyLabelVisible: LiveData<Boolean> = _emptyLabelVisible

    init {
        viewModelScope.launch {
            _pet.postValue(repository.getPet(petId))
            _trackerItems.postValue(repository.getEvents(petId))
            _emptyLabelVisible.postValue(trackerItems.value?.size ?: 0 == 0)
        }
    }

    fun forceRefreshEvents() {
        viewModelScope.launch {
            _trackerItems.postValue(repository.getEvents(""))
            _emptyLabelVisible.postValue(trackerItems.value?.size ?: 0 == 0)
        }
    }

    fun addTrackerItem(item: TrackerItem) {
        viewModelScope.launch {
            repository.addEvent(item.petId, item.itemId, item)
            _trackerItems.postValue(repository.getEvents(""))
            _emptyLabelVisible.postValue(trackerItems.value?.size ?: 0 == 0)
        }
    }

    fun removeTrackerItem(index: Int) {
        viewModelScope.launch {
            repository.deleteEventAt(index)
            _trackerItems.postValue(repository.getEvents(""))
            _emptyLabelVisible.postValue(trackerItems.value?.size ?: 0 == 0)
        }
    }
}

class TrackerViewModelFactory(val repository: PetTrackerRepository, val petId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackerViewModel(repository, petId) as T
    }
}