package com.caitlynwiley.pettracker.repository

import com.google.firestore.v1.Document
import retrofit2.http.GET
import retrofit2.http.Path

interface FirestoreApi {
    companion object {
        const val BASE_URL: String = "https://firestore.googleapis.com"
        const val PROJECT_ID: String = "pet-tracker-1530373031875"
        const val DATABASE_ID: String = "(default)"
    }

    @GET("/v1/projects/$PROJECT_ID/databases/$DATABASE_ID/documents/users/{user_id}")
    suspend fun getUser(@Path("user_id") userId: String): Document?

    /*

    {
      "name": "projects/pet-tracker-1530373031875/databases/(default)/documents/users/uUXGp7mk7MhzCbpO5imC",
        "fields": {
          "pets": {
            "arrayValue": {
              "values": [
                {
                  "referenceValue": "projects/pet-tracker-1530373031875/databases/(default)/documents/pets/I323LBlhBPp39ESiplmv"
                }
              ]
            }
          },
          "uid": {
            "stringValue": "123"
          }
        },
      "createTime": "2021-07-30T02:00:42.710838Z",
      "updateTime": "2021-07-30T17:11:08.803645Z"
    }

     */
}