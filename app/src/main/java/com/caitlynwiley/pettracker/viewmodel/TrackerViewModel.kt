package com.caitlynwiley.pettracker.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.models.TrackerItem
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class TrackerViewModel(private val repository: PetTrackerRepository, petId: String) : ViewModel() {
    var gmtTimeZone: TimeZone = TimeZone.getTimeZone("Europe/London")

    private val _trackerItems = MutableLiveData<List<TrackerItem>>()
    val trackerItems: LiveData<List<TrackerItem>> = _trackerItems

    private val _pet = MutableLiveData<Pet>()
    val pet: LiveData<Pet> = _pet

    private val _isRefreshing = MutableStateFlow<Boolean>(false)
    var isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        viewModelScope.launch {
            _pet.postValue(repository.getPet(petId))
            _trackerItems.postValue(repository.getEvents(petId))
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            Log.d("TrackerVM", "Refreshing...")
            Thread.sleep(1000)
            _trackerItems.postValue(repository.getEvents(""))
            _isRefreshing.emit(false)
            Log.d("TrackerVM", "Done refreshing.")
        }
    }

    fun addTrackerItem(item: TrackerItem) {
        viewModelScope.launch {
            repository.addEvent(item.petId, item.itemId, item)
            _trackerItems.postValue(repository.getEvents(""))
        }
    }

    fun removeTrackerItem(index: Int) {
        viewModelScope.launch {
            repository.deleteEventAt(index)
            _trackerItems.postValue(repository.getEvents(""))
        }
    }
}

class TrackerViewModelFactory(val repository: PetTrackerRepository, val petId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackerViewModel(repository, petId) as T
    }
}