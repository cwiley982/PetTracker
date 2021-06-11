package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.caitlynwiley.pettracker.FirebaseApi
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.fragments.ManagePetsFragment
import com.caitlynwiley.pettracker.fragments.SettingsFragment
import com.caitlynwiley.pettracker.fragments.TrackerFragment
import com.caitlynwiley.pettracker.models.Account
import com.caitlynwiley.pettracker.models.Pet
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : BaseActivity() {
    private lateinit var mDrawerLayout : DrawerLayout
    private lateinit var mNavigationView : NavigationView
    private var mNavHeader : View? = null
    private val mDatabase = FirebaseDatabase.getInstance()
    private val mReference = mDatabase.reference
    private var mUser : FirebaseUser? = null
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var retrofit : Retrofit
    private lateinit var mFirebaseApi : FirebaseApi

    var user : Account? = null
    var petID : String = "0"
    var mPet : Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        mUser = mAuth.currentUser

        val gson = GsonBuilder().setLenient().create()
        retrofit = Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        mFirebaseApi = retrofit.create(FirebaseApi::class.java)
        mFirebaseApi.getPets(mUser?.uid)?.enqueue(object : Callback<Map<String?, Boolean?>?> {
            override fun onResponse(call: Call<Map<String?, Boolean?>?>, response: Response<Map<String?, Boolean?>?>) {
                petID = response.body()!!.keys.toTypedArray()[0] as String
                getPet()
            }

            override fun onFailure(call: Call<Map<String?, Boolean?>?>, t: Throwable) {}
        })

        mReference.child("users").child(if (mUser != null) mUser!!.uid else "").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.value as Account?
            }

            override fun onCancelled(error: DatabaseError) {
                // no-op
            }
        })

        mDrawerLayout = findViewById(R.id.drawer_layout)
        mNavigationView = findViewById(R.id.nav_view)
        mNavHeader = mNavigationView.getHeaderView(R.layout.nav_drawer_header)

        // set the toolbar as the action bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white, null))
        setSupportActionBar(toolbar)
        // add menu icon to action bar
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // add listener to navigation view to watch for menu items being selected
        mNavigationView.setNavigationItemSelectedListener { item: MenuItem ->
            item.isChecked = true
            mDrawerLayout.closeDrawers()

            // replace fragment with new fragment
            var newFragment: Fragment?
            val id = item.itemId
            newFragment = supportFragmentManager.findFragmentById(id)
            if (newFragment == null) {
                when (id) {
                    R.id.tracker_item -> newFragment = TrackerFragment()
                    R.id.manage_pets_item -> newFragment = ManagePetsFragment()
                    R.id.share_item -> {
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT, getShareableText())
                        intent.type = "text/plain"

                        val shareIntent = Intent.createChooser(intent, null)
                        startActivity(shareIntent)
                        false
                    }
                    else -> false
                }
            }

            if (newFragment is SettingsFragment) {
                actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
            } else {
                actionbar?.setHomeAsUpIndicator(R.drawable.ic_menu)
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_view, newFragment!!)
                .commit()
            true
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_view, TrackerFragment())
                    .commit()
        }
    }

    private fun getShareableText() : String {
        return if (mPet == null) {
            "Here's my pet's ID: $petID"
        } else {
            "Here's ${mPet?.name}'s ID: $petID"
        }
    }

    private fun getPet() {
        mFirebaseApi.getPet(petID)?.enqueue(object : Callback<Pet?> {
            override fun onResponse(call: Call<Pet?>, response: Response<Pet?>) {
                if (response.isSuccessful && response.body() != null) {
                    mPet = response.body()
                }
            }

            override fun onFailure(call: Call<Pet?>, t: Throwable) {
                // no-op
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // if the menu button is clicked on, open the drawer
        if (item.itemId == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}