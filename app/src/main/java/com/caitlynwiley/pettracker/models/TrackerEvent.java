package com.caitlynwiley.pettracker.models;

import android.annotation.TargetApi;
import android.icu.util.TimeZone;
import android.util.Log;

import com.caitlynwiley.pettracker.R;
import com.google.firebase.database.Exclude;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TrackerEvent extends TrackerItem {

    private EventType type;
    private WalkLength walkLength;
    @Exclude
    private String localTime;
    private String petId;
    private double cupsFood;
    private boolean number1;
    private boolean number2;


    @SuppressWarnings("unused")
    public TrackerEvent() {

    }

    public TrackerEvent(EventType type) {
        this.type = type;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetId() {
        return petId;
    }

    @Override
    public void setUtcMillis(long utcMillis) {
        super.setUtcMillis(utcMillis);
        setLocalTime(utcMillis);
    }

    @Exclude
    @TargetApi(24)
    private void setLocalTime(long utcMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(utcMillis);
        localTime = String.format(Locale.US, "%d:%02d %s", c.get(Calendar.HOUR) == 0 ? 12 : c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
                c.get(Calendar.AM_PM) == Calendar.AM? "am" : "pm");
    }

    public String getLocalTime() {
        return localTime;
    }

    public int getDrawableResId() {
        switch (type) {
            case POTTY:
                return R.drawable.ic_dog_poop_64dp;
            case FEED:
                return R.drawable.ic_dog_bowl_64dp;
            case WALK:
                return R.drawable.ic_dog_walk_64dp;
        }
        return R.drawable.ic_clock_black_24dp; // default for now
    }

    @Exclude
    public EventType getTypeAsEnum(){
        return type;
    }

    // these methods are a Firebase 9.0.0 hack to handle the enum
    public String getType(){
        if (type == null){
            return null;
        } else {
            return type.name();
        }
    }

    public void setType(String type){
        if (type == null){
            this.type = null;
        } else {
            this.type = EventType.valueOf(type);
        }
    }

    public double getCupsFood() {
        return cupsFood;
    }

    public TrackerEvent setCupsFood(double cupsFood) {
        this.cupsFood = cupsFood;
        return this;
    }

    public boolean isNumber1() {
        return number1;
    }

    public void setNumber1(boolean number1) {
        this.number1 = number1;
    }

    public boolean isNumber2() {
        return number2;
    }

    public void setNumber2(boolean number2) {
        this.number2 = number2;
    }

    public void setWalkLength(int hours, int minutes) {
        this.walkLength = new WalkLength(hours, minutes);
    }

    public enum EventType {
        POTTY,
        WALK,
        FEED
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, localTime, getDate(), getId(), petId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackerEvent)) return false;
        TrackerEvent that = (TrackerEvent) o;
        return type == that.type &&
                Objects.equals(localTime, that.localTime) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(petId, that.petId);
    }

    public static class Builder {

        private TrackerEvent event;

        public Builder(EventType type) {
            event = new TrackerEvent();
            event.setType(type.name());
            event.setItemType("event");
        }

        // ... (setters)
        public Builder setDate(String date) {
            event.setDate(date);
            return this;
        }

        public Builder setMillis(long millis) {
            event.setUtcMillis(millis);
            return this;
        }

        public Builder setId(String id) {
            event.setId(id);
            return this;
        }

        public Builder setPetId(String id) {
            event.setPetId(id);
            return this;
        }

        public Builder setCupsFood(double cups) {
            event.setCupsFood(cups);
            return this;
        }

        public Builder setNumber1(boolean went) {
            event.setNumber1(went);
            return this;
        }

        public Builder setNumber2(boolean went) {
            event.setNumber2(went);
            return this;
        }

        public Builder setWalkLength(int hours, int minutes) {
            event.setWalkLength(hours, minutes);
            return this;
        }

        public TrackerEvent build() {
            return event;
        }
    }
}
