package com.caitlynwiley.pettracker;

import com.caitlynwiley.pettracker.models.TrackerItem;
import com.caitlynwiley.pettracker.models.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FirebaseApi {

    String BASE_URL = "https://pet-tracker-1530373031875.firebaseio.com";

    @GET("/users/{uid}/num_pets.json")
    Call<UserInfo> getNumPets(@Path("uid") String uid);

    @POST("/pets/{id}/events")
    Call<Void> addItemToTracker(@Path("id") String petId,
                                @Body TrackerItem item);

    @GET("pets/{id}/events.json")
    Call<List<TrackerItem>> getEvents(@Path("id") String petId);

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
