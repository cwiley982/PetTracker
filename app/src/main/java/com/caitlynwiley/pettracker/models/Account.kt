package com.caitlynwiley.pettracker.models

import com.google.gson.annotations.SerializedName

class Account(@SerializedName("user_id") var userId: String,
              @SerializedName("email") var email: String) {

    @SerializedName("pets")
    var pets: MutableMap<String, Boolean> = HashMap()

    constructor() : this("", "")

    fun addPet(id: String) {
        pets[id] = true
    }

    fun removePet(id: String) {
        pets.remove(id)
    }
}