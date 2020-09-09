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
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class EventAdapter(fragView: View) : RecyclerView.Adapter<TrackerViewHolder>() {
    private var mDataset: ArrayList<TrackerItem?>?
    private val mRef = FirebaseDatabase.getInstance().reference
    private var mRecentlyDeletedItem: TrackerItem? = null
    private var mRecentlyDeletedDate: TrackerItem? = null
    private var mRecentlyDeletedItemPosition = 0
    private val mFragView: View
    private val forceDeleteEventVals: MutableList<Int> = ArrayList()
    private val mContext: Context

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
        val o = mDataset!![position]
        if (o!!.itemType == "day") {
            holder.dateTextView!!.text = o.getPrettyDate(mContext)
        } else {
            holder.timeTextView!!.text = o.localTime
            holder.imageView!!.setImageResource(o.drawableResId)
        }
    }

    override fun getItemCount(): Int {
        return mDataset!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mDataset!![position]!!.itemType.equals("day", ignoreCase = true)) R.layout.date_header else R.layout.tracker_event
    }

    fun getItem(i: Int): TrackerItem? {
        return if (mDataset == null) null else mDataset!![i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    fun deleteItem(position: Int) {
        mRecentlyDeletedItem = mDataset!![position]
        mRecentlyDeletedItemPosition = position
        mDataset!!.removeAt(position)
        if (deletedLastItemOnDay()) {
            mRecentlyDeletedDate = mDataset!![position - 1]
            mDataset!!.removeAt(position - 1)
        } else {
            mRecentlyDeletedDate = null
        }
        notifyDataSetChanged()
        showUndoSnackbar()
    }

    private fun deletedLastItemOnDay(): Boolean {
        // if item before is a date (no preceding events) and either deleted item was last in the list
        // or the next item is a new date, then the deleted item was the last one for that date
        return (mDataset!![mRecentlyDeletedItemPosition - 1]!!.itemType.equals("day", ignoreCase = true)
                && (mDataset!!.size == mRecentlyDeletedItemPosition || mDataset!![mRecentlyDeletedItemPosition]!!.itemType.equals("day", ignoreCase = true)))
    }

    private fun showUndoSnackbar() {
        val view = mFragView
        val snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.snack_bar_undo) { v: View? -> undoDelete() }
        snackbar.addCallback(object : BaseCallback<Snackbar?>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (forceDeleteEventVals.contains(event)) {
                    mRef.child("pets").child(mRecentlyDeletedItem!!.petId!!).child("events").child(mRecentlyDeletedItem!!.itemId!!).setValue(null)
                    mRef.child("pets").child(mRecentlyDeletedItem!!.petId!!).child("events").child(mRecentlyDeletedDate!!.itemId!!).setValue(null)
                }
            }
        })
        snackbar.show()
    }

    private fun undoDelete() {
        // order below is important, events AT index get shifted right
        if (mRecentlyDeletedDate != null) {
            mDataset!!.add(mRecentlyDeletedItemPosition - 1, mRecentlyDeletedDate)
        }
        mDataset!!.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem)
        notifyDataSetChanged()
    }

    fun addItem(item: TrackerItem?) {
        mDataset!!.add(item)
    }

    fun addItems(list: Map<String?, TrackerItem?>) {
        mDataset!!.addAll(list.values)
        for (item in mDataset!!) {
            if (item!!.itemType.equals("event", ignoreCase = true)) {
                item.setLocalTime()
            }
        }
        notifyDataSetChanged()
    }

    fun setItems(list: Map<String?, TrackerItem?>) {
        mDataset = ArrayList(list.values)
        notifyDataSetChanged()
    }

    fun removeEvent(item: TrackerItem?) {
        mDataset!!.remove(item)
    }

    val mostRecentDate: String?
        get() {
            if (mDataset!!.size == 0) return ""
            val lastItem = mDataset!![mDataset!!.size - 1]
            return lastItem!!.date
        }

    init {
        mDataset = ArrayList()
        mFragView = fragView
        mContext = fragView.context
        forceDeleteEventVals.add(Snackbar.Callback.DISMISS_EVENT_SWIPE)
        forceDeleteEventVals.add(Snackbar.Callback.DISMISS_EVENT_TIMEOUT)
        forceDeleteEventVals.add(Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE)
    }
}