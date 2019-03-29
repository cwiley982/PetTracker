package com.caitlynwiley.pettracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManagePetsFragment extends Fragment implements View.OnClickListener {

    private View mFragView;
    private FloatingActionButton mFab;
    private AlertDialog mDiag;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.manage_pets_fragment, container, false);

        mDiag = new AlertDialog.Builder(getContext())
                .setView(R.layout.add_pet_dialog)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // save the new pet...
                        AlertDialog d = (AlertDialog) dialog;
                        String name = ((EditText) d.findViewById(R.id.pet_name)).getText().toString();
                        int genderId = ((RadioGroup) d.findViewById(R.id.pet_gender)).getCheckedRadioButtonId();
                        int speciesId = ((RadioGroup) d.findViewById(R.id.pet_species)).getCheckedRadioButtonId();
                        String years = ((EditText) d.findViewById(R.id.pet_age_years)).getText().toString();
                        String months = ((EditText) d.findViewById(R.id.pet_age_months)).getText().toString();
                        Pet pet = new Pet(name, years, months, genderId, speciesId);
                        ref.child("pets").push().setValue(pet); // doesn't like this, won't add to db
                        // TODO: add pet to users list
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDiag.cancel();
                    }
                })
                .create();

        mFab = mFragView.findViewById(R.id.add_pet_fab);
        mFab.setOnClickListener(this);
        return mFragView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pet_fab:
                mDiag.show();
                // open diag view to add new pet
                // ask for name, age, gender, species (dog, cat, bird, etc), opt birthday, opt breed
        }
    }
}
