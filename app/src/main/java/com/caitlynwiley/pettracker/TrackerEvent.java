package com.caitlynwiley.pettracker;

import android.graphics.drawable.Drawable;

import java.util.Calendar;
import java.util.Locale;

public class TrackerEvent {

    private EventType type;
    private String time;
    private String date;

    public TrackerEvent(Calendar c, EventType type) {
        setTime(c);
        this.type = type;
        setDate(c);
    }

    private void setTime(Calendar c) {
        time = String.format(Locale.US, "%d:%d %s", c.get(Calendar.HOUR) == 0 ? 12 : c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
                c.get(Calendar.AM_PM) == Calendar.AM ? "am" : "pm");
    }

    private void setDate(Calendar c) {
        date = String.format(Locale.US, "%d/%d/%d", c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
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

    public EventType getType() {
        return type;
    }

    public enum EventType {
        POOP,
        WALK,
        FEED
    }
}
