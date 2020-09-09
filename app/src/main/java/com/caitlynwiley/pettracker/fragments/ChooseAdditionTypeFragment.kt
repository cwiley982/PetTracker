package com.caitlynwiley.pettracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.caitlynwiley.pettracker.R

class ChooseAdditionTypeFragment : Fragment() {
    private lateinit var mFragView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mFragView = inflater.inflate(R.layout.choose_new_or_existing_pet, container, false)

        val petIdOption = mFragView.findViewById<Button>(R.id.add_existing_button)
        petIdOption.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.add_pet_frag_view, AddByIdFragment())
                    .commit()
        }

        val createPetOption = mFragView.findViewById<Button>(R.id.create_new_button)
        createPetOption.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.add_pet_frag_view, CreatePetFragment())
                    .commit()
        }

        return mFragView
    }
}