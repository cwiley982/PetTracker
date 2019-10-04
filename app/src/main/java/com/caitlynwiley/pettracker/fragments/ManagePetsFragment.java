package com.caitlynwiley.pettracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.caitlynwiley.pettracker.R;
import com.caitlynwiley.pettracker.models.Pet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ManagePetsFragment extends Fragment implements View.OnClickListener {

    private View mFragView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference();

    private EditText mPetNameET;
    private EditText mYearsET;
    private EditText mMonthsET;
    private RadioGroup mGenderGroup;
    private RadioGroup mSpeciesGroup;
    private FloatingActionButton mSaveEditFab;

    private boolean mEditing;

    private String mUid;
    private String petId;
    private Pet pet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.fragment_manage_pets, container, false);

        mUid = mAuth.getCurrentUser().getUid();
        petId = "";
        ref.child("users").child(mUid).child("pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for (DataSnapshot d : data) {
                    petId = d.getKey();
                    setUpPetListener();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mPetNameET = mFragView.findViewById(R.id.pet_name);
        mYearsET = mFragView.findViewById(R.id.pet_age_years);
        mMonthsET = mFragView.findViewById(R.id.pet_age_months);
        mGenderGroup = mFragView.findViewById(R.id.pet_gender);
        mSpeciesGroup = mFragView.findViewById(R.id.pet_species);
        mSaveEditFab = mFragView.findViewById(R.id.save_edit_fab);

        mPetNameET.setEnabled(false);
        mYearsET .setEnabled(false);
        mMonthsET.setEnabled(false);
        mGenderGroup.setEnabled(false);
        mSpeciesGroup.setEnabled(false);

        mSaveEditFab.setImageResource(R.drawable.ic_edit_black_24dp);
        mSaveEditFab.setOnClickListener(this);

        return mFragView;
    }

    private void setUpPetListener() {
        ref.child("pets").child(petId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pet = dataSnapshot.getValue(Pet.class);
                fillFields();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillFields() {
        mPetNameET.setText(pet.getName());
        mYearsET.setText(String.format(Locale.US, "%d", (int) pet.getAge()));
        mMonthsET.setText(String.format(Locale.US, "%d", (int) ((pet.getAge() % 1) * 12) ));
        ((RadioButton) mFragView.findViewById(pet.getGender().equals("male") ? R.id.male_btn : R.id.female_btn)).toggle();
        ((RadioButton) mFragView.findViewById(pet.getSpecies().equals("dog") ? R.id.dog_btn : R.id.cat_btn)).toggle();
    }

    @Override
    public void onClick(View v) {
        if (mEditing) {
            savePet();
        } else {
            enableEdit();
        }
    }

    private void enableEdit() {
        // show save fab, hide edit fab
        mSaveEditFab.setImageResource(R.drawable.ic_check_black_24dp);
        mPetNameET.setEnabled(true);
        mYearsET .setEnabled(true);
        mMonthsET.setEnabled(true);
        mGenderGroup.setEnabled(true);
        mSpeciesGroup.setEnabled(true);
        mEditing = true;
    }

    private void savePet() {
        // get values from fields
        String name = mPetNameET.getText().toString();
        String years = mYearsET.getText().toString();
        String months = mMonthsET.getText().toString();
        int genderId = mGenderGroup.getCheckedRadioButtonId();
        int speciesId = mSpeciesGroup.getCheckedRadioButtonId();
        Pet pet = new Pet(name, years, months, getGender(genderId), getSpecies(speciesId));
        pet.setId(petId);
        ref.child("pets").child(petId).setValue(pet);
        // change icon on fab
        mSaveEditFab.setImageResource(R.drawable.ic_edit_black_24dp);
        mPetNameET.setEnabled(false);
        mYearsET .setEnabled(false);
        mMonthsET.setEnabled(false);
        mGenderGroup.setEnabled(false);
        mSpeciesGroup.setEnabled(false);
        mEditing = false;
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
