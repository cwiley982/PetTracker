package com.caitlynwiley.pettracker.repository

import com.caitlynwiley.pettracker.models.Account
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.models.TrackerItem
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

    init {
        val gson = GsonConverterFactory.create(GsonBuilder().setLenient().create())
        val retrofit = Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
            .addConverterFactory(gson)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
        api = retrofit.create(FirebaseApi::class.java)
    }

    suspend fun getPet(id: String): Pet {
        // check cache first
//        val cached: Pet = petCache.getPet(id)
//        if (cached != null) return cached

        if (id.isEmpty()) return Pet()

        return try {
            // get from api
            val pet = api.getPet(id)
//            petCache.put(id, pet)
            pet ?: Pet()
        } catch (_: KotlinNullPointerException) {
            Pet()
        }
    }

    suspend fun getAccount(id: String): Account? {
        return try {
            api.getUser(id)
        } catch (_: KotlinNullPointerException) {
            Account()
        }

        // check cache first
//        val cached: Account = userCache.getUser(id)
//        if (cached != null) return cached

        // get from api
//        val account = api.getUser(id)
//        userCache.put(id, account)
//        return account
    }

    suspend fun getNumPets(uid: String): Int {
        return getPets(uid).size
    }

    suspend fun addItemToTracker(petId: String?, item: TrackerItem?) {
        api.addItemToTracker(petId, item)
    }

    suspend fun getPets(uid: String): List<Pet> {
        if (uid.isEmpty()) return emptyList()
        val map: Map<String, Boolean> = api.getPets(uid) ?: return emptyList()
        val ids = map.keys

        val pets: List<Pet> = ids.map { id -> getPet(id) }
        return pets
    }

    suspend fun addPet(id: String?, p: Pet?) {
        api.addPet(id, p)
    }

    suspend fun getEvents(petId: String?): Map<String?, TrackerItem?>? {
        return api.getEvents(petId)
    }

    suspend fun addEvent(petId: String?, dayId: String?, event: TrackerItem?) {
        api.addEvent(petId, dayId, event)
    }
}