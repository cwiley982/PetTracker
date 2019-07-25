package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TrackerFragment extends Fragment implements View.OnClickListener {

    private View mFragView;

    private Animation mRotateForward;
    private Animation mRotateBackward;
    private Animation mMiniAppear;
    private Animation mMiniDisappear;
    private Animation mLabelAppear;
    private Animation mLabelDisappear;

    private TextView mPottyFabLabel;
    private TextView mFeedFabLabel;
    private TextView mLetOutFabLabel;
    private FloatingActionButton mTrackerFab;
    private FloatingActionButton mPottyFab;
    private FloatingActionButton mFeedFab;
    private FloatingActionButton mLetOutFab;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<TrackerEvent> events = new ArrayList<>();
    private ArrayList<String> pets = new ArrayList<>();
    private String mUID;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private boolean mIsFabOpen;
    private boolean mPetsListWasEmpty = true;
    private Pet mPet;

    private ChildEventListener eventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.tracker_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUID = mUser.getUid();
        //parentActivity = getActivity();
        mRotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_backward);

        mMiniAppear = AnimationUtils.loadAnimation(getContext(), R.anim.mini_fab_appear);
        mMiniAppear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPottyFab.setVisibility(View.VISIBLE);
                mLetOutFab.setVisibility(View.VISIBLE);
                mFeedFab.setVisibility(View.VISIBLE);
                mPottyFabLabel.setVisibility(View.VISIBLE);
                mLetOutFabLabel.setVisibility(View.VISIBLE);
                mFeedFabLabel.setVisibility(View.VISIBLE);
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
                mPottyFab.setVisibility(View.GONE);
                mLetOutFab.setVisibility(View.GONE);
                mFeedFab.setVisibility(View.GONE);
                mPottyFabLabel.setVisibility(View.GONE);
                mLetOutFabLabel.setVisibility(View.GONE);
                mFeedFabLabel.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mLabelAppear = AnimationUtils.loadAnimation(getContext(), R.anim.fab_label_appear);
        mLabelDisappear = AnimationUtils.loadAnimation(getContext(), R.anim.fab_label_disappear);

        mIsFabOpen = false;
        // get views
        mTrackerFab = mFragView.findViewById(R.id.tracker_fab);
        mPottyFab = mFragView.findViewById(R.id.track_potty_fab);
        mFeedFab = mFragView.findViewById(R.id.track_fed_fab);
        mLetOutFab = mFragView.findViewById(R.id.track_let_out_fab);
        mPottyFabLabel = mFragView.findViewById(R.id.potty_fab_label);
        mFeedFabLabel = mFragView.findViewById(R.id.fed_fab_label);
        mLetOutFabLabel = mFragView.findViewById(R.id.let_out_fab_label);

        // set on click listeners
        mTrackerFab.setOnClickListener(this);
        mPottyFab.setOnClickListener(this);
        mFeedFab.setOnClickListener(this);
        mLetOutFab.setOnClickListener(this);
        mPottyFabLabel.setOnClickListener(this);
        mFeedFabLabel.setOnClickListener(this);
        mLetOutFabLabel.setOnClickListener(this);

        mRecyclerView = mFragView.findViewById(R.id.tracker_items);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mFragView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        final EventAdapter adapter = new EventAdapter(mFragView);
        mRecyclerView.setAdapter(adapter);

        // listens to changes made to user's mPet list
        mDatabase.child("users").child(mUID).child("pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for (DataSnapshot d : data) {
                    pets.add(d.getKey());
                    // all mPet ids are the keys, values are just true so ignore those
                }
                if (mPetsListWasEmpty) {
                    mDatabase.child("pets").child(pets.get(0)).child("events").addChildEventListener(eventListener);
                    Log.d("PET", pets.get(0));
                    mPetsListWasEmpty = false;

                    mDatabase.child("pets").child(pets.get(0)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mPet = dataSnapshot.getValue(Pet.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        eventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TrackerEvent e = dataSnapshot.getValue(TrackerEvent.class);
                if (!adapter.contains(e)) {
                    adapter.addEvent(e);
                }
                Log.d("event type", e.getType().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.removeEvent(dataSnapshot.getValue(TrackerEvent.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteHelper(adapter, getContext()));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return mFragView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tracker_fab:
                if (mIsFabOpen) {
                    closeFab();
                } else {
                    openFab();
                }
                mIsFabOpen = !mIsFabOpen;
                break;
            case R.id.track_fed_fab:
                addEvent(TrackerEvent.EventType.FEED);
                break;
            case R.id.track_potty_fab:
                addEvent(TrackerEvent.EventType.POTTY);
                break;
            case R.id.track_let_out_fab:
                addEvent(TrackerEvent.EventType.WALK);
                break;
        }
    }

    private void addEvent(final TrackerEvent.EventType type) {

        View v = getLayoutInflater().inflate(R.layout.add_event_dialog, null);
        ((RadioButton) v.findViewById(R.id.pet0)).setText(mPet.getName());

        new AlertDialog.Builder(getContext())
            .setView(v)
            .setPositiveButton(R.string.save, (dialog, which) -> {
                AlertDialog d = (AlertDialog) dialog;
                String title = ((EditText) d.findViewById(R.id.event_title)).getText().toString();
                // pet
                RadioGroup radioGroup = d.findViewById(R.id.pet_radio_group);
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int petChosen = radioGroup.indexOfChild(radioButton);
                String petId = pets.get(petChosen);
                //note
                String note = ((EditText) d.findViewById(R.id.event_note)).getText().toString();
                //date
                Calendar c = Calendar.getInstance();
                String when = String.format(Locale.US, "%2d/%2d/%4d %2d:%02d %s",
                        c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                        c.get(Calendar.YEAR), c.get(Calendar.HOUR) == 0 ? 12 : c.get(Calendar.HOUR),
                        c.get(Calendar.MINUTE), c.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm");
                TrackerEvent e = new TrackerEvent(when, type, title, note);
                String id = mDatabase.child("pets").child(petId).child("events").push().getKey();
                e.setId(id);
                e.setPetId(petId);
                mDatabase.child("pets").child(petId).child("events").child(id).setValue(e);
            })
            .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
            .create()
            .show();
    }

    private void openFab() {
        mTrackerFab.startAnimation(mRotateForward);
        mLetOutFab.startAnimation(mMiniAppear);
        mPottyFab.startAnimation(mMiniAppear);
        mFeedFab.startAnimation(mMiniAppear);
        mPottyFabLabel.startAnimation(mLabelAppear);
        mFeedFabLabel.startAnimation(mLabelAppear);
        mLetOutFabLabel.startAnimation(mLabelAppear);

    }

    private void closeFab() {
        mTrackerFab.startAnimation(mRotateBackward);
        mLetOutFab.startAnimation(mMiniDisappear);
        mPottyFab.startAnimation(mMiniDisappear);
        mFeedFab.startAnimation(mMiniDisappear);
        mPottyFabLabel.startAnimation(mLabelDisappear);
        mFeedFabLabel.startAnimation(mLabelDisappear);
        mLetOutFabLabel.startAnimation(mLabelDisappear);
    }
}
