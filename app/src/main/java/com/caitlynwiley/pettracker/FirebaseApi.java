package com.caitlynwiley.pettracker;

import com.caitlynwiley.pettracker.models.Pet;
import com.caitlynwiley.pettracker.models.TrackerItem;
import com.caitlynwiley.pettracker.models.UserInfo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FirebaseApi {

    String BASE_URL = "https://pet-tracker-1530373031875.firebaseio.com";

    @GET("/users/{uid}/num_pets.json")
    Call<UserInfo> getNumPets(@Path("uid") String uid);

    @POST("/pets/{id}/events")
    Call<Void> addItemToTracker(@Path("id") String petId,
                                @Body TrackerItem item);

    @GET("/users/{uid}/pets.json")
    Call<Map<String, Boolean>> getPets(@Path("uid") String uid);

    @GET("/pets/{id}.json")
    Call<Pet> getPet(@Path("id") String petId);

    @PUT("/pets/{id}.json")
    Call<Void> addPet(@Path("id") String id, @Body Pet p);

    @GET("/pets/{id}/events.json")
    Call<Map<String, TrackerItem>> getEvents(@Path("id") String petId);

    @PUT("/pets/{petId}/events/{eventId}.json")
    Call<Void> addEvent(@Path("petId") String petId, @Path("eventId") String dayId, @Body TrackerItem event);

    /*
    To use apis:

    OkHttpClient httpClient = (new OkHttpClient.Builder()).build();
    Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
     */
}
