package com.caitlynwiley.pettracker.fragments

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
import com.caitlynwiley.pettracker.FirebaseApi
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.activities.MainActivity
import com.caitlynwiley.pettracker.models.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class CreatePetFragment : Fragment() {
    private lateinit var mFragView: View
    private val ref = FirebaseDatabase.getInstance().reference
    private val mUid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mFragView = inflater.inflate(R.layout.create_pet_layout, container, false)
        mFragView.findViewById<View>(R.id.create_pet_button).setOnClickListener { v: View? ->
            // save the new pet...
            val name = (mFragView.findViewById<View>(R.id.new_pet_name) as EditText).text.toString()
            val genderId = (mFragView.findViewById<View>(R.id.pet_gender) as RadioGroup).checkedRadioButtonId
            val speciesId = (mFragView.findViewById<View>(R.id.pet_species) as RadioGroup).checkedRadioButtonId
            val years = (mFragView.findViewById<View>(R.id.pet_age_years) as EditText).text.toString()
            val months = (mFragView.findViewById<View>(R.id.pet_age_months) as EditText).text.toString()
            val pet = Pet(name, years, months, getGender(genderId), getSpecies(speciesId))
            val petId = ref.child("pets").push().key
            pet.id = petId
            AddPetTask().execute(pet)
            ref.child("users").child(mUid).child("pets").child(petId!!).setValue(true)
            ref.child("users").child(mUid).child("num_pets").setValue(1)
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("creating_pet", false).apply()
            startActivity(Intent(activity, MainActivity::class.java))
        }
        return mFragView
    }

    private fun getGender(id: Int): String {
        return when (id) {
            R.id.male_btn -> "male"
            else -> "female"
        }
    }

    private fun getSpecies(id: Int): String {
        return when (id) {
            R.id.dog_btn -> "dog"
            else -> "cat"
        }
    }

    internal class AddPetTask : AsyncTask<Pet?, Void?, Void?>() {
        override fun doInBackground(vararg pets: Pet?): Void? {
            val gson = GsonBuilder()
                    .setLenient()
                    .create()
            val retrofit = Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            try {
                retrofit.create(FirebaseApi::class.java).addPet(pets[0]?.id, pets[0])?.execute()
            } catch (e: IOException) {
                Log.d("api", "error adding pet")
            }
            return null
        }
    }
}