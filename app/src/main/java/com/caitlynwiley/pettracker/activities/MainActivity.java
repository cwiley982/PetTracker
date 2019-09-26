package com.caitlynwiley.pettracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.caitlynwiley.pettracker.FirebaseApi;
import com.caitlynwiley.pettracker.R;
import com.caitlynwiley.pettracker.fragments.ManagePetsFragment;
import com.caitlynwiley.pettracker.fragments.SettingsFragment;
import com.caitlynwiley.pettracker.fragments.TrackerFragment;
import com.caitlynwiley.pettracker.models.Account;
import com.caitlynwiley.pettracker.models.Pet;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;

    private NavigationView mNavView;
    private View mNavHeader;
    private FirebaseDatabase database;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Retrofit retrofit;

    private Account user;
    private String petID = "0";
    private Pet mPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        FirebaseApi api = retrofit.create(FirebaseApi.class);
        api.getPets(mUser.getUid()).enqueue(new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                petID = (String) response.body().keySet().toArray()[0];
                getPet();
            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {

            }
        });

        ref.child("users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Account.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mFragmentManager = getSupportFragmentManager();
        mNavView = findViewById(R.id.nav_view);
        mNavHeader = mNavView.getHeaderView(R.layout.nav_drawer_header);

        // set the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
        setSupportActionBar(toolbar);
        // add menu icon to action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // add listener to navigation view to watch for menu items being selected
        mNavView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            mDrawerLayout.closeDrawers();

            // replace fragment with new fragment
            Fragment newFrag;
            int id = item.getItemId();
            // get fragment that already exists if it exists
            newFrag = mFragmentManager.findFragmentById(id);
            // if not create it
            if (newFrag == null) {
                switch (id) {
                    case R.id.tracker_item:
                        newFrag = new TrackerFragment();
                        break;
                    case R.id.manage_pets_item:
                        newFrag = new ManagePetsFragment();
                        break;
                    case R.id.share_item:
                        // no new fragment, just open sharing menu
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, getShareableText());
                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                        return false;
                    case R.id.settings_item:
                        newFrag = new SettingsFragment();
                        break;
                    default:
                        return false;
                }
            }

            if (newFrag instanceof SettingsFragment) {
                actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            } else {
                actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }

            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_view, newFrag)
                    .commit();
            return true;
        });

        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.fragment_view, new TrackerFragment()).commit();
        }
    }

    private String getShareableText() {
        if (mPet == null) {
            return "Here's my pet's ID: " + petID;
        } else {
            return "Here's " + mPet.getName() + "'s ID: " + petID;
        }
    }

    private void getPet() {
        FirebaseApi api = retrofit.create(FirebaseApi.class);
        api.getPet(petID).enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPet = response.body();
                }
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // if the menu button is clicked on, open the drawer
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getPetID() {
        return petID;
    }
}
