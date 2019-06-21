package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ManagePetsFragment extends Fragment /*implements View.OnClickListener*/ {

    //private Animation mRotateForward;
    //private Animation mRotateBackward;
    //private Animation mMiniAppear;
    //private Animation mMiniDisappear;

    private View mFragView;
    //private FloatingActionButton mFab;
    //private FloatingActionButton mAddPetFab;
    //private FloatingActionButton mCreatePetFab;
    //private AlertDialog mCreatePetDiag;
    //private AlertDialog mAddPetDiag;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference();

    private String mUid;
    private ArrayList<Pet> pets;
    private ArrayList<String> petIds;
    //private boolean mIsFabMenuOpen;

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

        /*mCreatePetDiag = new AlertDialog.Builder(getContext())
                .setView(R.layout.create_pet_layout)
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
                        mCreatePetDiag.cancel();
                    }
                })
                .create();

        mAddPetDiag = new AlertDialog.Builder(getContext())
                .setView(R.layout.add_pet_dialog)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // save the new pet...
                        AlertDialog d = (AlertDialog) dialog;
                        String petId = ((EditText) d.findViewById(R.id.pet_id)).getText().toString();
                        ref.child("users").child(mUid).child("pets").child(petId).setValue(true);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAddPetDiag.cancel();
                    }
                })
                .create();

        mFab = mFragView.findViewById(R.id.manage_pets_fab);
        mFab.setOnClickListener(this);
        mAddPetFab = mFragView.findViewById(R.id.add_pet_fab);
        mAddPetFab.setOnClickListener(this);
        mCreatePetFab = mFragView.findViewById(R.id.create_pet_fab);
        mCreatePetFab.setOnClickListener(this);
        */

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

        /*
        mRotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_backward);

        mMiniAppear = AnimationUtils.loadAnimation(getContext(), R.anim.mini_fab_appear);
        mMiniAppear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCreatePetFab.show();
                mAddPetFab.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mMiniDisappear = AnimationUtils.loadAnimation(getContext(), R.anim.mini_fab_disappear);
        mMiniDisappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCreatePetFab.hide();
                mAddPetFab.hide();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mIsFabMenuOpen = false;
        */

        return mFragView;
    }

    /*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_pets_fab:
                // show/hide mini fabs
                showHideMiniFabs();
                mIsFabMenuOpen = !mIsFabMenuOpen;
                break;
            case R.id.add_pet_fab:
                // open diag view to add new pet by id
                mCreatePetDiag.show();
                break;
            case R.id.create_pet_fab:
                // open dialog to create new pet
                mAddPetDiag.show();
                break;
        }
    }

    private void showHideMiniFabs() {
        if (mIsFabMenuOpen) {
            // close
            mFab.startAnimation(mRotateBackward);
            mCreatePetFab.startAnimation(mMiniDisappear);
            mAddPetFab.startAnimation(mMiniDisappear);
        } else {
            // open
            mFab.startAnimation(mRotateForward);
            mCreatePetFab.startAnimation(mMiniAppear);
            mAddPetFab.startAnimation(mMiniAppear);
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
    */

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
