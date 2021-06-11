package com.caitlynwiley.pettracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.caitlynwiley.pettracker.R;
import com.caitlynwiley.pettracker.fragments.ChooseAdditionTypeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class AddPetActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        // set the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
        setSupportActionBar(toolbar);

        // add menu icon to action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.add_pet_frag_view, new ChooseAdditionTypeFragment()).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // if the back button is clicked on, log user out and go back to login screen
       if (item.getItemId() == android.R.id.home) {
           FirebaseAuth.getInstance().signOut();
           PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                   .remove("logged_in")
                   .remove("creating_pet")
                   .apply();
           Intent i = new Intent(AddPetActivity.this, LoginActivity.class);
           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(i);
           finish();
           return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
