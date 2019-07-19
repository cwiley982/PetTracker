package com.caitlynwiley.pettracker;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private ArrayList<TrackerEvent> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTextView;
        private TextView titleTextView;
        private ImageView imageView;

        public MyViewHolder(CardView v) {
            super(v);
            timeTextView = v.findViewById(R.id.time_text);
            titleTextView = v.findViewById(R.id.event_name);
            imageView = v.findViewById(R.id.event_icon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(ArrayList<TrackerEvent> myDataset) {
        mDataset = myDataset;
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
        holder.titleTextView.setText(((TrackerEvent) getItem(position)).getTitle());
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
}