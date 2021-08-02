package com.caitlynwiley.pettracker.repository

import com.caitlynwiley.pettracker.models.Account
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.models.TrackerItem
import com.caitlynwiley.pettracker.views.screens.PetType
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class PetTrackerRepository @Inject constructor(
//    private val petCache: PetCache
    // uncomment this once it's called somewhere and pass
    // in an api object made via the code within the init block below
//    private val api: FirebaseApi
) {
    private var api: FirebaseApi
    private val defaultPet: Pet = Pet("Nanook", "2016", "9", Pet.Gender.MALE, PetType.DOG, "Husky")
    private val defaultUser: Account = Account("123", "cwiley982@gmail.com")
    private val defaultEvent: TrackerItem = TrackerItem.Builder()
        .setEventType(TrackerItem.EventType.FEED)
        .setCupsFood(2.0)
        .setDate("07/30/2021")
        .setItemType("event")
        .setId("789")
        .setPetId("456")
        .build()
    private val defaultDay: TrackerItem = TrackerItem.Builder()
        .setDate("07/30/2021")
        .setItemType("day")
        .setId("000")
        .setPetId("456")
        .build()
    private var defaultTrackerItems: ArrayList<TrackerItem> = ArrayList(listOf(defaultDay, defaultEvent))

    init {
        val gson = GsonConverterFactory.create(GsonBuilder().setLenient().create())
        val retrofit = Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
            .addConverterFactory(gson)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
        api = retrofit.create(FirebaseApi::class.java)
        defaultPet.id = "456"
        defaultUser.addPet("456")
    }

    suspend fun getPet(id: String): Pet {
        return defaultPet
//        // check cache first
//        val cached: Pet = petCache.getPet(id)
//        if (cached != null) return cached
//
//        if (id.isEmpty()) return Pet()
//
//        return try {
//            // get from api
//            val pet = api.getPet(id)
//            petCache.put(id, pet)
//            pet ?: Pet()
//        } catch (_: KotlinNullPointerException) {
//            Pet()
//        }
    }

    suspend fun getAccount(id: String): Account? {
        return defaultUser
//        return try {
//            api.getUser(id)
//        } catch (_: KotlinNullPointerException) {
//            Account()
//        }

        // check cache first
//        val cached: Account = userCache.getUser(id)
//        if (cached != null) return cached

        // get from api
//        val account = api.getUser(id)
//        userCache.put(id, account)
//        return account
    }

    suspend fun getNumPets(uid: String): Int {
        return defaultUser.pets.size
//        return getPets(uid).size
    }

    suspend fun addItemToTracker(petId: String?, item: TrackerItem?) {
        api.addItemToTracker(petId, item)
    }

    suspend fun getPets(uid: String): List<Pet> {
        return listOf(defaultPet)
//        if (uid.isEmpty()) return emptyList()
//        val map: Map<String, Boolean> = api.getPets(uid) ?: return emptyList()
//        val ids = map.keys
//
//        val pets: List<Pet> = ids.map { id -> getPet(id) }
//        return pets
    }

    suspend fun addPet(id: String?, p: Pet?) {
        api.addPet(id, p)
    }

    suspend fun getEvents(petId: String?): ArrayList<TrackerItem> {
        return defaultTrackerItems
//        return api.getEvents(petId)
    }

    suspend fun addEvent(petId: String, dayId: String, event: TrackerItem) {
        defaultTrackerItems.add(event)
//        api.addEvent(petId, dayId, event)
    }

    suspend fun deleteEventAt(index: Int) {
        defaultTrackerItems.removeAt(index)
    }
}