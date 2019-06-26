package com.caitlynwiley.pettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CreatePetFragment extends Fragment {
    private View mFragView;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.create_pet_layout, container, false);

        mFragView.findViewById(R.id.create_pet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save the new pet...
                String name = ((EditText) v.findViewById(R.id.new_pet_name)).getText().toString();
                int genderId = ((RadioGroup) v.findViewById(R.id.pet_gender)).getCheckedRadioButtonId();
                int speciesId = ((RadioGroup) v.findViewById(R.id.pet_species)).getCheckedRadioButtonId();
                String years = ((EditText) v.findViewById(R.id.pet_age_years)).getText().toString();
                String months = ((EditText) v.findViewById(R.id.pet_age_months)).getText().toString();
                Pet pet = new Pet(name, years, months, getGender(genderId), getSpecies(speciesId));
                String petId = ref.child("pets").push().getKey();
                pet.setId(petId);
                ref.child("pets").child(petId).setValue(pet);
                ref.child("users").child(mUid).child("pets").child(petId).setValue(true);

                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return mFragView;
    }

    private String getGender(int id) {
        switch (id) {
            case R.id.male_btn:
                return "male";
            default:
                return "female";
        }
    }

    private String getSpecies(int id) {
        switch (id) {
            case R.id.dog_btn:
                return "dog";
            default:
                return "cat";
        }
    }
}