package com.caitlynwiley.pettracker.models

class Account(userId: String, email: String) {

    private lateinit var userId: String
    private var pets: MutableMap<String, Boolean>? = null
    private lateinit var email: String

    init {
        pets = HashMap()
        setEmail(email)
        setUserId(userId)
    }

    fun addPet(id: String) {
        pets!![id] = true
    }

    fun removePet(id: String) {
        pets!!.remove(id)
    }

    fun getPets() : MutableMap<String, Boolean>? {
        return pets
    }

    fun getUserId() : String {
        return userId
    }

    private fun setUserId(userId: String) {
        this.userId = userId
    }

    fun getEmail() : String {
        return email
    }

    private fun setEmail(email: String) {
        this.email = email
    }

}