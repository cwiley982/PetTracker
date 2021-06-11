package com.caitlynwiley.pettracker.fragments

import android.annotation.TargetApi
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.models.ScheduleEvent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class ScheduleFragment : Fragment(), View.OnClickListener, OnItemSelectedListener {
    private lateinit var mFragView: View
    private lateinit var mRotateForward: Animation
    private lateinit var mRotateBackward: Animation
    private lateinit var mMiniAppear: Animation
    private lateinit var mMiniDisappear: Animation
    private lateinit var mMainFab: FloatingActionButton
    private lateinit var mWalkFab: FloatingActionButton
    private lateinit var mFeedFab: FloatingActionButton
    private lateinit var mSleepFab: FloatingActionButton
    private var mEventTitle: EditText? = null
    private var mStartHourSpinner: Spinner? = null
    private var mStartMinutesSpinner: Spinner? = null
    private var mEndHourSpinner: Spinner? = null
    private var mEndMinutesSpinner: Spinner? = null
    private var mNote: EditText? = null
    private var mIsFabOpen = false
    private var mEvents: MutableList<ScheduleEvent>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        mFragView = inflater.inflate(R.layout.schedule_fragment, container, false)
        mRotateForward = AnimationUtils.loadAnimation(context, R.anim.fab_spin_forward)
        mRotateBackward = AnimationUtils.loadAnimation(context, R.anim.fab_spin_backward)
        mMiniAppear = AnimationUtils.loadAnimation(context, R.anim.mini_fab_appear)
        mMiniAppear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mWalkFab.visibility = View.VISIBLE
                mFeedFab.visibility = View.VISIBLE
                mSleepFab.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mMiniDisappear = AnimationUtils.loadAnimation(context, R.anim.mini_fab_disappear)
        mMiniDisappear.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mWalkFab.visibility = View.GONE
                mFeedFab.visibility = View.GONE
                mSleepFab.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mIsFabOpen = false
        mMainFab = mFragView.findViewById(R.id.schedule_fab)
        mWalkFab = mFragView.findViewById(R.id.add_walk_fab)
        mFeedFab = mFragView.findViewById(R.id.add_feed_fab)
        mSleepFab = mFragView.findViewById(R.id.add_sleep_fab)
        mMainFab.setOnClickListener(this)
        mWalkFab.setOnClickListener(this)
        mFeedFab.setOnClickListener(this)
        mSleepFab.setOnClickListener(this)
        mEvents = ArrayList()
        return mFragView
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.schedule_fab -> {
                if (mIsFabOpen) {
                    closeFab()
                } else {
                    openFab()
                }
                mIsFabOpen = !mIsFabOpen
            }
            R.id.add_walk_fab -> openAddWindow(R.drawable.ic_sun_white_24dp)
            R.id.add_feed_fab -> openAddWindow(R.drawable.ic_food_white_24dp)
            R.id.add_sleep_fab -> openAddWindow(R.drawable.ic_sleep_white_24dp)
        }
    }

    private fun openFab() {
        mMainFab.startAnimation(mRotateForward)
        mSleepFab.startAnimation(mMiniAppear)
        mWalkFab.startAnimation(mMiniAppear)
        mFeedFab.startAnimation(mMiniAppear)
        // open and show all small fabs
    }

    private fun closeFab() {
        mMainFab.startAnimation(mRotateBackward)
        mSleepFab.startAnimation(mMiniDisappear)
        mWalkFab.startAnimation(mMiniDisappear)
        mFeedFab.startAnimation(mMiniDisappear)
        // close and hide all small fabs
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun openAddWindow(resId: Int) {
        val v = View.inflate(context, R.layout.add_activity_layout, null)
        mEventTitle = v.findViewById(R.id.eventTitleText)
        mNote = v.findViewById(R.id.noteText)
        mStartHourSpinner = v.findViewById(R.id.startTimeHour)
        mStartMinutesSpinner = v.findViewById(R.id.startTimeMinutes)
        mEndHourSpinner = v.findViewById(R.id.endTimeHour)
        mEndMinutesSpinner = v.findViewById(R.id.endTimeMinutes)
        val hourAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.hours, R.layout.spinner_item)
        hourAdapter.setDropDownViewResource(R.layout.dropdown_spinner_item)
        val minutesAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.minutes, R.layout.spinner_item)
        minutesAdapter.setDropDownViewResource(R.layout.dropdown_spinner_item)
        mStartHourSpinner?.adapter = hourAdapter
        mStartMinutesSpinner?.adapter = minutesAdapter
        mEndHourSpinner?.adapter = hourAdapter
        mEndMinutesSpinner?.adapter = minutesAdapter
        val alertDialog = AlertDialog.Builder(requireContext())
                .setView(v)
                .setCancelable(true)
                .setNegativeButton("Cancel", null) //just closes the window by default
                .setPositiveButton("Add") { _: DialogInterface?, _: Int ->
                    // add event to schedule
                    val type = if (resId == R.drawable.ic_sleep_white_24dp) ScheduleEvent.Type.SLEEP else if (resId == R.drawable.ic_food_white_24dp) ScheduleEvent.Type.FEED else ScheduleEvent.Type.WALK
                    // get title and stuff then set it
                    val event = ScheduleEvent(type, mEventTitle?.text.toString(), mNote?.text.toString())
                    event.setStartTime(mStartHourSpinner!!, mStartMinutesSpinner!!)
                    event.setEndTime(mEndHourSpinner!!, mEndMinutesSpinner!!)
                    mEvents!!.add(event)
                }
                .create()
        var title = ""
        when (resId) {
            R.drawable.ic_sun_white_24dp -> title = "Create your walk"
            R.drawable.ic_sleep_white_24dp -> title = "Add your sleep time"
            R.drawable.ic_food_white_24dp -> title = "Add your feeding time"
        }
        alertDialog.setTitle(title)
        val icon = ResourcesCompat.getDrawable(resources, resId, null)
        icon?.setTint(resources.getColor(R.color.accentColorLight, null))
        alertDialog.setIcon(icon)
        alertDialog.setOnShowListener {
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.accentColorLight, null))
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.accentColorLight, null))
        }
        alertDialog.show()

        // TODO: need options for am/pm in custom layout
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        // TODO: Keep track of what they've chosen, because I don't know how to get it once dialog is closed
        if (view == mStartHourSpinner) {
            Log.d("Spinner", "Start hour spinner expected and is actual")
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TODO: Set selection for corresponding spinner to null
    }
}