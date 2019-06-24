package com.caitlynwiley.pettracker;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    // TODO: add logout option
    /*
    AuthUI.getInstance()
        .signOut(this)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                // ...
            }
        });
     */
    private DrawerLayout mDrawerLayout;
    private FrameLayout mFrameLayout;
    private FragmentManager mFragmentManager;

    private NavigationView mNavView;
    private View mNavHeader;
    private FirebaseDatabase database;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private Account user;

    private String petID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

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
        mFrameLayout = findViewById(R.id.frame_layout);
        mFragmentManager = getSupportFragmentManager();
        mNavView = findViewById(R.id.nav_view);
        mNavHeader = mNavView.getHeaderView(R.layout.nav_drawer_header);

        // set the toolbar as the action bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        // add menu icon to action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // add listener to navigation view to watch for menu items being selected
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                        /*case R.id.calendar_item:
                            newFrag = new CalendarFragment();
                            break;
                        case R.id.shopping_list_item:
                            newFrag = new ShoppingListFragment();
                            break;
                        case R.id.wish_list_item:
                            newFrag = new WishListFragment();
                            break;
                        case R.id.schedule_item:
                            newFrag = new ScheduleFragment();
                            break;*/
                        case R.id.tracker_item:
                            newFrag = new TrackerFragment();
                            break;
                        case R.id.manage_pets_item:
                            newFrag = new ManagePetsFragment();
                            break;
                        case R.id.settings_item:
                            // start a new activity here
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        default:
                            return false;
                    }
                }
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_view, newFrag)
                        .commit();
                return true;
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("dark_theme_enabled")) {
            boolean enabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key, false);
            if (enabled) {
                this.setTheme(R.style.DarkTheme);
            } else {
                this.setTheme(R.style.LightTheme);
            }
        }
    }
}
