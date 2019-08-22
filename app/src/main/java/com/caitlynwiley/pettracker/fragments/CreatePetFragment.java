package com.caitlynwiley.pettracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.caitlynwiley.pettracker.FirebaseApi;
import com.caitlynwiley.pettracker.activities.MainActivity;
import com.caitlynwiley.pettracker.R;
import com.caitlynwiley.pettracker.models.Pet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreatePetFragment extends Fragment {
    private View mFragView;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.create_pet_layout, container, false);

        mFragView.findViewById(R.id.create_pet_button).setOnClickListener(v -> {
            // save the new pet...
            String name = ((EditText) mFragView.findViewById(R.id.new_pet_name)).getText().toString();
            int genderId = ((RadioGroup) mFragView.findViewById(R.id.pet_gender)).getCheckedRadioButtonId();
            int speciesId = ((RadioGroup) mFragView.findViewById(R.id.pet_species)).getCheckedRadioButtonId();
            String years = ((EditText) mFragView.findViewById(R.id.pet_age_years)).getText().toString();
            String months = ((EditText) mFragView.findViewById(R.id.pet_age_months)).getText().toString();
            Pet pet = new Pet(name, years, months, getGender(genderId), getSpecies(speciesId));
            String petId = ref.child("pets").push().getKey();
            pet.setId(petId);
            addPet(petId, pet);
            ref.child("users").child(mUid).child("pets").child(petId).setValue(true);
            ref.child("users").child(mUid).child("num_pets").setValue(1);

            startActivity(new Intent(getActivity(), MainActivity.class));
        });

        return mFragView;
    }

    // TODO: NetworkOnMainThreadException wrap in async task
    private void addPet(String id, Pet p) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        try {
            retrofit.create(FirebaseApi.class).addPet(id, p).execute();
        } catch (IOException e) {
            Log.d("api", "error adding pet");
        }
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
