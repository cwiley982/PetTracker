package com.caitlynwiley.pettracker.views.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.caitlynwiley.pettracker.EventAdapter
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.SwipeToDeleteHelper
import com.caitlynwiley.pettracker.models.TrackerItem
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.caitlynwiley.pettracker.viewmodel.TrackerViewModel
import com.caitlynwiley.pettracker.viewmodel.TrackerViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.apache.commons.text.RandomStringGenerator
import java.util.*

class TrackerFragment : Fragment(), View.OnClickListener {
    private lateinit var mFragView: View

    private val viewModel by viewModels<TrackerViewModel>(
        factoryProducer = { TrackerViewModelFactory(PetTrackerRepository(), "") }
    )

    private lateinit var mRotateForward: Animation
    private lateinit var mRotateBackward: Animation
    private lateinit var mMiniAppear: Animation
    private lateinit var mMiniDisappear: Animation
    private lateinit var mLabelAppear: Animation
    private lateinit var mLabelDisappear: Animation

    private lateinit var mPottyFabLabel: TextView
    private lateinit var mFeedFabLabel: TextView
    private lateinit var mLetOutFabLabel: TextView
    private lateinit var mTrackerFab: FloatingActionButton
    private lateinit var mPottyFab: FloatingActionButton
    private lateinit var mFeedFab: FloatingActionButton
    private lateinit var mLetOutFab: FloatingActionButton

    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var mAdapter: EventAdapter
    private var mIsFabOpen = false

    private val randomStringGenerator: RandomStringGenerator =
        RandomStringGenerator.Builder().withinRange(charArrayOf('0', '9'), charArrayOf('a', 'z'),
            charArrayOf('A', 'Z')).build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        mFragView = inflater.inflate(R.layout.tracker_fragment, container, false)

        mRotateForward = AnimationUtils.loadAnimation(context, R.anim.fab_spin_forward)
        mRotateBackward = AnimationUtils.loadAnimation(context, R.anim.fab_spin_backward)
        mMiniAppear = AnimationUtils.loadAnimation(context, R.anim.mini_fab_appear)
        mMiniDisappear = AnimationUtils.loadAnimation(context, R.anim.mini_fab_disappear)
        mLabelAppear = AnimationUtils.loadAnimation(context, R.anim.fab_label_appear)
        mLabelDisappear = AnimationUtils.loadAnimation(context, R.anim.fab_label_disappear)

        mRotateForward.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mPottyFab.visibility = View.VISIBLE
                mLetOutFab.visibility = View.VISIBLE
                mFeedFab.visibility = View.VISIBLE
                mPottyFabLabel.visibility = View.VISIBLE
                mLetOutFabLabel.visibility = View.VISIBLE
                mFeedFabLabel.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        mRotateBackward.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mPottyFab.visibility = View.GONE
                mLetOutFab.visibility = View.GONE
                mFeedFab.visibility = View.GONE
                mPottyFabLabel.visibility = View.GONE
                mLetOutFabLabel.visibility = View.GONE
                mFeedFabLabel.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        mIsFabOpen = false

        // get views
        mTrackerFab = mFragView.findViewById(R.id.tracker_fab)
        mPottyFab = mFragView.findViewById(R.id.track_potty_fab)
        mFeedFab = mFragView.findViewById(R.id.track_fed_fab)
        mLetOutFab = mFragView.findViewById(R.id.track_let_out_fab)
        mPottyFabLabel = mFragView.findViewById(R.id.potty_fab_label)
        mFeedFabLabel = mFragView.findViewById(R.id.fed_fab_label)
        mLetOutFabLabel = mFragView.findViewById(R.id.let_out_fab_label)
        mSwipeRefreshLayout = mFragView.findViewById(R.id.swipe_refresh_view)

        // set on click listeners
        mTrackerFab.setOnClickListener(this)
        mPottyFab.setOnClickListener(this)
        mFeedFab.setOnClickListener(this)
        mLetOutFab.setOnClickListener(this)
        mPottyFabLabel.setOnClickListener(this)
        mFeedFabLabel.setOnClickListener(this)
        mLetOutFabLabel.setOnClickListener(this)

        // setup swipe-to-refresh logic
        mSwipeRefreshLayout?.setOnRefreshListener {
            mSwipeRefreshLayout?.isRefreshing = true
            viewModel.forceRefreshEvents()
        }

        val recyclerView: RecyclerView = mFragView.findViewById(R.id.tracker_items)
        recyclerView.layoutManager = LinearLayoutManager(mFragView.context)

