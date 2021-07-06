package com.caitlynwiley.pettracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.screens.PetInfoEditor
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ManagePetsFragment : Fragment(), View.OnClickListener {
    private lateinit var mFragView: View
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val ref = db.reference
    private val mPetNameET: EditText
        get() {
            return mFragView.findViewById(R.id.pet_name)
        }
    private val mYearsET: EditText
        get() {
            return mFragView.findViewById(R.id.pet_age_years)
        }
    private val mMonthsET: EditText
        get() {
            return mFragView.findViewById(R.id.pet_age_months)
        }
    private var mBirthdayET: EditText? = null
    private var mBreedET: EditText? = null
    private var mGenderGroup: RadioGroup? = null
    private var mSpeciesGroup: RadioGroup? = null
    private var mSaveEditFab: FloatingActionButton? = null
    private var mEditing = false
    private var mUid: String? = null
    private var petId: String? = null
    private var pet: Pet? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    // In Compose world
                    PetInfoEditor()
                }
            }
        }

//        mFragView = inflater.inflate(R.layout.manage_pets_fragment, container, false)
        mUid = mAuth.currentUser!!.uid
        petId = ""
        ref.child("users").child(mUid!!).child("pets").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.children
                for (d in data) {
                    petId = d.key
                    setUpPetListener()
                    return
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
//        mBirthdayET = mFragView.findViewById(R.id.pet_birthday)
//        mBreedET = mFragView.findViewById(R.id.pet_breed)
//        mGenderGroup = mFragView.findViewById(R.id.pet_gender)
//        mSpeciesGroup = mFragView.findViewById(R.id.pet_species)
//        mSaveEditFab = mFragView.findViewById(R.id.save_edit_fab)
//        mPetNameET.isEnabled = false
//        mYearsET.isEnabled = false
//        mMonthsET.isEnabled = false
//        mBirthdayET?.isEnabled = false
//        mBreedET?.isEnabled = false
//        mGenderGroup?.isEnabled = false
//        mSpeciesGroup?.isEnabled = false
//        mSaveEditFab?.setImageResource(R.drawable.ic_edit_black_24dp)
//        mSaveEditFab?.setOnClickListener(this)
//        return mFragView
        return view
    }

    private fun setUpPetListener() {
        ref.child("pets").child(petId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pet = dataSnapshot.getValue(Pet::class.java)
                fillFields()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun fillFields() {
        mPetNameET.setText(pet!!.name)
        mYearsET.setText(String.format(Locale.US, "%d", pet!!.age.toInt()))
        mMonthsET.setText(String.format(Locale.US, "%d", (pet!!.age % 1 * 12).toInt()))
        mBirthdayET!!.setText(pet!!.birthday)
        mBreedET!!.setText(pet!!.breed)
        (mFragView.findViewById<View>(if (pet!!.gender == "male") R.id.male_btn else R.id.female_btn) as RadioButton).toggle()
        (mFragView.findViewById<View>(mapSpeciesToId(pet!!.species)) as RadioButton).toggle()
    }

    private fun mapSpeciesToId(species: String?): Int {
        return when (species) {
            "dog" -> R.id.dog_btn
            "cat" -> R.id.cat_btn
            else -> R.id.cat_btn
        }
    }

    override fun onClick(v: View) {
        if (mEditing) {
            savePet()
        } else {
            enableEdit()
        }
    }

    private fun enableEdit() {
        // show save fab, hide edit fab
        mSaveEditFab!!.setImageResource(R.drawable.ic_check_black_24dp)
        mPetNameET.isEnabled = true
        mYearsET.isEnabled = true
        mMonthsET.isEnabled = true
        mBirthdayET!!.isEnabled = true
        mBreedET!!.isEnabled = true
        mGenderGroup!!.isEnabled = true
        mSpeciesGroup!!.isEnabled = true
        mEditing = true
    }

    private fun savePet() {
        // get values from fields
        val name = mPetNameET.text.toString()
        val years = mYearsET.text.toString()
        val months = mMonthsET.text.toString()
        val birthday = if (mBirthdayET!!.text == null) "" else mBirthdayET!!.text.toString()
        val breed = if (mBreedET!!.text == null) "" else mBreedET!!.text.toString()
        val genderId = mGenderGroup!!.checkedRadioButtonId
        val speciesId = mSpeciesGroup!!.checkedRadioButtonId
        val pet = Pet(name, years, months, getGender(genderId), getSpecies(speciesId))
        pet.id = petId
        pet.breed = breed
        pet.birthday = birthday
        ref.child("pets").child(petId!!).setValue(pet)

        // change icon on fab
        mSaveEditFab!!.setImageResource(R.drawable.ic_edit_black_24dp)
        mPetNameET.isEnabled = false
        mYearsET.isEnabled = false
        mMonthsET.isEnabled = false
        mBirthdayET!!.isEnabled = false
        mBreedET!!.isEnabled = false
        mGenderGroup!!.isEnabled = false
        mSpeciesGroup!!.isEnabled = false
        mEditing = false
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
}