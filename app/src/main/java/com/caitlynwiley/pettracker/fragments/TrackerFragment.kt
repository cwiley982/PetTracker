package com.caitlynwiley.pettracker.fragments

import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.caitlynwiley.pettracker.EventAdapter
import com.caitlynwiley.pettracker.FirebaseApi
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.SwipeToDeleteHelper
import com.caitlynwiley.pettracker.models.Pet
import com.caitlynwiley.pettracker.models.TrackerItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class TrackerFragment : Fragment(), View.OnClickListener {
    private lateinit var mFragView: View

//    private val mRotateForward: Animation = AnimationUtils.loadAnimation(context, R.anim.fab_spin_forward)
//    private val mRotateBackward: Animation = AnimationUtils.loadAnimation(context, R.anim.fab_spin_backward)
//    private val mMiniAppear: Animation = AnimationUtils.loadAnimation(context, R.anim.mini_fab_appear)
//    private val mMiniDisappear: Animation = AnimationUtils.loadAnimation(context, R.anim.mini_fab_disappear)
//    private val mLabelAppear: Animation = AnimationUtils.loadAnimation(context, R.anim.fab_label_appear)
//    private val mLabelDisappear: Animation = AnimationUtils.loadAnimation(context, R.anim.fab_label_disappear)

    private lateinit var mPottyFabLabel: TextView
    private lateinit var mFeedFabLabel: TextView
    private lateinit var mLetOutFabLabel: TextView
    private lateinit var mTrackerFab: FloatingActionButton
    private lateinit var mPottyFab: FloatingActionButton
    private lateinit var mFeedFab: FloatingActionButton
    private lateinit var mLetOutFab: FloatingActionButton

    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mAdapter: EventAdapter? = null
    private val pets = ArrayList<String>()
    private var mUID: String? = null
    private var mUser: FirebaseUser? = null
    private var mIsFabOpen = false
    private var mPet: Pet? = null
    private var mPetId: String? = null
    private val mLondonTZ: TimeZone = TimeZone.getTimeZone("Europe/London")

    private lateinit var retrofit: Retrofit
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        retrofit = Retrofit.Builder().baseUrl(FirebaseApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        mFragView = inflater.inflate(R.layout.tracker_fragment, container, false)
        mDatabase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
        mUID = mUser?.uid

//        mMiniAppear.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation) {}
//            override fun onAnimationEnd(animation: Animation) {
//                mPottyFab.visibility = View.VISIBLE
//                mLetOutFab.visibility = View.VISIBLE
//                mFeedFab.visibility = View.VISIBLE
//                mPottyFabLabel.visibility = View.VISIBLE
//                mLetOutFabLabel.visibility = View.VISIBLE
//                mFeedFabLabel.visibility = View.VISIBLE
//            }
//
//            override fun onAnimationRepeat(animation: Animation) {}
//        })

//        mMiniDisappear.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation) {}
//            override fun onAnimationEnd(animation: Animation) {
//                mPottyFab.visibility = View.GONE
//                mLetOutFab.visibility = View.GONE
//                mFeedFab.visibility = View.GONE
//                mPottyFabLabel.visibility = View.GONE
//                mLetOutFabLabel.visibility = View.GONE
//                mFeedFabLabel.visibility = View.GONE
//            }
//
//            override fun onAnimationRepeat(animation: Animation) {}
//        })

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

        mSwipeRefreshLayout?.setOnRefreshListener {
            val api = retrofit.create(FirebaseApi::class.java)
            api?.getEvents(mPetId)!!.enqueue(object : Callback<Map<String?, TrackerItem?>?> {
                override fun onResponse(
                    call: Call<Map<String?, TrackerItem?>?>,
                    response: Response<Map<String?, TrackerItem?>?>
                ) {
                    if (response.body() != null) {
                        mFragView.findViewById<View>(R.id.no_events_label).visibility = View.GONE
                        mAdapter!!.setItems(response.body()!!)
                    } else {
                        mFragView.findViewById<View>(R.id.no_events_label).visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Map<String?, TrackerItem?>?>, t: Throwable) {}
            })
            mSwipeRefreshLayout?.isRefreshing = false
        }
        val recyclerView: RecyclerView = mFragView.findViewById(R.id.tracker_items)

        // use a linear layout manager
        recyclerView.layoutManager = LinearLayoutManager(mFragView.context)

        // specify an adapter
        mAdapter = EventAdapter(mFragView)
        recyclerView.adapter = mAdapter
        val api = retrofit.create(FirebaseApi::class.java)
        api?.getPets(mUID)!!.enqueue(object : Callback<Map<String?, Boolean?>?> {
            override fun onResponse(
                call: Call<Map<String?, Boolean?>?>,
                response: Response<Map<String?, Boolean?>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!.forEach {
                        it.key?.let { it1 -> pets.add(it1) }
                    }
                    mPetId = pets[0]
                    items
                    getPetById(mPetId)
                }
            }

            override fun onFailure(call: Call<Map<String?, Boolean?>?>, t: Throwable) {}
        })
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteHelper(mAdapter!!, requireContext()))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return mFragView
    }

    private val items: Unit
        get() {
            val api = retrofit.create(FirebaseApi::class.java)
            api.getEvents(mPetId)!!.enqueue(object : Callback<Map<String?, TrackerItem?>?> {
                override fun onResponse(
                    call: Call<Map<String?, TrackerItem?>?>,
                    response: Response<Map<String?, TrackerItem?>?>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            mAdapter!!.addItems(response.body()!!)
                        }
                        Log.d("count", "" + mAdapter!!.itemCount)
                    } else {
                        Log.d("body", response.errorBody().toString())
                        onFailure(call, Throwable(response.message()))
                    }
                }

                override fun onFailure(call: Call<Map<String?, TrackerItem?>?>, t: Throwable) {
                    Log.e("json-error", "Error getting events: " + t.message)
                }
            })
        }

    private fun getPetById(id: String?) {
        val api = retrofit.create(FirebaseApi::class.java)
        api.getPet(id)!!.enqueue(object : Callback<Pet?> {
            override fun onResponse(call: Call<Pet?>, response: Response<Pet?>) {
                if (response.isSuccessful && response.body() != null) {
                    mPet = response.body()
                }
            }

            override fun onFailure(call: Call<Pet?>, t: Throwable) {
                t.message?.let { Log.d("error", it) }
            }
        })
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
                if (mAdapter!!.mostRecentDate != date) {
                    addDayToList(c, mPetId, date)
                }
                val id = mDatabase.child("pets").child(mPetId!!).child("events").push().key
                val e = TrackerItem.Builder()
                    .setType(TrackerItem.EventType.POTTY)
                    .setDate(date)
                    .setMillis(System.currentTimeMillis())
                    .setNumber1(num1)
                    .setNumber2(num2)
                    .setPetId(mPetId)
                    .setItemType("event")
                    .setId(id)
                    .build()
                PostEventTask().execute(e)
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
                if (mAdapter!!.mostRecentDate != date) {
                    addDayToList(c, mPetId, date)
                }
                val id = mDatabase.child("pets").child(mPetId!!).child("events").push().key
                val e = TrackerItem.Builder()
                    .setType(TrackerItem.EventType.WALK)
                    .setDate(date)
                    .setMillis(System.currentTimeMillis())
                    .setWalkLength(hours, mins)
                    .setPetId(mPetId)
                    .setItemType("event")
                    .setId(id)
                    .build()
                PostEventTask().execute(e)
            }
            .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.cancel() }
            .create()
            .show()
    }

    private fun trackFeed() {
        val v = layoutInflater.inflate(R.layout.track_meal_dialog, null)
        v.findViewById<View>(R.id.error_msg).visibility = View.GONE
        //((RadioButton) v.findViewById(R.id.pet0)).setText(mPet.getName());
        val alertDialog = AlertDialog.Builder(
            requireContext()
        )
            .setView(v)
            .setPositiveButton(R.string.save, null) // set below
            .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.cancel() }
            .create()
        alertDialog.show()
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
                if (mAdapter!!.mostRecentDate != date) {
                    addDayToList(c, mPetId, date)
                }
                val id = mDatabase.child("pets").child(mPetId!!).child("events").push().key
                val e = TrackerItem.Builder()
                    .setType(TrackerItem.EventType.FEED)
                    .setDate(date)
                    .setMillis(System.currentTimeMillis())
                    .setCupsFood(cupsFood)
                    .setPetId(mPetId)
                    .setItemType("event")
                    .setId(id)
                    .build()
                PostEventTask().execute(e)
                alertDialog.dismiss()
            }
        }
    }

    private fun addDayToList(c: Calendar, petId: String?, date: String) {
        val day = TrackerItem.Builder()
            .setDate(date)
            .setItemType("day")
            .setPetId(petId)
            .build()
        val dayId = mDatabase.child("pets").child(petId!!).child("events").push().key
        day.itemId = dayId
        val now = System.currentTimeMillis()
        c.timeInMillis = now
        c[Calendar.HOUR] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[Calendar.MILLISECOND] = 0
        c.add(Calendar.MILLISECOND, mLondonTZ.getOffset(now))
        day.setUtcMillis(c.timeInMillis)
        PostEventTask().execute(day)
    }

    private fun openFab() {
//        mTrackerFab.startAnimation(mRotateForward)
//        mLetOutFab.startAnimation(mMiniAppear)
//        mPottyFab.startAnimation(mMiniAppear)
//        mFeedFab.startAnimation(mMiniAppear)
//        mPottyFabLabel.startAnimation(mLabelAppear)
//        mFeedFabLabel.startAnimation(mLabelAppear)
//        mLetOutFabLabel.startAnimation(mLabelAppear)
        mIsFabOpen = true
    }

    private fun closeFab() {
//        mTrackerFab.startAnimation(mRotateBackward)
//        mLetOutFab.startAnimation(mMiniDisappear)
//        mPottyFab.startAnimation(mMiniDisappear)
//        mFeedFab.startAnimation(mMiniDisappear)
//        mPottyFabLabel.startAnimation(mLabelDisappear)
//        mFeedFabLabel.startAnimation(mLabelDisappear)
//        mLetOutFabLabel.startAnimation(mLabelDisappear)
        mIsFabOpen = false
    }

    inner class PostEventTask : AsyncTask<TrackerItem, Void?, Void?>() {
        override fun doInBackground(vararg items: TrackerItem): Void? {
            retrofit.create(FirebaseApi::class.java)
                .addEvent(items[0].petId, items[0].itemId, items[0])!!
                .enqueue(object : Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        if (response.isSuccessful) {
                            Log.d("addEvent", "Event added")
                        }
                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Log.d("addEvent", "Event add failed")
                    }
                })
            mAdapter!!.addItem(items[0])
            return null
        }
    }
}