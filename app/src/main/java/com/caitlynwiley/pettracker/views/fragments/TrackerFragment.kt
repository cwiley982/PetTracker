package com.caitlynwiley.pettracker.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.caitlynwiley.pettracker.EventAdapter
import com.caitlynwiley.pettracker.models.TrackerItem
import com.caitlynwiley.pettracker.repository.PetTrackerRepository
import com.caitlynwiley.pettracker.theme.PetTrackerTheme
import com.caitlynwiley.pettracker.viewmodel.TrackerViewModel
import com.caitlynwiley.pettracker.viewmodel.TrackerViewModelFactory
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.apache.commons.text.RandomStringGenerator
import java.util.*

class TrackerFragment : Fragment() {
    private val viewModel by viewModels<TrackerViewModel>(
        factoryProducer = { TrackerViewModelFactory(PetTrackerRepository(), "") }
    )

    private lateinit var mAdapter: EventAdapter

    private val randomStringGenerator: RandomStringGenerator =
        RandomStringGenerator.Builder().withinRange(charArrayOf('0', '9'), charArrayOf('a', 'z'),
            charArrayOf('A', 'Z')).build()

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PetTrackerTheme {
                    Surface {
                        val trackerItems by viewModel.trackerItems.observeAsState(listOf())
                        val isRefreshing by viewModel.isRefreshing.collectAsState()

                        SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                            onRefresh = { viewModel.refresh() }
                        ) {
                            TrackerScreen(items = trackerItems)
                        }
                    }
                }
            }
        }

//        mRotateForward = AnimationUtils.loadAnimation(context, R.anim.fab_spin_forward)
//        mRotateBackward = AnimationUtils.loadAnimation(context, R.anim.fab_spin_backward)
//
//        // setup swipe-to-delete and undo logic
//        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteHelper(mAdapter, requireContext()))
//        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /*
    Positive btn: "Save"
    onClick (pseudocode):
        // type
        val num1 = peeCheckBox.isChecked
        val num2 = poopCheckBox.isChecked
        //date
        val c = Calendar.getInstance()
        val date = String.format(
            Locale.US, "%02d/%02d/%4d",
            c[Calendar.MONTH] + 1, c[Calendar.DAY_OF_MONTH],
            c[Calendar.YEAR]
        )
        if (mAdapter.mostRecentDate != date) {
            addDayToList(c, viewModel.pet.value?.id ?: "")
        }
        val e = TrackerItem.Builder()
            .setEventType(TrackerItem.EventType.POTTY)
            .setMillis(System.currentTimeMillis())
            .setNumber1(num1)
            .setNumber2(num2)
            .setPetId(viewModel.pet.value?.id ?: "")
            .setItemType("event")
            .setId(randomStringGenerator.generate(30))
            .build()
        viewModel.addTrackerItem(e)

    Negative btn: "Cancel"
    onClick: dialog.cancel()
     */

    /*
    Positive btn: "Save"
    onClick (pseudocode):
        val hours = hoursTextField.getValue
        val mins = minutesTextField.getValue
        //date
        val c = Calendar.getInstance()
        val date = String.format(
            Locale.US, "%02d/%02d/%4d",
            c[Calendar.MONTH] + 1, c[Calendar.DAY_OF_MONTH],
            c[Calendar.YEAR]
        )
        if (mAdapter.mostRecentDate != date) {
            addDayToList(c, viewModel.pet.value?.id ?: "")
        }
        val e = TrackerItem.Builder()
            .setEventType(TrackerItem.EventType.WALK)
            .setMillis(System.currentTimeMillis())
            .setWalkLength(hours, mins)
            .setPetId(viewModel.pet.value?.id ?: "")
            .setItemType("event")
            .setId(randomStringGenerator.generate(30))
            .build()
        viewModel.addTrackerItem(e)

    Negative btn: "Cancel"
    onClick: dialog.cancel()
     */

    /*
    Positive btn: "Save"
    onClick (pseudocode):
        if (cups.isEmpty() || cups.getValue == "0") {
            // stay open and show error message
        } else {
            // amount
            val cupsFood = cups.getValue
            //date
            val c = Calendar.getInstance()
            val date = String.format(
                Locale.US, "%02d/%02d/%4d",
                c[Calendar.MONTH] + 1, c[Calendar.DAY_OF_MONTH],
                c[Calendar.YEAR]
            )
            if (mAdapter.mostRecentDate != date) {
                addDayToList(c, viewModel.pet.value?.id ?: "")
            }
            val e = TrackerItem.Builder()
                .setEventType(TrackerItem.EventType.FEED)
                .setMillis(System.currentTimeMillis())
                .setCupsFood(cupsFood)
                .setPetId(viewModel.pet.value?.id ?: "")
                .setItemType("event")
                .setId(randomStringGenerator.generate(30))
                .build()
            viewModel.addTrackerItem(e)
        }

    Negative btn: "Cancel"
    onClick: dialog.cancel()
     */

    private fun addDayToList(c: Calendar, petId: String) {
        val day = TrackerItem.Builder()
            .setMillis(System.currentTimeMillis())
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
}