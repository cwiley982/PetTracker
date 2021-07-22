package com.caitlynwiley.pettracker.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.views.fragments.ChooseAdditionTypeFragment
import com.google.firebase.auth.FirebaseAuth

class AddPetActivity : BaseActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        // set the toolbar as the action bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitleTextColor(resources.getColor(android.R.color.white, null))
        setSupportActionBar(toolbar)

        // add menu icon to action bar
        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.add_pet_frag_view, ChooseAdditionTypeFragment()).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // if the back button is clicked on, log user out and go back to login screen
        if (item.itemId == android.R.id.home) {
            FirebaseAuth.getInstance().signOut()
            PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
                .remove("logged_in")
                .remove("creating_pet")
                .apply()
            val i = Intent(this@AddPetActivity, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}