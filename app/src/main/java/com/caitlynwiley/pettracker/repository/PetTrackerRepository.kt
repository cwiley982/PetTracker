package com.caitlynwiley.pettracker.repository

import android.util.Log
import com.caitlynwiley.pettracker.models.Account
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.models.TrackerItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
    private var realtimeApi: RealtimeApi // old implementation
    private var firestoreApi: FirestoreApi // new implementation

    init {
        val gson = GsonConverterFactory.create(GsonBuilder().setLenient().create())
        var retrofit = Retrofit.Builder()
            .baseUrl(RealtimeApi.BASE_URL)
            .addConverterFactory(gson)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
        realtimeApi = retrofit.create(RealtimeApi::class.java)

        val customGson = GsonBuilder()
            .setLenient()
            .registerTypeAdapterFactory(DocumentTypeAdapterFactory(GsonBuilder().create()))
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(FirestoreApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
        firestoreApi = retrofit.create(FirestoreApi::class.java)
    }

    suspend fun getPet(id: String): Pet {
        // check cache first
//        val cached: Pet = petCache.getPet(id)
//        if (cached != null) return cached

        if (id.isEmpty()) return Pet()

        return try {
            // get from api
            val pet = realtimeApi.getPet(id)
//            petCache.put(id, pet)
            pet ?: Pet()
        } catch (_: KotlinNullPointerException) {
            Pet()
        }
    }

    suspend fun getAccount(id: String): Account? {
        return firestoreApi.getUser(id)
//        val db = Firebase.firestore
//        db.collection("users").whereEqualTo("uid", "123").get()
//            .addOnSuccessListener {
//                it.documents[0].toObject<Account>()
//                Log.d("PTRepo", "num matches: " + it.size())
//            }
//        return try {
//            realtimeApi.getUser(id)
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
        return 1
//        if (uid.isEmpty()) return 0
//        val map: Map<String, Boolean> = api.getPets(uid) ?: return 0
//        return map.keys.size
    }

    suspend fun addItemToTracker(petId: String?, item: TrackerItem?) {
        realtimeApi.addItemToTracker(petId, item)
    }

    suspend fun getPets(uid: String): List<Pet> {
        val db = Firebase.firestore
        db.collection("users").whereEqualTo("uid", "123").get()
            .addOnSuccessListener {
                Log.d("PTRepo", "num matches: " + it.size())
            }
        return emptyList()
//        if (uid.isEmpty()) return emptyList()
//        val map: Map<String, Boolean> = api.getPets(uid) ?: return emptyList()
//        val ids = map.keys
//
//        val pets: List<Pet> = ids.map { id -> getPet(id) }
//        return pets
    }

    suspend fun addPet(id: String?, p: Pet?) {
        realtimeApi.addPet(id, p)
    }

    suspend fun getEvents(petId: String?): Map<String?, TrackerItem?>? {
        return realtimeApi.getEvents(petId)
    }

    suspend fun addEvent(petId: String?, dayId: String?, event: TrackerItem?) {
        realtimeApi.addEvent(petId, dayId, event)
    }

    suspend fun deleteEventAt(index: Int) {
//        realtimeApi.removeEvent(index)
    }
}