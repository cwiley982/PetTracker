package com.caitlynwiley.pettracker;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    FrameLayout mFrameLayout;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mFrameLayout = findViewById(R.id.frame_layout);
        mFragmentManager = getSupportFragmentManager();

        // set the toolbar as the action bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        // add menu icon to action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // add listener to navigation view to watch for menu items being selected
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                        case R.id.calendar_item:
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
                            break;
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
}
