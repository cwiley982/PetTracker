package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddPetActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean useDarkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme_enabled", false);
        setTheme(useDarkTheme ? R.style.DarkTheme : R.style.LightTheme);
        setContentView(R.layout.activity_add_pet);

        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.add_pet_frag_view, new ChooseAdditionTypeFragment()).commit();
        }
    }
}
