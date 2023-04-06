package com.caitlynwiley.pettracker.models

import com.caitlynwiley.pettracker.view.screens.PetType
import com.google.gson.annotations.SerializedName

class Pet {
    @SerializedName("species")
    var species: PetType = PetType.OTHER

    @SerializedName("breed")
    var breed: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("age")
    var age: String = ""
        private set

    @SerializedName("birthMonth")
    var birthMonth: Int = -1

    @SerializedName("birthYear")
    var birthYear: Int = -1

    @SerializedName("gender")
    var gender: Gender = Gender.UNKNOWN
        private set

    @SerializedName("id")
    var id: String = ""

    @SerializedName("primaryOwnerName")
    var primaryOwnerName: String = ""

    constructor()

    constructor(name: String, birthYear: String, birthMonth: String, gender: Gender, species: PetType, breed: String) {
        this.name = name
        this.birthMonth = birthMonth.toInt()
        this.birthYear = birthYear.toInt()
        calculateAge(birthYear, birthMonth)
        this.gender = gender
        this.species = species
        this.breed = breed
    }

    private fun calculateAge(year: String, month: String) {
        val birthYear = year.toInt()
        val birthMonth = month.toInt()
        // TODO: make this not hard-coded
        val currentYear = 2021
        val currentMonth = 7
        var ageMonths = currentMonth - birthMonth
        var ageYears = currentYear - birthYear
        if (ageMonths < 0) {
            ageYears--
            ageMonths += 12
        }
        age = "$ageYears year${if (ageYears > 1) "s" else ""}" +
                when (ageMonths) {
                    0 -> ""
                    1 -> " $ageMonths month"
                    else -> " $ageMonths months"
                }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Pet) {
            other.id == id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    enum class Gender {
        @SerializedName("male") MALE,
        @SerializedName("female") FEMALE,
        @SerializedName("unknown") UNKNOWN
    }
}