package com.caitlynwiley.pettracker.models;

public class WalkLength {

    private int hours;
    private int minutes;

    public WalkLength() {}

    public WalkLength(int hours, int minutes) {
        setHours(hours);
        setMinutes(minutes);
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
}
