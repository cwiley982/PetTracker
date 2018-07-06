package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ScheduleFragment extends Fragment implements View.OnClickListener {

    private View mFragView;
    private Animation mRotateForward;
    private Animation mRotateBackward;
    private Animation mMiniAppear;
    private Animation mMiniDisappear;
    private FloatingActionButton mainFab;
    private FloatingActionButton addWalkFab;
    private FloatingActionButton addFeedFab;
    private FloatingActionButton addSleepFab;
    private boolean fabOpen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.schedule_fragment, container, false);
        mRotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_backward);
        mMiniAppear = AnimationUtils.loadAnimation(getContext(), R.anim.mini_fab_appear);
        mMiniDisappear = AnimationUtils.loadAnimation(getContext(), R.anim.mini_fab_disappear);

        mMiniAppear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addWalkFab.setVisibility(View.VISIBLE);
                addFeedFab.setVisibility(View.VISIBLE);
                addSleepFab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mMiniDisappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addWalkFab.setVisibility(View.GONE);
                addFeedFab.setVisibility(View.GONE);
                addSleepFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mainFab = mFragView.findViewById(R.id.schedule_fab);
        addWalkFab = mFragView.findViewById(R.id.add_walk_fab);
        addFeedFab = mFragView.findViewById(R.id.add_feed_fab);
        addSleepFab = mFragView.findViewById(R.id.add_sleep_fab);
        mainFab.setOnClickListener(this);
        addWalkFab.setOnClickListener(this);
        addFeedFab.setOnClickListener(this);
        addSleepFab.setOnClickListener(this);
        fabOpen = false;
        return mFragView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.schedule_fab:
                if (fabOpen) {
                    closeFab();
                } else {
                    openFab();
                }
                fabOpen = !fabOpen;
                // Maybe just add a new row to the list that they can type in
                break;
            case R.id.add_walk_fab:
                openAddWindow(R.drawable.ic_sun_white_24dp);
                break;
            case R.id.add_feed_fab:
                openAddWindow(R.drawable.ic_food_white_24dp);
                break;
            case R.id.add_sleep_fab:
                openAddWindow(R.drawable.ic_sleep_white_24dp);
                break;
        }
    }

    private void openFab() {
        mainFab.startAnimation(mRotateForward);
        addSleepFab.startAnimation(mMiniAppear);
        addWalkFab.startAnimation(mMiniAppear);
        addFeedFab.startAnimation(mMiniAppear);
        // open and show all small fabs
    }

    private void closeFab() {
        mainFab.startAnimation(mRotateBackward);
        addSleepFab.startAnimation(mMiniDisappear);
        addWalkFab.startAnimation(mMiniDisappear);
        addFeedFab.startAnimation(mMiniDisappear);
        // close and hide all small fabs
    }

    private void openAddWindow(int resId) {
        // need to have a time chooser for start/end times
        // box to name the event
        // cancel and okay buttons
        // show icon passed into this method
        // use an alert dialog with a custom layout, need a class extending DialogFragment though
    }
}
