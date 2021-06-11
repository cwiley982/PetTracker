package com.caitlynwiley.pettracker.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.caitlynwiley.pettracker.R
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.activities.MainActivity

class AddByIdFragment : Fragment() {
    private var mFragView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mFragView = inflater.inflate(R.layout.enter_pet_id_layout, container, false)
        mFragView?.findViewById<View>(R.id.add_pet_button)?.setOnClickListener { v: View? ->
            val id = (mFragView?.findViewById<View>(R.id.new_pet_id) as EditText).text.toString()
            FirebaseDatabase.getInstance().reference.child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("pets")
                .child(id)
                .setValue(true)
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean("creating_pet", false).apply()
            startActivity(Intent(activity, MainActivity::class.java))
        }
        return mFragView
    }
}