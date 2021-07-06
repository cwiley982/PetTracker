package com.caitlynwiley.pettracker

import com.caitlynwiley.pettracker.models.Account
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.models.TrackerItem
import retrofit2.Call
import retrofit2.http.*

interface FirebaseApi {
    @GET("/users/{uid}/num_pets.json")
    fun getNumPets(@Path("uid") uid: String?): Call<Int?>

    @POST("/pets/{id}/events")
    fun addItemToTracker(@Path("id") petId: String?,
                         @Body item: TrackerItem?): Call<Void?>

    @GET("/users/{uid}/pets.json")
    fun getPets(@Path("uid") uid: String?): Call<Map<String?, Boolean?>?>

    @GET("/pets/{id}.json")
    fun getPet(@Path("id") petId: String?): Call<Pet?>

    @PUT("/pets/{id}.json")
    fun addPet(@Path("id") id: String?, @Body p: Pet?): Call<Void?>

    @GET("/pets/{id}/events.json")
    fun getEvents(@Path("id") petId: String?): Call<Map<String?, TrackerItem?>?>

    @PUT("/pets/{petId}/events/{eventId}.json")
    fun addEvent(@Path("petId") petId: String?, @Path("eventId") dayId: String?, @Body event: TrackerItem?): Call<Void?>

    @GET("/users/{userId}")
    fun getUser(@Path("userId") userId: String?): Call<Account?>

    /*
    To use apis:

    OkHttpClient httpClient = (new OkHttpClient.Builder()).build();
    Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
     */

    companion object {
        const val BASE_URL = "https://pet-tracker-1530373031875.firebaseio.com"
    }
}