package com.caitlynwiley.pettracker

import android.view.View
import com.caitlynwiley.pettracker.models.TrackerItem
import com.caitlynwiley.pettracker.viewmodel.TrackerViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar

class EventAdapter(
    private var mFragView: View,
    private var viewModel: TrackerViewModel) {

    private var mDataset: ArrayList<TrackerItem> = ArrayList()
    private var mRecentlyDeletedItem: TrackerItem? = null
    private var mRecentlyDeletedDate: TrackerItem? = null
    private var mRecentlyDeletedItemPosition = 0
    private val forceDeleteEventVals: List<Int> = listOf(Snackbar.Callback.DISMISS_EVENT_SWIPE,
            Snackbar.Callback.DISMISS_EVENT_TIMEOUT, Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE)

    fun deleteItem(position: Int) {
        mRecentlyDeletedItem = mDataset[position]
        mRecentlyDeletedItemPosition = position
        mDataset.removeAt(position)
        if (deletedLastItemOnDay()) {
            mRecentlyDeletedDate = mDataset[position - 1]
            mDataset.removeAt(position - 1)
        } else {
            mRecentlyDeletedDate = null
        }
        showUndoSnackbar()
    }

    private fun deletedLastItemOnDay(): Boolean {
        // if item before is a date (no preceding events) and either deleted item was last in the list
        // or the next item is a new date, then the deleted item was the last one for that date
        return (mDataset[mRecentlyDeletedItemPosition - 1].itemType.equals("day", ignoreCase = true)
                && (mDataset.size == mRecentlyDeletedItemPosition || mDataset[mRecentlyDeletedItemPosition].itemType.equals("day", ignoreCase = true)))
    }

    private fun showUndoSnackbar() {
        val view = mFragView
        val snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.snack_bar_undo) { undoDelete() }
        snackbar.addCallback(object : BaseCallback<Snackbar?>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (forceDeleteEventVals.contains(event)) {
                    viewModel.removeTrackerItem(mRecentlyDeletedItemPosition)
                    if (mRecentlyDeletedDate != null) {
                        viewModel.removeTrackerItem(mRecentlyDeletedItemPosition - 1)
                        mRecentlyDeletedDate = null
                    }

                    mRecentlyDeletedItemPosition = -1
                    mRecentlyDeletedItem = null
//                    mRef.child("pets").child(mRecentlyDeletedItem!!.petId).child("events").child(mRecentlyDeletedItem!!.itemId).setValue(null)
//                    mRef.child("pets").child(mRecentlyDeletedItem!!.petId).child("events").child(mRecentlyDeletedDate!!.itemId).setValue(null)
                }
            }
        })
        snackbar.show()
    }

    private fun undoDelete() {
        // order below is important, events AT index get shifted right
        if (mRecentlyDeletedDate != null) {
            mDataset.add(mRecentlyDeletedItemPosition - 1, mRecentlyDeletedDate!!)
        }
        mDataset.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem!!)
    }
}