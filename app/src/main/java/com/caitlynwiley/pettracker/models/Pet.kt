package com.caitlynwiley.pettracker.models

import com.google.gson.annotations.SerializedName

class Pet {
    @SerializedName("species")
    var species: String? = null

    @SerializedName("breed")
    var breed: String? = null

    @SerializedName("name")
    var name: String = ""

    @SerializedName("age")
    var age = 0.0
        private set

    @SerializedName("birthday")
    var birthday: String = ""

    @SerializedName("gender")
    var gender: String? = null
        private set

    @SerializedName("id")
    var id: String? = null

    @SerializedName("primaryOwnerName")
    var primaryOwnerName: String = ""

    constructor()

    constructor(name: String, years: String, months: String, gender: String, species: String) {
        this.name = name
        setAge(years, months)
        setGender(gender)
        this.species = species
    }

    private fun setAge(y: String, m: String) {
        val years = y.toInt().toDouble()
        val months = m.toInt().toDouble()
        age = years + months / 12.0
    }

    private fun setGender(gender: String) {
        this.gender = gender
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Pet) {
            other.id == id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}