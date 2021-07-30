package com.caitlynwiley.pettracker.repository

import com.caitlynwiley.pettracker.models.Account
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.models.TrackerItem
import retrofit2.http.*

internal interface FirebaseApi {
    companion object {
        const val BASE_URL = "https://pet-tracker-1530373031875.firebaseio.com"
    }

    @POST("/pets/{id}/events.json")
    suspend fun addItemToTracker(@Path("id") petId: String?, @Body item: TrackerItem?): Void

    @GET("/users/{uid}/pets.json")
    suspend fun getPets(@Path("uid") uid: String): Map<String, Boolean>?

    @GET("/pets/{id}.json")
    suspend fun getPet(@Path("id") petId: String?): Pet?

    @PUT("/pets/{id}.json")
    suspend fun addPet(@Path("id") id: String?, @Body p: Pet?): Void

    @GET("/pets/{id}/events.json")
    suspend fun getEvents(@Path("id") petId: String?): Map<String?, TrackerItem?>?

    @PUT("/pets/{petId}/events/{eventId}.json")
    suspend fun addEvent(@Path("petId") petId: String?, @Path("eventId") dayId: String?, @Body event: TrackerItem?): Void

    @GET("/users/{userId}.json")
    suspend fun getUser(@Path("userId") userId: String): Account?
}