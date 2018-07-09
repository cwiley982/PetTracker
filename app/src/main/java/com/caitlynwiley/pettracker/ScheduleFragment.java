package com.caitlynwiley.pettracker;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment implements View.OnClickListener {

    private View mFragView;

    private Animation mRotateForward;
    private Animation mRotateBackward;
    private Animation mMiniAppear;
    private Animation mMiniDisappear;

    private FloatingActionButton mMainFab;
    private FloatingActionButton mWalkFab;
    private FloatingActionButton mFeedFab;
    private FloatingActionButton mSleepFab;

    private EditText mEventTitle;
    private EditText mStartTime;
    private EditText mEndTime;
    private EditText mNote;

    private boolean mIsFabOpen;

    private List<Event> mEvents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.schedule_fragment, container, false);
        mRotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_backward);

        mMiniAppear = AnimationUtils.loadAnimation(getContext(), R.anim.mini_fab_appear);
        mMiniAppear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mWalkFab.setVisibility(View.VISIBLE);
                mFeedFab.setVisibility(View.VISIBLE);
                mSleepFab.setVisibility(View.VISIBLE);
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
                mWalkFab.setVisibility(View.GONE);
                mFeedFab.setVisibility(View.GONE);
                mSleepFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mIsFabOpen = false;
        mMainFab = mFragView.findViewById(R.id.schedule_fab);
        mWalkFab = mFragView.findViewById(R.id.add_walk_fab);
        mFeedFab = mFragView.findViewById(R.id.add_feed_fab);
        mSleepFab = mFragView.findViewById(R.id.add_sleep_fab);
        mMainFab.setOnClickListener(this);
        mWalkFab.setOnClickListener(this);
        mFeedFab.setOnClickListener(this);
        mSleepFab.setOnClickListener(this);

        View dialogView = inflater.inflate(R.layout.add_activity_layout, container);
        mEventTitle = dialogView.findViewById(R.id.eventTitleText);
        mStartTime = dialogView.findViewById(R.id.startTime);
        mEndTime = dialogView.findViewById(R.id.endTime);
        mNote = dialogView.findViewById(R.id.noteText);

        mEvents = new ArrayList<>();
        return mFragView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.schedule_fab:
                if (mIsFabOpen) {
                    closeFab();
                } else {
                    openFab();
                }
                mIsFabOpen = !mIsFabOpen;
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
        mMainFab.startAnimation(mRotateForward);
        mSleepFab.startAnimation(mMiniAppear);
        mWalkFab.startAnimation(mMiniAppear);
        mFeedFab.startAnimation(mMiniAppear);
        // open and show all small fabs
    }

    private void closeFab() {
        mMainFab.startAnimation(mRotateBackward);
        mSleepFab.startAnimation(mMiniDisappear);
        mWalkFab.startAnimation(mMiniDisappear);
        mFeedFab.startAnimation(mMiniDisappear);
        // close and hide all small fabs
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openAddWindow(final int resId) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(R.layout.add_activity_layout)
                .setCancelable(true)
                .setNegativeButton("Cancel", null) //just closes the window by default
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // add event to schedule
                        Event.Type type = resId == R.drawable.ic_sleep_white_24dp ? Event.Type.SLEEP :
                                resId == R.drawable.ic_food_white_24dp ? Event.Type.FEED : Event.Type.WALK;
                        // get title and stuff then set it
                        mEvents.add(new Event(type, mEventTitle.getText().toString(), mStartTime.getText(), mEndTime.getText(), mNote.getText()));

                    }
                })
                .create();

        String title = "";
        switch (resId) {
            case R.drawable.ic_sun_white_24dp:
                title = "Create your walk";
                break;
            case R.drawable.ic_sleep_white_24dp:
                title = "Add your sleep time";
                break;
            case R.drawable.ic_food_white_24dp:
                title = "Add your feeding time";
                break;
        }
        alertDialog.setTitle(title);

        Drawable icon = getResources().getDrawable(resId);
        icon.setTint(getResources().getColor(R.color.secondaryColor));
        alertDialog.setIcon(icon);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.secondaryDarkColor));
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.secondaryDarkColor));
            }
        });

        alertDialog.show();

        // TODO: need options for am/pm in custom layout
        // TODO: use a spinner for times instead of allowing users to type it in (so many possible errors...)
    }
}
