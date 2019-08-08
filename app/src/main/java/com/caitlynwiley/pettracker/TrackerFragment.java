package com.caitlynwiley.pettracker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.caitlynwiley.pettracker.models.Day;
import com.caitlynwiley.pettracker.models.Pet;
import com.caitlynwiley.pettracker.models.TrackerEvent;
import com.caitlynwiley.pettracker.models.TrackerItem;
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
import java.util.TimeZone;

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
    private EventAdapter mAdapter;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
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

        RecyclerView recyclerView = mFragView.findViewById(R.id.tracker_items);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(mFragView.getContext()));

        // specify an adapter
        mAdapter = new EventAdapter(mFragView);
        recyclerView.setAdapter(mAdapter);

        // listens to changes made to user's mPet list
        mDatabase.child("users").child(mUID).child("pets").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                pets.add(dataSnapshot.getKey());
                if (mPetsListWasEmpty) {
                    mDatabase.child("pets").child(pets.get(0)).child("events").addChildEventListener(eventListener);
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
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                pets.remove(dataSnapshot.getKey());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        eventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TrackerItem item;
                if (dataSnapshot.child("itemType").getValue(String.class).equals("event")) {
                    item = dataSnapshot.getValue(TrackerEvent.class);
                    Log.d("fragment", "event");
                } else {
                    item = dataSnapshot.getValue(Day.class);
                    ((Day) item).setContext(getContext());
                    Log.d("fragment", "day");
                }
                if (mAdapter.getItemCount() == 0 && item instanceof TrackerEvent) {
                    // need to insert a day item before the event
                    TrackerEvent event = (TrackerEvent) item;
                    Day d = new Day(getContext(), event.getDate());
                    String id = mDatabase.child("pets").child(pets.get(0)).child("events").push().getKey();
                    d.setId(id);
                    Calendar c = Calendar.getInstance();
                    c.set(event.getYear(), event.getMonth() - 1, event.getDay(), 0, 0, 0);
                    c.set(Calendar.MILLISECOND, 0);
                    d.setUtcMillis(c.getTimeInMillis() - c.getTimeZone().getOffset(c.getTimeInMillis()));
                    mDatabase.child("pets").child(pets.get(0)).child("events").child(id).setValue(d);
                }
                mAdapter.addItem(item);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                mAdapter.removeEvent(dataSnapshot.getValue(TrackerItem.class));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteHelper(mAdapter, getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);

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

        switch (type) {
            case FEED:
                trackFeed();
                break;
            case WALK:
                trackWalk();
                break;
            case POTTY:
                trackPotty();
                break;
        }
    }

    private void trackPotty() {
        View v = getLayoutInflater().inflate(R.layout.track_potty_dialog, null);
        ((RadioButton) v.findViewById(R.id.pet0)).setText(mPet.getName());
        final Context context = this.getActivity().getApplicationContext();

        new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    AlertDialog d = (AlertDialog) dialog;
                    // pet
                    RadioGroup radioGroup = d.findViewById(R.id.pet_radio_group);
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int petChosen = radioGroup.indexOfChild(radioButton);
                    String petId = pets.get(petChosen);
                    // type
                    boolean num1 = ((CheckBox) d.findViewById(R.id.number1)).isChecked();
                    boolean num2 = ((CheckBox) d.findViewById(R.id.number2)).isChecked();
                    //date
                    Calendar c = Calendar.getInstance();
                    String date = String.format(Locale.US, "%2d/%2d/%4d",
                            c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                            c.get(Calendar.YEAR));
                    if (!mAdapter.getMostRecentDate().equals(date)) {
                        addDayToList(context, c, petId, date);
                    }
                    String id = mDatabase.child("pets").child(petId).child("events").push().getKey();
                    TrackerEvent e = new TrackerEvent.Builder(TrackerEvent.EventType.POTTY)
                            .setDate(date)
                            .setMillis(c.getTimeInMillis() - c.getTimeZone().getOffset(c.getTimeInMillis()))
                            .setNumber1(num1)
                            .setNumber2(num2)
                            .setPetId(petId)
                            .setId(id)
                            .build();
                    mDatabase.child("pets").child(petId).child("events").child(id).setValue(e);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    private void trackWalk() {
        View v = getLayoutInflater().inflate(R.layout.track_walk_dialog, null);
        ((RadioButton) v.findViewById(R.id.pet0)).setText(mPet.getName());
        final Context context = this.getActivity().getApplicationContext();

        new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    AlertDialog d = (AlertDialog) dialog;
                    // pet
                    RadioGroup radioGroup = d.findViewById(R.id.pet_radio_group);
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int petChosen = radioGroup.indexOfChild(radioButton);
                    String petId = pets.get(petChosen);
                    // duration
                    int hours = Integer.parseInt(((EditText) d.findViewById(R.id.walk_duration_hours))
                            .getText().toString());
                    int mins = Integer.parseInt(((EditText) d.findViewById(R.id.walk_duration_mins))
                            .getText().toString());
                    //date
                    Calendar c = Calendar.getInstance();
                    String date = String.format(Locale.US, "%2d/%2d/%4d",
                            c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                            c.get(Calendar.YEAR));
                    if (!mAdapter.getMostRecentDate().equals(date)) {
                        addDayToList(context, c, petId, date);
                    }
                    String id = mDatabase.child("pets").child(petId).child("events").push().getKey();
                    TrackerEvent e = new TrackerEvent.Builder(TrackerEvent.EventType.WALK)
                            .setDate(date)
                            .setMillis(c.getTimeInMillis() - c.getTimeZone().getOffset(c.getTimeInMillis()))
                            .setWalkLength(hours, mins)
                            .setPetId(petId)
                            .setId(id)
                            .build();
                    mDatabase.child("pets").child(petId).child("events").child(id).setValue(e);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    private void trackFeed() {
        View v = getLayoutInflater().inflate(R.layout.track_meal_dialog, null);
        ((RadioButton) v.findViewById(R.id.pet0)).setText(mPet.getName());
        final Context context = this.getActivity().getApplicationContext();

        new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    AlertDialog d = (AlertDialog) dialog;
                    // pet
                    RadioGroup radioGroup = d.findViewById(R.id.pet_radio_group);
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int petChosen = radioGroup.indexOfChild(radioButton);
                    String petId = pets.get(petChosen);
                    // amount
                    double cupsFood = Double.parseDouble(((EditText) d.findViewById(R.id.num_cups))
                            .getText().toString());
                    //date
                    Calendar c = Calendar.getInstance();
                    String date = String.format(Locale.US, "%2d/%2d/%4d",
                            c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                            c.get(Calendar.YEAR));
                    if (!mAdapter.getMostRecentDate().equals(date)) {
                        addDayToList(context, c, petId, date);
                    }
                    String id = mDatabase.child("pets").child(petId).child("events").push().getKey();
                    TrackerEvent e = new TrackerEvent.Builder(TrackerEvent.EventType.FEED)
                            .setDate(date)
                            .setMillis(c.getTimeInMillis() - c.getTimeZone().getOffset(c.getTimeInMillis()))
                            .setCupsFood(cupsFood)
                            .setPetId(petId)
                            .setId(id)
                            .build();
                    mDatabase.child("pets").child(petId).child("events").child(id).setValue(e);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }

    private void addDayToList(Context context, Calendar c, String petId, String date) {
        Day day = new Day(context, date);
        String dayId = mDatabase.child("pets").child(petId).child("events").push().getKey();
        day.setId(dayId);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        day.setUtcMillis(c.getTimeInMillis());
        mDatabase.child("pets").child(petId).child("events").child(dayId).setValue(day);
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
