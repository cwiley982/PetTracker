package com.caitlynwiley.pettracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManagePetsFragment extends Fragment implements View.OnClickListener {

    private String[] genders = {"male", "female"};
    private String[] species = {"dog", "cat"};
    private View mFragView;
    private FloatingActionButton mFab;
    private AlertDialog mDiag;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference();

    private String mUid;
    private ArrayList<Pet> pets;
    private ArrayList<String> petIds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.manage_pets_fragment, container, false);

        mUid = mAuth.getCurrentUser().getUid();
        pets = new ArrayList<>();
        petIds = new ArrayList<>();
        ListView mListView = mFragView.findViewById(R.id.pets_listview);
        final PetAdapter adapter = new PetAdapter();
        mListView.setAdapter(adapter);

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
                        Pet pet = new Pet(name, years, months, getGender(genderId), getSpecies(speciesId));
                        String petId = ref.child("pets").push().getKey();
                        pet.setId(petId);
                        ref.child("pets").child(petId).setValue(pet);
                        ref.child("users").child(mUid).child("pets").child(petId).setValue(true);
                        adapter.notifyDataSetChanged();
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

        ref.child("users").child(mUid).child("pets").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();
                petIds.add(id);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                pets.remove(dataSnapshot.getValue(Pet.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("pets").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                pets.add(dataSnapshot.getValue(Pet.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pet changed = dataSnapshot.getValue(Pet.class);
                for (Pet p : pets) {
                    if (p.equals(changed)) {
                        pets.remove(p);
                        pets.add(changed);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                pets.remove(dataSnapshot.getValue(Pet.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //no-op
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //no-op
            }
        });
        return mFragView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pet_fab:
                // open diag view to add new pet
                mDiag.show();
                break;
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

    class PetAdapter extends BaseAdapter {

        TextView nameTextView;

        @Override
        public int getCount() {
            return petIds == null ? 0 : petIds.size();
        }

        @Override
        public Object getItem(int i) {
            String id = petIds.get(i);
            for (Pet p : pets) {
                if (id.equals(p.getId())) {
                    return p;
                }
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View pet = getLayoutInflater().inflate(R.layout.pet_item, viewGroup, false);
            nameTextView = pet.findViewById(R.id.pet_name);
            nameTextView.setText(((Pet) getItem(i)).getName());
            return pet;
        }
    }
}
