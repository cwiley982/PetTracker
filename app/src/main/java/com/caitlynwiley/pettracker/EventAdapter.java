package com.caitlynwiley.pettracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caitlynwiley.pettracker.models.Day;
import com.caitlynwiley.pettracker.models.TrackerEvent;
import com.caitlynwiley.pettracker.models.TrackerItem;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE;
import static com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_SWIPE;
import static com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_TIMEOUT;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.TrackerViewHolder> {

    private ArrayList<TrackerItem> mDataset;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private TrackerEvent mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View mFragView;
    private List<Integer> forceDeleteEventVals = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class TrackerViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTextView;
        private ImageView imageView;
        private TextView dateTextView;
        private boolean isDate;

        public TrackerViewHolder(View v) {
            super(v);
            if (v instanceof CardView) {
                timeTextView = v.findViewById(R.id.time_text);
                imageView = v.findViewById(R.id.event_icon);
                isDate = false;
            } else {
                dateTextView = v.findViewById(R.id.date_textview);
                isDate = true;
            }
        }
    }

    public EventAdapter(View fragView) {
        mDataset = new ArrayList<>();
        mFragView = fragView;
        forceDeleteEventVals.add(DISMISS_EVENT_SWIPE);
        forceDeleteEventVals.add(DISMISS_EVENT_TIMEOUT);
        forceDeleteEventVals.add(DISMISS_EVENT_CONSECUTIVE);
    }

    @Override
    public TrackerViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new TrackerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrackerViewHolder holder, int position) {
        Object o = mDataset.get(position);
        if (o instanceof Day) {
            holder.dateTextView.setText(((Day) o).getPrettyDate());
        } else {
            holder.timeTextView.setText(((TrackerEvent) o).getLocalTime());
            holder.imageView.setImageResource(((TrackerEvent) o).getDrawableResId());
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position) instanceof Day ? R.layout.date_header : R.layout.tracker_event;
    }

    public TrackerItem getItem(int i) {
        return mDataset == null ? null : mDataset.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = (TrackerEvent) mDataset.get(position);
        mRecentlyDeletedItemPosition = position;
        mDataset.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = mFragView;
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (forceDeleteEventVals.contains(event)) {
                    mRef.child("pets").child(mRecentlyDeletedItem.getPetId()).child("events").child(mRecentlyDeletedItem.getId()).setValue(null);
                }
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        mDataset.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public void addItem(TrackerItem item) {
        mDataset.add(item);
    }

    public void addItem(int index, TrackerItem item) {
        mDataset.add(index, item);
    }

    public void removeEvent(TrackerItem item) {
        mDataset.remove(item);
    }

    public String getMostRecentDate() {
        if (mDataset.size() == 0) return "";
        TrackerItem lastItem = mDataset.get(mDataset.size() - 1);
        return lastItem.getDate();
    }
}