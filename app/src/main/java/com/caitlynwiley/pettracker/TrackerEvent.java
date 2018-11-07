package com.caitlynwiley.pettracker;

import android.graphics.drawable.Drawable;

import java.util.Calendar;
import java.util.Locale;

public class TrackerEvent {

    private EventType type;
    private String time;

    public TrackerEvent() {

    }

    public TrackerEvent(Calendar c, EventType type) {
        setTime(c);
        this.type = type;
    }

    private void setTime(Calendar c) {
        time = String.format(Locale.US, "%d:%d %s", c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
                c.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm");
    }

    public String getTime() {
        return time;
    }

    public int getDrawableResId() {
        switch (type) {
            case POOP:
                return R.drawable.ic_dog_poop_64dp;
            case FEED:
                return R.drawable.ic_dog_bowl_64dp;
            case WALK:
                return R.drawable.ic_dog_walk_64dp;
        }
        return R.drawable.ic_clock; // default for now
    }

    public enum EventType {
        POOP, WALK, FEED
    }
}
