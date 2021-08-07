package com.caitlynwiley.pettracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.caitlynwiley.pettracker.EventAdapter.TrackerViewHolder
import com.caitlynwiley.pettracker.models.TrackerItem
import com.caitlynwiley.pettracker.viewmodel.TrackerViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar

class EventAdapter(
    private var mFragView: View,
    private var viewModel: TrackerViewModel) : RecyclerView.Adapter<TrackerViewHolder>() {

    private var mDataset: ArrayList<TrackerItem> = ArrayList()
    private var mRecentlyDeletedItem: TrackerItem? = null
    private var mRecentlyDeletedDate: TrackerItem? = null
    private var mRecentlyDeletedItemPosition = 0
    private val forceDeleteEventVals: List<Int> = listOf(Snackbar.Callback.DISMISS_EVENT_SWIPE,
            Snackbar.Callback.DISMISS_EVENT_TIMEOUT, Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE)
    private val mContext: Context = mFragView.context

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class TrackerViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var timeTextView: TextView? = null
        var imageView: ImageView? = null
        var dateTextView: TextView? = null
        private var isDate = false

        init {
            if (v is CardView) {
                timeTextView = v.findViewById(R.id.time_text)
                imageView = v.findViewById(R.id.event_icon)
                isDate = false
            } else {
                dateTextView = v.findViewById(R.id.date_textview)
                isDate = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TrackerViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        return TrackerViewHolder(v)
    }

    override fun onBindViewHolder(holder: TrackerViewHolder, position: Int) {
        val o = mDataset[position]
        if (o.itemType == "day") {
            holder.dateTextView!!.text = o.getDisplayDate(mContext)
        } else {
            holder.timeTextView!!.text = o.localTime
            holder.imageView!!.setImageResource(o.drawableResId)
        }
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mDataset[position].itemType.equals("day", ignoreCase = true))
            R.layout.date_header
        else R.layout.tracker_event
    }

    fun getItem(i: Int): TrackerItem {
        return mDataset[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    fun deleteItem(position: Int) {
        mRecentlyDeletedItem = mDataset[position]
        mRecentlyDeletedItemPosition = position
        mDataset.removeAt(position)
        notifyItemRemoved(position)
        if (deletedLastItemOnDay()) {
            mRecentlyDeletedDate = mDataset[position - 1]
            mDataset.removeAt(position - 1)
            notifyItemRemoved(position - 1)
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
            notifyItemInserted(mRecentlyDeletedItemPosition - 1)
        }
        mDataset.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem!!)
        notifyItemInserted(mRecentlyDeletedItemPosition)
    }

    fun addItem(item: TrackerItem) {
        mDataset.add(item)
    }

    fun setItems(list: List<TrackerItem>?) {
//        val initialSize = mDataset.size
//        mDataset = ArrayList()
        mDataset = list?.toCollection(ArrayList()) ?: ArrayList()
        notifyItemRangeChanged(0, mDataset.size)
    }

    val mostRecentDate: String?
        get() {
            if (mDataset.isEmpty()) return ""
            val lastItem = mDataset[mDataset.size - 1]
            return lastItem.date
        }
}