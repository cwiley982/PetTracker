package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private FloatingActionButton mEditPetFab;
    private FloatingActionButton mSavePetFab;

    private String mUid;
    private String petId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.manage_pets_fragment, container, false);

        mUid = mAuth.getCurrentUser().getUid();
        petId = ""; //TODO

        mPetNameET = mFragView.findViewById(R.id.pet_name);
        mYearsET = mFragView.findViewById(R.id.pet_age_years);
        mMonthsET = mFragView.findViewById(R.id.pet_age_months);
        mGenderGroup = mFragView.findViewById(R.id.pet_gender);
        mSpeciesGroup = mFragView.findViewById(R.id.pet_species);
        mEditPetFab = mFragView.findViewById(R.id.edit_pet_fab);
        mSavePetFab = mFragView.findViewById(R.id.save_pet_fab);

        // fill fields with current values
        // TODO

        mEditPetFab.setVisibility(View.VISIBLE);
        mSavePetFab.setVisibility(View.GONE);

        return mFragView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_pet_fab:
                enableEdit();
                break;
            case R.id.save_pet_fab:
                savePet();
                break;
        }
    }

    private void enableEdit() {
        // show save fab, hide edit fab
        mEditPetFab.setVisibility(View.GONE);
        mSavePetFab.setVisibility(View.VISIBLE);
        mFragView.findViewById(R.id.edit_pet_view).setEnabled(true);
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
        //show edit fab, hide save fab
        mEditPetFab.show();
        mSavePetFab.hide();
        mFragView.findViewById(R.id.edit_pet_view).setEnabled(false);
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
