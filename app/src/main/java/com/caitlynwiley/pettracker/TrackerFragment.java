package com.caitlynwiley.pettracker;

<<<<<<< HEAD
=======
import android.app.Activity;
import android.app.AlertDialog;
>>>>>>> develop
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class TrackerFragment extends Fragment implements View.OnClickListener {

    private View mFragView;

    private Animation mRotateForward;
    private Animation mRotateBackward;
    private Animation mMiniAppear;
    private Animation mMiniDisappear;

    private FloatingActionButton mTrackerFab;
    private FloatingActionButton mPoopFab;
    private FloatingActionButton mFeedFab;
    private FloatingActionButton mLetOutFab;
    private ListView mListView;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<TrackerEvent> events = new ArrayList<>();
    private ArrayList<String> pets = new ArrayList<>();
    private String mUID;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private AppCompatActivity parentActivity;

    private boolean mIsFabOpen;
    private boolean mPetsListWasEmpty = true;

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
                mPoopFab.show();
                mFeedFab.show();
                mLetOutFab.show();
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
                mPoopFab.hide();
                mLetOutFab.hide();
                mFeedFab.hide();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mIsFabOpen = false;
        // get views
        mTrackerFab = mFragView.findViewById(R.id.tracker_fab);
        mPoopFab = mFragView.findViewById(R.id.track_poop_fab);
        mFeedFab = mFragView.findViewById(R.id.track_fed_fab);
        mLetOutFab = mFragView.findViewById(R.id.track_let_out_fab);

        // set on click listeners
        mTrackerFab.setOnClickListener(this);
        mPoopFab.setOnClickListener(this);
        mFeedFab.setOnClickListener(this);
        mLetOutFab.setOnClickListener(this);

        mListView = mFragView.findViewById(R.id.tracker_items);
        final EventAdapter adapter = new EventAdapter();
        mListView.setAdapter(adapter);

        // listens to changes made to user's pet list
        mDatabase.child("users").child(mUID).child("pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for (DataSnapshot d : data) {
                    pets.add(d.getKey());
                    // all pet ids are the keys, values are just true so ignore those
                }
                if (mPetsListWasEmpty) {
                    mDatabase.child("pets").child(pets.get(0)).child("events").addChildEventListener(eventListener);
                    Log.d("PET", pets.get(0));
                    mPetsListWasEmpty = false;
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
                events.add(e);
                Log.d("event type", e.getType().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                events.remove(dataSnapshot.getValue(TrackerEvent.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

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
            case R.id.track_poop_fab:
                addEvent(TrackerEvent.EventType.POOP);
                break;
            case R.id.track_let_out_fab:
                addEvent(TrackerEvent.EventType.WALK);
                break;
        }
    }

    private void addEvent(final TrackerEvent.EventType type) {
        new AlertDialog.Builder(getContext())
            .setView(R.layout.add_event_dialog)
            .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // save the event...
                    AlertDialog d = (AlertDialog) dialog;
                    String title = ((EditText) d.findViewById(R.id.event_title)).getText().toString();
                    RadioGroup radioGroup = d.findViewById(R.id.pet_radio_group);
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int petChosen = radioGroup.indexOfChild(radioButton);
                    String petId = pets.get(petChosen);
                    String note = ((EditText) d.findViewById(R.id.event_note)).getText().toString();
                    Calendar c = Calendar.getInstance();
                    String when = String.format(Locale.US, "%2d/%2d/%4d %2d:%2d %s",
                            c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                            c.get(Calendar.YEAR), c.get(Calendar.HOUR) == 0 ? 12 : c.get(Calendar.HOUR),
                            c.get(Calendar.MINUTE), c.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm");
                    TrackerEvent e = new TrackerEvent(when, type, title, note);
                    mDatabase.child("pets").child(petId).child("events").push().setValue(e);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .create()
            .show();
    }

    private void openFab() {
        mTrackerFab.startAnimation(mRotateForward);
        mLetOutFab.startAnimation(mMiniAppear);
        mPoopFab.startAnimation(mMiniAppear);
        mFeedFab.startAnimation(mMiniAppear);
        // open and show all small fabs
    }

    private void closeFab() {
        mTrackerFab.startAnimation(mRotateBackward);
        mLetOutFab.startAnimation(mMiniDisappear);
        mPoopFab.startAnimation(mMiniDisappear);
        mFeedFab.startAnimation(mMiniDisappear);
        // close and hide all small fabs
    }

    class EventAdapter extends BaseAdapter {

        private TextView textView;
        private ImageView imageView;

        @Override
        public int getCount() {
            return events == null ? 0 : events.size();
        }

        @Override
        public Object getItem(int i) {
            return events == null ? null : events.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View event = getLayoutInflater().inflate(R.layout.tracker_event, viewGroup, false);
            textView = event.findViewById(R.id.time_text);
            textView.setText(((TrackerEvent) getItem(i)).getTime());
            imageView = event.findViewById(R.id.event_icon);
            imageView.setImageResource(((TrackerEvent) getItem(i)).getDrawableResId());
            return event;
        }
    }
}
