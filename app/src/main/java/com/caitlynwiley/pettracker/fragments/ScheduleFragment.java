package com.caitlynwiley.pettracker.fragments;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.caitlynwiley.pettracker.R;
import com.caitlynwiley.pettracker.models.ScheduleEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ScheduleFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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
    private Spinner mStartHourSpinner;
    private Spinner mStartMinutesSpinner;
    private Spinner mEndHourSpinner;
    private Spinner mEndMinutesSpinner;
    private EditText mNote;

    private boolean mIsFabOpen;

    private List<ScheduleEvent> mEvents;

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
        View v = View.inflate(getContext(), R.layout.add_activity_layout, null);
        mEventTitle = v.findViewById(R.id.eventTitleText);
        mNote = v.findViewById(R.id.noteText);
        mStartHourSpinner = v.findViewById(R.id.startTimeHour);
        mStartMinutesSpinner = v.findViewById(R.id.startTimeMinutes);
        mEndHourSpinner = v.findViewById(R.id.endTimeHour);
        mEndMinutesSpinner = v.findViewById(R.id.endTimeMinutes);

        ArrayAdapter<CharSequence> hourAdapter = ArrayAdapter.createFromResource(getContext(), R.array.hours, R.layout.spinner_item);
        hourAdapter.setDropDownViewResource(R.layout.dropdown_spinner_item);
        ArrayAdapter<CharSequence> minutesAdapter = ArrayAdapter.createFromResource(getContext(), R.array.minutes, R.layout.spinner_item);
        minutesAdapter.setDropDownViewResource(R.layout.dropdown_spinner_item);

        mStartHourSpinner.setAdapter(hourAdapter);
        mStartMinutesSpinner.setAdapter(minutesAdapter);
        mEndHourSpinner.setAdapter(hourAdapter);
        mEndMinutesSpinner.setAdapter(minutesAdapter);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(v)
                .setCancelable(true)
                .setNegativeButton("Cancel", null) //just closes the window by default
                .setPositiveButton("Add", (dialog, which) -> {
                    // add event to schedule
                    ScheduleEvent.Type type = resId == R.drawable.ic_sleep_white_24dp ? ScheduleEvent.Type.SLEEP :
                            resId == R.drawable.ic_food_white_24dp ? ScheduleEvent.Type.FEED : ScheduleEvent.Type.WALK;
                    // get title and stuff then set it
                    ScheduleEvent event = new ScheduleEvent(type, mEventTitle.getText().toString(), mNote.getText());
                    event.setStartTime(mStartHourSpinner, mStartMinutesSpinner);
                    event.setEndTime(mEndHourSpinner, mEndMinutesSpinner);
                    mEvents.add(event);
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
        icon.setTint(getResources().getColor(R.color.accentColorLight));
        alertDialog.setIcon(icon);

        alertDialog.setOnShowListener(dialog -> {
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.accentColorLight));
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.accentColorLight));
        });

        alertDialog.show();

        // TODO: need options for am/pm in custom layout
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Keep track of what they've chosen, because I don't know how to get it once dialog is closed
        if (view.equals(mStartHourSpinner)) {
            Log.d("Spinner", "Start hour spinner expected and is actual");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO: Set selection for corresponding spinner to null
    }
}
