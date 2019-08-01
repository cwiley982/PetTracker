package com.caitlynwiley.pettracker.models;

import com.caitlynwiley.pettracker.R;
import com.google.firebase.database.Exclude;

import java.util.Objects;

public class TrackerEvent extends TrackerItem {

    private EventType type;
    private String time;
    private String petId;
    private int hours;
    private int minutes;
    private double cupsFood;
    private boolean number1;
    private boolean number2;


    @SuppressWarnings("unused")
    public TrackerEvent() {

    }

    public TrackerEvent(String when, EventType type) {
        // when has format mm/dd/yyyy hh:mm am/pm
        setWhen(when);
        this.type = type;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetId() {
        return petId;
    }

    protected void setWhen(String when) {
        super.setDate(when.substring(0, 10));
        time = when.substring(11);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
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

    // these methods are just a Firebase 9.0.0 hack to handle the enum
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

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
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

    public enum EventType {
        POTTY,
        WALK,
        FEED
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, time, getDate(), getId(), petId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackerEvent)) return false;
        TrackerEvent that = (TrackerEvent) o;
        return type == that.type &&
                Objects.equals(time, that.time) &&
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
        public Builder setWhen(String when) {
            event.setWhen(when);
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

        public Builder setHours(int hours) {
            event.setHours(hours);
            return this;
        }

        public Builder setMinutes(int mins) {
            event.setMinutes(mins);
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

        public TrackerEvent build() {
            return event;
        }
    }
}
