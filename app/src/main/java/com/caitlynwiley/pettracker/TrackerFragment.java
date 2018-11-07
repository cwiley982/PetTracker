package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Map;

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
    private Map<String, Event> events;
    private String mUsername;

    private boolean mIsFabOpen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.tracker_fragment, container, false);
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mIsFabOpen = false;
        mTrackerFab = mFragView.findViewById(R.id.schedule_fab);
        mPoopFab = mFragView.findViewById(R.id.add_walk_fab);
        mFeedFab = mFragView.findViewById(R.id.add_feed_fab);
        mLetOutFab = mFragView.findViewById(R.id.add_sleep_fab);
        mTrackerFab.setOnClickListener(this);
        mPoopFab.setOnClickListener(this);
        mFeedFab.setOnClickListener(this);
        mLetOutFab.setOnClickListener(this);

        mListView = mFragView.findViewById(R.id.tracker_items);
        final EventAdapter adapter = new EventAdapter();
        mListView.setAdapter(adapter);
        mUsername = getActivity().getIntent().getStringExtra("USERNAME");
        mDatabase.child("users").child(mUsername).child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events = (Map) dataSnapshot.getValue();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TrackerFrag", "Error loading events");
            }
        });


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

    private void addEvent(TrackerEvent.EventType type) {
        TrackerEvent e = new TrackerEvent(Calendar.getInstance(), type);
        mDatabase.child("users").child(mUsername).child("events").push().setValue(e);
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
            return events.keySet().size();
        }

        @Override
        public Object getItem(int i) {
            return events.get((String) events.keySet().toArray()[i]);
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
