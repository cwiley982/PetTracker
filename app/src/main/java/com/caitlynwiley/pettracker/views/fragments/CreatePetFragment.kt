package com.caitlynwiley.pettracker.views.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.activities.MainActivity
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.caitlynwiley.pettracker.views.screens.PetType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException

class CreatePetFragment : Fragment() {
    private lateinit var mFragView: View
    private val ref = FirebaseDatabase.getInstance().reference
    private val mUid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mFragView = inflater.inflate(R.layout.create_pet_layout, container, false)
        mFragView.findViewById<View>(R.id.create_pet_button).setOnClickListener {
            // save the new pet...
            val name = (mFragView.findViewById<View>(R.id.new_pet_name) as EditText).text.toString()
            val genderId = (mFragView.findViewById<View>(R.id.pet_gender) as RadioGroup).checkedRadioButtonId
            val speciesId = (mFragView.findViewById<View>(R.id.pet_species) as RadioGroup).checkedRadioButtonId
            val years = (mFragView.findViewById<View>(R.id.pet_age_years) as EditText).text.toString()
            val months = (mFragView.findViewById<View>(R.id.pet_age_months) as EditText).text.toString()
            val pet = Pet(name, years, months, getGender(genderId), getSpecies(speciesId), "")
            val petId = ref.child("pets").push().key
            pet.id = petId ?: ""
            AddPetTask().execute(pet)
            ref.child("users").child(mUid).child("pets").child(petId!!).setValue(true)
            ref.child("users").child(mUid).child("num_pets").setValue(1)
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("creating_pet", false).apply()
            startActivity(Intent(activity, MainActivity::class.java))
        }
        return mFragView
    }

    private fun getGender(id: Int): Pet.Gender {
        return when (id) {
            R.id.male_btn -> Pet.Gender.MALE
            R.id.female_btn -> Pet.Gender.FEMALE
            else -> Pet.Gender.UNKNOWN
        }
    }

    private fun getSpecies(id: Int): PetType {
        return when (id) {
            R.id.dog_btn -> PetType.DOG
            R.id.cat_btn -> PetType.CAT
            else -> PetType.OTHER
        }
    }

    internal class AddPetTask : AsyncTask<Pet?, Void?, Void?>() {
        override fun doInBackground(vararg pets: Pet?): Void? {
            runBlocking {
                val repo = PetTrackerRepository()
                launch {
                    try {
                        repo.addPet(pets[0]?.id, pets[0])
                    } catch (e: IOException) {
                        Log.d("api", "error adding pet")
                    }
                }
            }
            return null
        }
    }
}