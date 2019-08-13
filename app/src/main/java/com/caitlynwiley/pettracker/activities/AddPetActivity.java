package com.caitlynwiley.pettracker.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.caitlynwiley.pettracker.R;
import com.caitlynwiley.pettracker.fragments.ChooseAdditionTypeFragment;

public class AddPetActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.add_pet_frag_view, new ChooseAdditionTypeFragment()).commit();
        }
    }
}