        // specify an adapter
        mAdapter = EventAdapter(mFragView, viewModel)
        recyclerView.adapter = mAdapter
        viewModel.trackerItems.observe(viewLifecycleOwner) {
            mAdapter.setItems(it)
            Log.d("count", "" + mAdapter.itemCount)
            mSwipeRefreshLayout?.isRefreshing = false
        }

        viewModel.emptyLabelVisible.observe(viewLifecycleOwner) {
            Log.d("TrackerFragment", "changing visibility of 'no events' label to " +
                    "-${if (it) View.VISIBLE else View.GONE}-")
            mFragView.findViewById<View>(R.id.no_events_label).visibility =
                if (it) View.VISIBLE else View.GONE
        }

        // setup swipe-to-delete and undo logic
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteHelper(mAdapter, requireContext()))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return mFragView
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tracker_fab -> if (mIsFabOpen) {
                closeFab()
            } else {
                openFab()
            }
            R.id.track_fed_fab -> {
                closeFab()
                addEvent(TrackerItem.EventType.FEED)
            }
            R.id.track_potty_fab -> {
                closeFab()
                addEvent(TrackerItem.EventType.POTTY)
            }
            R.id.track_let_out_fab -> {
                closeFab()
                addEvent(TrackerItem.EventType.WALK)
            }
        }
    }

    private fun addEvent(type: TrackerItem.EventType) {
        when (type) {
            TrackerItem.EventType.FEED -> trackFeed()
            TrackerItem.EventType.WALK -> trackWalk()
            TrackerItem.EventType.POTTY -> trackPotty()
        }
    }

    private fun trackPotty() {
        val v = layoutInflater.inflate(R.layout.track_potty_dialog, null)
        //((RadioButton) v.findViewById(R.id.pet0)).setText("Techs");
        AlertDialog.Builder(requireContext())
            .setView(v)
            .setPositiveButton(R.string.save) { dialog: DialogInterface, _: Int ->
                val d = dialog as AlertDialog
                // pet
                /* For multipet support in future
                        RadioGroup radioGroup = d.findViewById(R.id.pet_radio_group);
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(radioButtonID);
                        int petChosen = radioGroup.indexOfChild(radioButton);
                    String petId = mPetId;
                    */
                // type
                val num1 = (d.findViewById<View>(R.id.number1) as CheckBox?)!!.isChecked
                val num2 = (d.findViewById<View>(R.id.number2) as CheckBox?)!!.isChecked
                //date
                val c = Calendar.getInstance()
                val date = String.format(
                    Locale.US, "%02d/%02d/%4d",
                    c[Calendar.MONTH] + 1, c[Calendar.DAY_OF_MONTH],
                    c[Calendar.YEAR]
                )
                if (mAdapter.mostRecentDate != date) {
                    addDayToList(c, viewModel.pet.value?.id ?: "", date)
                }
                val e = TrackerItem.Builder()
                    .setEventType(TrackerItem.EventType.POTTY)
                    .setDate(date)
                    .setMillis(System.currentTimeMillis())
                    .setNumber1(num1)
                    .setNumber2(num2)
                    .setPetId(viewModel.pet.value?.id ?: "")
                    .setItemType("event")
                    .setId(randomStringGenerator.generate(30))
                    .build()
                viewModel.addTrackerItem(e)
            }
            .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.cancel() }
            .create()
            .show()
    }

    private fun trackWalk() {
        val v = layoutInflater.inflate(R.layout.track_walk_dialog, null)
        //((RadioButton) v.findViewById(R.id.pet0)).setText(mPet.getName());
        AlertDialog.Builder(requireContext())
            .setView(v)
            .setPositiveButton(R.string.save) { dialog: DialogInterface, _: Int ->
                val d = dialog as AlertDialog
                // pet
                /* For multipet support in future
                        RadioGroup radioGroup = d.findViewById(R.id.pet_radio_group);
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(radioButtonID);
                        int petChosen = radioGroup.indexOfChild(radioButton);
                        String petId = mPetId;
                    */
                // duration
                val hours = (d.findViewById<View>(R.id.walk_duration_hours) as EditText?)
                    ?.text.toString().toInt()
                val mins = (d.findViewById<View>(R.id.walk_duration_mins) as EditText?)
                    ?.text.toString().toInt()
                //date
                val c = Calendar.getInstance()
                val date = String.format(
                    Locale.US, "%02d/%02d/%4d",
                    c[Calendar.MONTH] + 1, c[Calendar.DAY_OF_MONTH],
                    c[Calendar.YEAR]
                )
                if (mAdapter.mostRecentDate != date) {
                    addDayToList(c, viewModel.pet.value?.id ?: "", date)
                }
                val e = TrackerItem.Builder()
                    .setEventType(TrackerItem.EventType.WALK)
                    .setDate(date)
                    .setMillis(System.currentTimeMillis())
                    .setWalkLength(hours, mins)
                    .setPetId(viewModel.pet.value?.id ?: "")
                    .setItemType("event")
                    .setId(randomStringGenerator.generate(30))
                    .build()
                viewModel.addTrackerItem(e)
            }
            .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.cancel() }
            .create()
            .show()
    }

    private fun trackFeed() {
        val v = layoutInflater.inflate(R.layout.track_meal_dialog, null)
        v.findViewById<View>(R.id.error_msg).visibility = View.GONE
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(v)
            .setPositiveButton(R.string.save, null) // set below
            .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.cancel() }
            .create()
        alertDialog.show()
        /*
            IMPORTANT: This listener has to be set after creating the alert dialog so that it can
            reference it to find the error message view, otherwise it can't find it. Also, the
            onClick listener here doesn't make the button default to dismissing the dialog, so when
            the input is invalid it won't automatically dismiss the dialog so the error message can
            actually be seen.
         */
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val cups = alertDialog.findViewById<EditText>(R.id.num_cups)
            if (cups!!.text.isEmpty() || cups.text.toString() == "0") {
                // stay open and show error message
                alertDialog.findViewById<View>(R.id.error_msg)!!.visibility = View.VISIBLE
            } else {
                Log.d("TrackerFragment", "positive button clicked")
                // pet
                /* For multipet support in future
                        RadioGroup radioGroup = d.findViewById(R.id.pet_radio_group);
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(radioButtonID);
                        int petChosen = radioGroup.indexOfChild(radioButton);
                        String petId = mPetId;
                    */
                // amount
                val cupsFood = cups.text.toString().toDouble()
                //date
                val c = Calendar.getInstance()
                val date = String.format(
                    Locale.US, "%02d/%02d/%4d",
                    c[Calendar.MONTH] + 1, c[Calendar.DAY_OF_MONTH],
                    c[Calendar.YEAR]
                )
                if (mAdapter.mostRecentDate != date) {
                    addDayToList(c, viewModel.pet.value?.id ?: "", date)
                }
                val e = TrackerItem.Builder()
                    .setEventType(TrackerItem.EventType.FEED)
                    .setDate(date)
                    .setMillis(System.currentTimeMillis())
                    .setCupsFood(cupsFood)
                    .setPetId(viewModel.pet.value?.id ?: "")
                    .setItemType("event")
                    .setId(randomStringGenerator.generate(30))
                    .build()
                viewModel.addTrackerItem(e)
            }
        }
    }

    private fun addDayToList(c: Calendar, petId: String, date: String) {
        val day = TrackerItem.Builder()
            .setDate(date)
            .setItemType("day")
            .setPetId(petId)
            .setId(randomStringGenerator.generate(30))
            .build()
        val now = System.currentTimeMillis()
        c.timeInMillis = now
        c[Calendar.HOUR] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[Calendar.MILLISECOND] = 0
        c.add(Calendar.MILLISECOND, viewModel.gmtTimeZone.getOffset(now))
        day.setUtcMillis(c.timeInMillis)
        viewModel.addTrackerItem(day)
    }

    private fun openFab() {
        mTrackerFab.startAnimation(mRotateForward)
        mLetOutFab.startAnimation(mMiniAppear)
        mPottyFab.startAnimation(mMiniAppear)
        mFeedFab.startAnimation(mMiniAppear)
        mPottyFabLabel.startAnimation(mLabelAppear)
        mFeedFabLabel.startAnimation(mLabelAppear)
        mLetOutFabLabel.startAnimation(mLabelAppear)
        mIsFabOpen = true
    }

    private fun closeFab() {
        mTrackerFab.startAnimation(mRotateBackward)
        mLetOutFab.startAnimation(mMiniDisappear)
        mPottyFab.startAnimation(mMiniDisappear)
        mFeedFab.startAnimation(mMiniDisappear)
        mPottyFabLabel.startAnimation(mLabelDisappear)
        mFeedFabLabel.startAnimation(mLabelDisappear)
        mLetOutFabLabel.startAnimation(mLabelDisappear)
        mIsFabOpen = false
    }
}