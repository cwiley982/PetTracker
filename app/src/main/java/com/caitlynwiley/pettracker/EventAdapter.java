package com.caitlynwiley.pettracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caitlynwiley.pettracker.models.TrackerEvent;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE;
import static com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_SWIPE;
import static com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_TIMEOUT;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private ArrayList<TrackerEvent> mDataset;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private TrackerEvent mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View mFragView;
    private List<Integer> forceDeleteEventVals = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTextView;

        private ImageView imageView;

        public MyViewHolder(CardView v) {
            super(v);
            timeTextView = v.findViewById(R.id.time_text);
            imageView = v.findViewById(R.id.event_icon);
        }
    }

    public EventAdapter(View fragView) {
        mDataset = new ArrayList<>();
        mFragView = fragView;
        forceDeleteEventVals.add(DISMISS_EVENT_SWIPE);
        forceDeleteEventVals.add(DISMISS_EVENT_TIMEOUT);
        forceDeleteEventVals.add(DISMISS_EVENT_CONSECUTIVE);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tracker_event, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.timeTextView.setText(((TrackerEvent) getItem(position)).getTime());
        holder.imageView.setImageResource(((TrackerEvent) getItem(position)).getDrawableResId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Object getItem(int i) {
        return mDataset == null ? null : mDataset.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = mDataset.get(position);
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
        mRef.child("pets").child(mRecentlyDeletedItem.getPetId()).child("events").child(mRecentlyDeletedItem.getId()).setValue(mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public void addEvent(TrackerEvent e) {
        mDataset.add(e);
    }

    public void removeEvent(TrackerEvent e) {
        mDataset.remove(e);
    }

    public boolean contains(TrackerEvent e) {
        return mDataset.contains(e);
    }
}